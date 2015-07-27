package com.ilyarudyak.android.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private View rootView;
    private LinearLayout mainLinearLayout;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mainLinearLayout = (LinearLayout) rootView.findViewById(R.id.mainLinearLayout);

        TextView originalTitle = (TextView) rootView.findViewById(R.id.textViewOriginalTitle);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.imageViewPoster);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
        TextView userRating = (TextView) rootView.findViewById(R.id.textViewUserRating);
        TextView plotSynopsis = (TextView) rootView.findViewById(R.id.textViewPlotSynopsis);

        // stage 2: add list of trailers
        List<Movie.Trailer> trailersList =  getActivity().getIntent()
                .getParcelableArrayListExtra(Movie.TRAILER_LIST);
        for(Movie.Trailer mt : trailersList) {
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
            mainLinearLayout.addView(reviewView);
            View divider = inflater.inflate(R.layout.divider, container, false);
            mainLinearLayout.addView(divider);
        }

        // detail activity called via intent.
        // inspect the intent for data.
        final Intent intent = getActivity().getIntent();
        originalTitle.setText(intent.getStringExtra(Movie.TMDB_ORIGINAl_TITLE));
        releaseDate.setText(intent.getStringExtra(Movie.TMDB_RELEASE_DATE).substring(0,4));
        final String MAX_RATING = "/10";
        userRating.setText(intent.getStringExtra(Movie.TMDB_USER_RATING) + MAX_RATING);
        plotSynopsis.setText(intent.getStringExtra(Movie.TMDB_PLOT_SYNOPSIS));

        String posterPathAbsolute = intent.getStringExtra(Movie.TMDB_POSTER_PATH_ABSOLUTE);
        Picasso.with(getActivity())
                .load(posterPathAbsolute)
                .placeholder(R.raw.place_holder)
                .resize(300, 450)
                .into(posterImageView);

        return rootView;
    }

    private void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }

    }
}
