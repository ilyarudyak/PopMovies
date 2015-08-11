package com.ilyarudyak.android.popmovies;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
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
import com.ilyarudyak.android.popmovies.utils.FavDbUtils;
import com.ilyarudyak.android.popmovies.utils.FavPrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    private View mRootView;
    private LinearLayout mMainLinearLayout;
    private TextView mOriginalTitle;
    private ImageView mPosterImageView;
    private TextView mReleaseDate;
    private TextView mUserRating;
    private TextView mPlotSynopsis;

    private Integer mMovieId;
    public Bundle mBundle;

    private List<Movie.Trailer> mTrailerList;
    private ShareActionProvider mShareActionProvider;

    private Typeface qsRegular;
    private Typeface qsBold;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setBundle();
        setQuattrocentoFont();
        inflateMainLayout(inflater, container);
        setMainLayoutFromBundle();

        setTrailerList(inflater, container);
        setReviewList(inflater, container);

        return mRootView;
    }

    // helper methods to set layout
    private void setBundle() {
        // receive arguments from: a) detail activity on a phone
        // and b) from main activity on a tablet
        mBundle = getArguments();
    }
    private void inflateMainLayout(LayoutInflater inflater, ViewGroup container) {

        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mMainLinearLayout = (LinearLayout) mRootView.findViewById(R.id.mainLinearLayout);

        mOriginalTitle = (TextView) mRootView.findViewById(R.id.textViewOriginalTitle);
        mPosterImageView = (ImageView) mRootView.findViewById(R.id.imageViewPoster);
        mReleaseDate = (TextView) mRootView.findViewById(R.id.textViewReleaseDate);
        mUserRating = (TextView) mRootView.findViewById(R.id.textViewUserRating);
        mPlotSynopsis = (TextView) mRootView.findViewById(R.id.textViewPlotSynopsis);
    }
    public void setMainLayoutFromBundle() {
        setMainLinearLayout();
        setPosterImage();
        setFavButton();
    }
    private void setMainLinearLayout() {
        if (mBundle != null) {

            // we don't provide error handling for first 3 variables
            mMovieId = mBundle.getInt(Movie.TMDB_ID, 0);
            mOriginalTitle.setText(mBundle.getString(Movie.TMDB_ORIGINAl_TITLE));
            mReleaseDate.setText(mBundle.getString(Movie.RELEASE_YEAR));

            final String MAX_RATING = "/10";
            final String PLACEHOLDER_RATING = "-/10";
            String rating = mBundle.getString(Movie.TMDB_USER_RATING);
            if (rating != null) {
                mUserRating.setText(rating + MAX_RATING);
            } else {
                mUserRating.setText(PLACEHOLDER_RATING);
            }

            String plot = mBundle.getString(Movie.TMDB_PLOT_SYNOPSIS);
            if (plot != null && plot.length() != 0 && !plot.equals("null")) {
                mPlotSynopsis.setText(plot);
            } else {
                mPlotSynopsis.setText(getActivity().getString(R.string.placeholder_plot));
            }

            mOriginalTitle.setTypeface(qsBold);
            mReleaseDate.setTypeface(qsRegular);
            mUserRating.setTypeface(qsRegular);
            mPlotSynopsis.setTypeface(qsRegular);
        }
    }
    private void setPosterImage() {
        if (mBundle != null) {
            String posterPathAbsolute = mBundle.getString(Movie.TMDB_POSTER_PATH_ABSOLUTE);
            Picasso.with(getActivity())
                    .load(posterPathAbsolute)
                    .placeholder(R.raw.placeholder_detail)
                    .resize(300, 450)
                    .into(mPosterImageView);
        }
    }
    private void setTrailerList(LayoutInflater inflater, ViewGroup container) {
        // stage 2: add list of trailers
        if (mBundle != null) {
            mTrailerList = mBundle
                    .getParcelableArrayList(Movie.TRAILER_LIST);
            if (mTrailerList != null && mTrailerList.size() > 0) {

                View v = inflater.inflate(R.layout.divider, container, false);
                mMainLinearLayout.addView(v);

                LinearLayout l = (LinearLayout) inflater.inflate(R.layout.trailers_title,
                        container, false);
                TextView tv = (TextView) l.findViewById(R.id.trailers_title);
                tv.setTypeface(qsRegular);
                mMainLinearLayout.addView(l);

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
    }
    private void setReviewList(LayoutInflater inflater, ViewGroup container) {
        if (mBundle != null) {
            List<String> mReviewList = mBundle.getStringArrayList(Movie.REVIEW_LIST);
            if (mReviewList != null && mReviewList.size() > 0) {

                LinearLayout l = (LinearLayout) inflater.inflate(R.layout.reviews_title,
                        container, false);
                TextView tv = (TextView) l.findViewById(R.id.reviews_title);
                tv.setTypeface(qsRegular);
                mMainLinearLayout.addView(l);

                for (String review : mReviewList) {
                    View reviewView = inflater.inflate(R.layout.review, container, false);
                    TextView reviewTextView = (TextView) reviewView.findViewById(
                            R.id.review_text_view);
                    reviewTextView.setText(review);
                    reviewTextView.setTypeface(qsRegular);
                    mMainLinearLayout.addView(reviewView);
                    View divider = inflater.inflate(R.layout.divider, container, false);
                    mMainLinearLayout.addView(divider);
                }
            }
        }
    }
    private void setQuattrocentoFont() {
        qsRegular = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Regular.ttf");
        qsBold = Typeface.createFromAsset(
                getActivity().getAssets(), "fonts/QuattrocentoSans-Bold.ttf");
    }


    private void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }
    }

    // ------------- favorites button ---------------

    private void setFavButton() {

        final Button favButton = (Button) mRootView.findViewById(R.id.button_favorite);
        if (mBundle != null && mMovieId != null) {
            if (!isFavorite()) {
                favButton.setText(getActivity().getString(R.string.button_favorite));
            } else {
                favButton.setText(getActivity().getString(R.string.button_favorite_remove));
            }
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFavorite()) {
                        // put to favorities and database
                        FavPrefUtils.putFavorities(getActivity(), mMovieId);
                        favButton.setText(getActivity().getString(R.string.button_favorite_remove));
                        new AddMovieToFavDbTask().execute();
                    } else {
                        // remove from favorities and database
                        FavPrefUtils.removeFromFavorities(getActivity(), mMovieId);
                        favButton.setText(getActivity().getString(R.string.button_favorite));
                        new RemoveMovieFromFavDbTask().execute();
                    }
                }
            });
        }
    }

    // check if movie is already favorite
    private boolean isFavorite() {
        Set<String> favorites = FavPrefUtils.getFavorities(getActivity());
        if (mMovieId != null) {
            return favorites != null && favorites.contains(Integer.toString(mMovieId));
        }
        return false;
    }

    // ------------- menu and share intent ---------------

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // this method shares first trailer
    private void setShareIntent() {

        if (mTrailerList != null && mTrailerList.size() > 0) {
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

    // -------------- add/remove favorites to/from db --------------

    public class AddMovieToFavDbTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            FavDbUtils.insertFavMovieIntoDb(getActivity(), mBundle);
            return null;
        }
    }
    public class RemoveMovieFromFavDbTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            FavDbUtils.deleteFavMovieFromDb(getActivity(), mMovieId);
            return null;
        }
    }

}
