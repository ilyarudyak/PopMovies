package com.ilyarudyak.android.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView originalTitle = (TextView) rootView.findViewById(R.id.textViewOriginalTitle);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.imageViewPoster);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
        TextView userRating = (TextView) rootView.findViewById(R.id.textViewUserRating);
        TextView plotSynopsis = (TextView) rootView.findViewById(R.id.textViewPlotSynopsis);

        // detail activity called via intent.
        // inspect the intent for data.
        Intent intent = getActivity().getIntent();
        originalTitle.setText(intent.getStringExtra(Movie.TMDB_ORIGINAl_TITLE));
        releaseDate.setText(intent.getStringExtra(Movie.TMDB_RELEASE_DATE));
        userRating.setText(intent.getStringExtra(Movie.TMDB_USER_RATING));
        plotSynopsis.setText(intent.getStringExtra(Movie.TMDB_PLOT_SYNOPSIS));

        String posterPathAbsolute = intent.getStringExtra(Movie.TMDB_POSTER_PATH_ABSOLUTE);
        Picasso.with(getActivity())
                .load(posterPathAbsolute)
                .placeholder(R.raw.place_holder)
                .resize(550, 775)
                .into(posterImageView);

        return rootView;
    }
}
