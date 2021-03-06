package com.ilyarudyak.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.Movie;


public class MainActivity extends AppCompatActivity
    implements PosterFragment.Callback {

    private final String TAG = MainActivity.class.getSimpleName();

    // true if we use tablet layout
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.poster_container, new PosterFragment(), null)
                    .commit();
        }
        // we provide a tablet layout only for a landscape mode
//        if(getResources().getBoolean(R.bool.landscape_only)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }

        // dynamically add detail fragment on a tablet
        // this fragment will be empty until we click
        // on a poster - then we replace it in callback
        if (findViewById(R.id.detail_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment(), null)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    // -------------- callback interface ----------------

    @Override
    public void onPosterSelected(Movie movie) {

        Log.d(TAG, "we are inside callback method");
        Log.d(TAG, "mTwoPane=" + mTwoPane);
        if (mTwoPane) {
            // create detail fragment with bundle
            DetailFragment df = new DetailFragment();
            Bundle args = Movie.buildDetailBundle(movie);
            Log.d(TAG, "args created " + args);
            df.setArguments(args);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, df)
                    .addToBackStack(null)
                    .commit();
        } else {
            // send intent to detail activity
            startActivity(Movie.buildDetailIntent(this, movie));
        }
    }
}













