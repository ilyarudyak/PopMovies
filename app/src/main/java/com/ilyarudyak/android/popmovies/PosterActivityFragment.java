package com.ilyarudyak.android.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.data.PicassoAdapter;
import com.ilyarudyak.android.popmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Upon launch, present the user with an grid arrangement
 * of movie posters. Allow your user to change sort order
 * via a setting. Allow the user to tap on a movie poster
 * and transition to a details screen with additional
 * information.
 */
public class PosterActivityFragment extends Fragment {

    private final String LOG_TAG = PosterActivityFragment.class.getSimpleName();

    // we have to make adapter global to update it
    // in onPostExecute() method of our fetch task
    private PicassoAdapter mImageAdapter;

    public PosterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        mImageAdapter = new PicassoAdapter(getActivity(), new ArrayList<Movie>());

        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = mImageAdapter.getItem(position);
                new FetchTrailersReviewsTask().execute(movie);

            }
        });

        return v;
    }

    private Intent buildDetailsIntent(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(Movie.TMDB_ORIGINAl_TITLE, movie.getOriginalTitle())
                .putExtra(Movie.TMDB_POSTER_PATH_ABSOLUTE, movie.getPosterPathAbsolute())
                .putExtra(Movie.TMDB_RELEASE_DATE, movie.getReleaseDate())
                .putExtra(Movie.TMDB_USER_RATING, Double.toString(movie.getUserRating()))
                .putExtra(Movie.TMDB_PLOT_SYNOPSIS, movie.getPlotSynopsis())
                .putParcelableArrayListExtra(Movie.TRAILER_LIST,
                        (ArrayList<? extends Parcelable>) movie.getMovieTrailers())
                .putStringArrayListExtra(Movie.REVIEW_LIST,
                        (ArrayList<String>) movie.getMovieReviews());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // now this fragment can handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // get preferences or use default value if they are not set
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_order = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_most_popular));
        Log.d(LOG_TAG, sort_order);
        // fetch movies using preferred sort order
        new FetchMoviesTask().execute(sort_order);

    }


    // ------------------- menu methods -------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_poster, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            new FetchMoviesTask().execute(NetworkUtils.MOST_POPULAR);
            return true;
        } else if (id == R.id.action_highest_rated) {
            new FetchMoviesTask().execute(NetworkUtils.HIGHEST_RATED);
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ------------------- async tasks -------------------

    /**
     * We subclass AsyncTask to get data from API call
     * in a separate thread. We make a network call
     * using HttpURLConnection and parse JSON with
     * JSONObject. We then update main thread using
     * adapter instance variable in onPostExecute().
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> list = NetworkUtils.getMoviesFromNetwork(params[0]);
//            addTrailersAndReviews(list); //TODO to a separate tasks
            return list;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mImageAdapter.clear();
                mImageAdapter.addAll(result);
            }
        }


    }

    /**
     * We fetch trailers and reviews only when a user
     * clicks on a poster image in PosterActivity. This makes
     * interface much more responsive.
     * We modify Movie object in place and use it to build intent.
     * Than we start activity in onPostExecute() with this intent.
     * */
    public class FetchTrailersReviewsTask extends AsyncTask<Movie, Void, Void> {

        private Movie m;

        @Override
        protected Void doInBackground(Movie... movies) {
            m = movies[0];

            // download list of trailers and set them on given movie
            List<Movie.Trailer> trailers = NetworkUtils.getTrailersFromNetwork(m.getId());
            m.setMovieTrailers(trailers);
            // download list of reviews and set them on given movie
            List<String> reviews = NetworkUtils.getReviewsFromNetwork(m.getId());
            m.setMovieReviews(reviews);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(buildDetailsIntent(m));
        }
    }
}


