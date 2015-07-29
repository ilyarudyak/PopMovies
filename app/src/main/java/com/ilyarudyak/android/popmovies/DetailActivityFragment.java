package com.ilyarudyak.android.popmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.utils.FavoritesUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private View mRootView;
    private LinearLayout mainLinearLayout;
    private Integer mMovieId;

    private List<Movie.Trailer> mTrailerList;
    private ShareActionProvider mShareActionProvider;

    private Typeface qsRegular;
    private Typeface qsBold;

    public DetailActivityFragment() {
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mainLinearLayout = (LinearLayout) mRootView.findViewById(R.id.mainLinearLayout);

        qsRegular = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Regular.ttf");
        qsBold = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Bold.ttf");

        TextView originalTitle = (TextView) mRootView.findViewById(R.id.textViewOriginalTitle);
        ImageView posterImageView = (ImageView) mRootView.findViewById(R.id.imageViewPoster);
        TextView releaseDate = (TextView) mRootView.findViewById(R.id.textViewReleaseDate);
        TextView userRating = (TextView) mRootView.findViewById(R.id.textViewUserRating);
        TextView plotSynopsis = (TextView) mRootView.findViewById(R.id.textViewPlotSynopsis);

        // detail activity called via intent.
        // inspect the intent for data.
        final Intent intent = getActivity().getIntent();
        mMovieId = intent.getIntExtra(Movie.TMDB_ID, 0);
        originalTitle.setText(intent.getStringExtra(Movie.TMDB_ORIGINAl_TITLE));
        releaseDate.setText(intent.getStringExtra(Movie.TMDB_RELEASE_DATE).substring(0,4));
        final String MAX_RATING = "/10";
        userRating.setText(intent.getStringExtra(Movie.TMDB_USER_RATING) + MAX_RATING);
        plotSynopsis.setText(intent.getStringExtra(Movie.TMDB_PLOT_SYNOPSIS));

        setShareButton();

        // change font to QuattrocentoSans
        originalTitle.setTypeface(qsBold);
        releaseDate.setTypeface(qsRegular);
        userRating.setTypeface(qsRegular);
        plotSynopsis.setTypeface(qsRegular);

        // stage 2: add list of trailers
        mTrailerList =  getActivity().getIntent()
                .getParcelableArrayListExtra(Movie.TRAILER_LIST);
        for(Movie.Trailer mt : mTrailerList) {
            View trailerView = inflater.inflate(R.layout.trailer, container, false);
            final String url = mt.getTrailerPathAbsolute();

            ImageButton trailerButton = (ImageButton) trailerView
                    .findViewById(R.id.trailer_image_button);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTrailer(url);
                }
            });

            TextView trailerTitle = (TextView) trailerView
                    .findViewById(R.id.trailer_title_text_view);
            trailerTitle.setText(mt.getTrailerName());
            trailerTitle.setTypeface(qsRegular);

            mainLinearLayout.addView(trailerView);

            View divider = inflater.inflate(R.layout.divider, container, false);
            mainLinearLayout.addView(divider);
        }

        // stage 2 add list of reviews
        List<String> reviewsList = getActivity().getIntent()
                .getStringArrayListExtra(Movie.REVIEW_LIST);
        if (reviewsList != null) { Log.i(LOG_TAG, reviewsList.toString()); }
        for (String review : reviewsList) {
            View reviewView = inflater.inflate(R.layout.review, container, false);
            TextView reviewTextView = (TextView) reviewView.findViewById(R.id.review_text_view);
            reviewTextView.setText(review);
            reviewTextView.setTypeface(qsRegular);
            mainLinearLayout.addView(reviewView);
            View divider = inflater.inflate(R.layout.divider, container, false);
            mainLinearLayout.addView(divider);
        }



        String posterPathAbsolute = intent.getStringExtra(Movie.TMDB_POSTER_PATH_ABSOLUTE);
        Picasso.with(getActivity())
                .load(posterPathAbsolute)
                .placeholder(R.raw.place_holder)
                .resize(300, 450)
                .into(posterImageView);

        return mRootView;
    }

    private void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }
    }
    // ------------- fovorites button ---------------

    private void setShareButton() {

        Button shareButton = (Button) mRootView.findViewById(R.id.button_favorite);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoritesUtils.putFavorities(getActivity(), mMovieId);
            }
        });

        // make button inactive for favorites
        if (isFavorite()) {
            shareButton.setEnabled(false);
        }
    }

    // check if movie is already favorite
    private boolean isFavorite() {
        Set<String> favorites = FavoritesUtils.getFavorities(getActivity());
        return favorites != null && favorites.contains(Integer.toString(mMovieId));
    }

    // ------------- menu  and share intent ---------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // now this fragment can handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        setShareIntent();
    }

    // this method shares first trailer
    private void setShareIntent() {

        String trailerPath = mTrailerList.get(0).getTrailerPathAbsolute();
        String shareTag = " #" + getActivity().getIntent()
                .getStringExtra(Movie.TMDB_ORIGINAl_TITLE);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                trailerPath + shareTag);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }


    }


}
