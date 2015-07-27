package com.ilyarudyak.android.popmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;


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

//        addTrailers();
        View trailerView = inflater.inflate(R.layout.trailer, container, false);
        View divider = inflater.inflate(R.layout.divider, container, false);
        mainLinearLayout.addView(trailerView);
        mainLinearLayout.addView(divider);

        ArrayList<Movie.Trailer> trailersList =  getActivity().getIntent()
                .getParcelableArrayListExtra(Movie.TRAILER_LIST);
        String url = trailersList.get(0).getTrailerPathAbsolute();
        Log.i(LOG_TAG, trailersList.toString());


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

    // stage 2 add trailers to details view
    // we create and add them programmatically -
    // each movie can have different number of trailers
    private void addTrailers() {

        LinearLayout trailerLinearLayout = new LinearLayout(getActivity());
        mainLinearLayout.addView(trailerLinearLayout);

        ImageButton trailerButton = new ImageButton(getActivity());
        Drawable iconArrow = getActivity().getResources()
                .getDrawable(R.drawable.ic_play_arrow_black_36dp);
        trailerButton.setBackground(iconArrow);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(48, 16, 16, 16);
        trailerButton.setLayoutParams(params);
        trailerLinearLayout.addView(trailerButton);

        TextView trailerTextView = new TextView(getActivity());
        trailerTextView.setText("Trailer");
        trailerTextView.setTextSize(16);
        params.setMargins(48, 24, 16, 16);
        trailerTextView.setLayoutParams(params);
        trailerLinearLayout.addView(trailerTextView);

        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Movie.Trailer> trailersList =  getActivity().getIntent()
                        .getParcelableArrayListExtra(Movie.TRAILER_LIST);
                String url = trailersList.get(0).getTrailerPathAbsolute();
                Log.i(LOG_TAG, url);
                openTrailer(url);
            }
        });

    }

    private void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }

    }
}
