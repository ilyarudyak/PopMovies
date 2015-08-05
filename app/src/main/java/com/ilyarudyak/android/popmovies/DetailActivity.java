package com.ilyarudyak.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ilyarudyak.android.popmovies.data.Movie;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            DetailFragment df = new DetailFragment();
            df.setArguments(getIntent().getBundleExtra(Movie.BUNDLE));

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, df)
                    .commit();
        }
    }
}
