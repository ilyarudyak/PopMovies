package com.ilyarudyak.android.popmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.data.PicassoAdapter;
import com.ilyarudyak.android.popmovies.favorities.FavPosterActivity;
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
public class PosterFragment extends Fragment {

    private final String LOG_TAG = PosterFragment.class.getSimpleName();

    // we have to make adapter global to update it
    // in onPostExecute() method of our fetch task
    private PicassoAdapter mPicassoAdapter;

    private Callback mCallback;

    public PosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_poster, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        mPicassoAdapter = new PicassoAdapter(getActivity(), new ArrayList<Movie>());

        // set empty view in case movies list is empty, no internet etc.
        View emptyView = v.findViewById(R.id.empty_movies_list);
        gridView.setEmptyView(emptyView);

        gridView.setAdapter(mPicassoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = mPicassoAdapter.getItem(position);
                Log.d(LOG_TAG, "we are in onclick");
                new FetchTrailersReviewsTask().execute(movie);

            }
        });



        return v;
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
        String sortOrder = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_most_popular));
        // fetch movies using preferred sort order
        new FetchMoviesTask().execute(sortOrder);

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
        } else if (id == R.id.action_favorities_api) {
            new FetchFavoritieMoviesTask().execute();
            return true;
        } else if (id == R.id.action_favorities_db) {
            startActivity(new Intent(getActivity(), FavPosterActivity.class));
            return true;
        }else if (id == R.id.action_settings) {
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

        @Override
        protected List<Movie> doInBackground(String... params) {
            return NetworkUtils.getMoviesFromNetwork(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mPicassoAdapter.clear();
                mPicassoAdapter.addAll(result);
            }
            updateEmptyView();
        }
    }

    /**
     * We fetch information about favorite movies based on set
     * of favorites from shared preferences. We fetch it for each movie
     * individually using network call that is different from our main call.
     * We than update our Picasso adapter in a usual way.
     * */
    public class FetchFavoritieMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return NetworkUtils.getFavoriteMoviesFromNetwork(getActivity());
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mPicassoAdapter.clear();
                mPicassoAdapter.addAll(result);
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

        private Movie mMovie;

        @Override
        protected Void doInBackground(Movie... movies) {
            mMovie = movies[0];

            // download list of trailers and set them on given movie
            List<Movie.Trailer> trailers = NetworkUtils.getTrailersFromNetwork(mMovie.getId());
            if (trailers != null) {
                mMovie.setMovieTrailers(trailers);
            }
            // download list of reviews and set them on given movie
            List<String> reviews = NetworkUtils.getReviewsFromNetwork(mMovie.getId());
            if (reviews != null) {
                mMovie.setMovieReviews(reviews);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(LOG_TAG, "we are inside async task");
            mCallback.onPosterSelected(mMovie);
        }
    }

    // -------------- callback interface ----------------

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onPosterSelected(Movie movie);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    // -------- empty view for no connection etc. ---------

    /*
    Updates the empty list view with contextually relevant information that the user can
    use to determine why they aren't seeing weather.
 */
    private void updateEmptyView() {
        if ( mPicassoAdapter.getCount() == 0 ) {
            TextView tv = null;
            if (getView() != null) {
                tv = (TextView) getView().findViewById(R.id.empty_movies_list);
            }
            if (tv != null) {
                int message = R.string.empty_movies_list;
                if (!NetworkUtils.isNetworkAvailable(getActivity()) ) {
                    message = R.string.empty_movies_list_no_network;
                }
                tv.setText(message);
            }
        }
    }



}


