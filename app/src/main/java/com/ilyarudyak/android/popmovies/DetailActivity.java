package com.ilyarudyak.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ilyarudyak.android.popmovies.data.Movie;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            DetailFragment df = new DetailFragment();
            df.setArguments(getIntent().getBundleExtra(Movie.MOVIE_BUNDLE));

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, df)
                    .commit();
        }
    }
}
