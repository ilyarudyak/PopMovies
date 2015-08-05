package com.ilyarudyak.android.popmovies;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private View mRootView;
    private LinearLayout mMainLinearLayout;
    private TextView mOriginalTitle;
    private ImageView mPosterImageView;
    private TextView mReleaseDate;
    private TextView mUserRating;
    private TextView mPlotSynopsis;

    private Integer mMovieId;
    private Bundle mBundle;

    private List<Movie.Trailer> mTrailerList;
    private ShareActionProvider mShareActionProvider;

    private Typeface qsRegular;
    private Typeface qsBold;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainLinearLayout(inflater, container);
        setShareButton();
        setQuattrocentoFont();
        addListOfTrailers(inflater, container);
        setReviewsList(inflater, container);
        setPosterImage();

        return mRootView;
    }

    // helper methods to set layout
    private void setMainLinearLayout (LayoutInflater inflater, ViewGroup container) {

        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mMainLinearLayout = (LinearLayout) mRootView.findViewById(R.id.mainLinearLayout);

        Bundle args = getArguments();
        if (args != null) {
            mBundle = args;
            Log.d(LOG_TAG, "args is not null");
            Log.d(LOG_TAG, "what about bundle" + mBundle);
        }

        mOriginalTitle = (TextView) mRootView.findViewById(R.id.textViewOriginalTitle);
        mPosterImageView = (ImageView) mRootView.findViewById(R.id.imageViewPoster);
        mReleaseDate = (TextView) mRootView.findViewById(R.id.textViewReleaseDate);
        mUserRating = (TextView) mRootView.findViewById(R.id.textViewUserRating);
        mPlotSynopsis = (TextView) mRootView.findViewById(R.id.textViewPlotSynopsis);

        if (mBundle != null) {
            mMovieId = mBundle.getInt(Movie.TMDB_ID, 0);
            mOriginalTitle.setText(mBundle.getString(Movie.TMDB_ORIGINAl_TITLE));
            Log.d(LOG_TAG, "release date" + mBundle.getString(Movie.TMDB_RELEASE_DATE));
            mReleaseDate.setText(mBundle.getString(Movie.TMDB_RELEASE_DATE).substring(0, 4));
            final String MAX_RATING = "/10";
            mUserRating.setText(mBundle.getString(Movie.TMDB_USER_RATING) + MAX_RATING);
            mPlotSynopsis.setText(mBundle.getString(Movie.TMDB_PLOT_SYNOPSIS));
        }
    }
    private void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }
    }
    private void setReviewsList(LayoutInflater inflater, ViewGroup container) {
        List<String> reviewsList = getActivity().getIntent()
                .getStringArrayListExtra(Movie.REVIEW_LIST);
        if (reviewsList != null && reviewsList.size() > 0) {
            for (String review : reviewsList) {
                View reviewView = inflater.inflate(R.layout.review, container, false);
                TextView reviewTextView = (TextView) reviewView.findViewById(R.id.review_text_view);
                reviewTextView.setText(review);
                reviewTextView.setTypeface(qsRegular);
                mMainLinearLayout.addView(reviewView);
                View divider = inflater.inflate(R.layout.divider, container, false);
                mMainLinearLayout.addView(divider);
            }
        }
    }
    private void setQuattrocentoFont() {
        qsRegular = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Regular.ttf");
        qsBold = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Bold.ttf");

        mOriginalTitle.setTypeface(qsBold);
        mReleaseDate.setTypeface(qsRegular);
        mUserRating.setTypeface(qsRegular);
        mPlotSynopsis.setTypeface(qsRegular);
    }
    private void addListOfTrailers(LayoutInflater inflater, ViewGroup container) {
        // stage 2: add list of trailers
        if (mBundle != null) {
            mTrailerList = mBundle
                    .getParcelableArrayList(Movie.TRAILER_LIST);
            for (Movie.Trailer mt : mTrailerList) {
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

                mMainLinearLayout.addView(trailerView);

                View divider = inflater.inflate(R.layout.divider, container, false);
                mMainLinearLayout.addView(divider);
            }
        }
    }
    private void setPosterImage() {
        if (mBundle != null) {
            String posterPathAbsolute = mBundle.getString(Movie.TMDB_POSTER_PATH_ABSOLUTE);
            Picasso.with(getActivity())
                    .load(posterPathAbsolute)
                    .placeholder(R.raw.place_holder)
                    .resize(300, 450)
                    .into(mPosterImageView);
        }
    }

    // ------------- favorites button ---------------

    private void setShareButton() {

        Button shareButton = (Button) mRootView.findViewById(R.id.button_favorite);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoritesUtils.putFavorities(getActivity(), mMovieId);
            }
        });

        // make button inactive for favorites
        if (mMovieId != null && isFavorite()) {
            shareButton.setEnabled(false);
        }
    }

    // check if movie is already favorite
    private boolean isFavorite() {
        Set<String> favorites = FavoritesUtils.getFavorities(getActivity());
        if (mMovieId != null) {
            return favorites != null && favorites.contains(Integer.toString(mMovieId));
        }
        return false;
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

        if (mTrailerList != null) {
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


}
