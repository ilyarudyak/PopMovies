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

import com.ilyarudyak.android.popmovies.data.JsonParser;
import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.data.PicassoAdapter;
import com.ilyarudyak.android.popmovies.utils.Utils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        mImageAdapter = new PicassoAdapter(getActivity(), new ArrayList<Movie>());

        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = mImageAdapter.getItem(position);
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
                if (movie.getMovieReviews() != null) {
                    Log.i(LOG_TAG, movie.getOriginalTitle() + " " +
                            movie.getMovieReviews().toString());
                }
                startActivity(intent);
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
            new FetchMoviesTask().execute(Utils.MOST_POPULAR);
            return true;
        } else if (id == R.id.action_highest_rated) {
            new FetchMoviesTask().execute(Utils.HIGHEST_RATED);
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ------------------- async task -------------------

    /**
     * We subclass AsyncTask to get data from API call
     * in a separate thread. We make a network call
     * using HttpURLConnection and parse JSON with
     * JSONObject. We then update main thread using
     * adapter instance variable in onPostExecute().
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

//        private final Integer TRAILER_FLAG = 0;
//        private final Integer REVIEW_FLAG = 1;

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> list = getDataFromAPICall(params);
            addTrailersAndReviews(list); //TODO to a separate tasks
            return list;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mImageAdapter.clear();
                mImageAdapter.addAll(result);
            }
        }

        // ------------------- helper methods -------------------

        private List<Movie> getDataFromAPICall(String...params) {

            HttpURLConnection urlConnection = null;

            try {

                URL url = Utils.buildMoviesAPIUrl(params[0]);
                if (url != null) {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                }
                if (urlConnection != null) {
                    InputStream inputStream = urlConnection.getInputStream();

                    String moviesJsonStr = new BufferedReader(
                            new InputStreamReader(inputStream)).readLine();

                    return new JsonParser(moviesJsonStr, Utils.MOVIE_FLAG).getMoviesList();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        // stage 2 method to iterate over list,
        // make API calls to get trailer URL
        private void addTrailersAndReviews(List<Movie> movies) {

            Iterator<Movie> i = movies.iterator();
            while (i.hasNext()){
                Movie m = i.next();

                List<Movie.Trailer> trailers = getTrailersFromAPICall(m.getId());
                m.setMovieTrailers(trailers);

                List<String> reviews = getReviewsFromAPICall(m.getId());
                if (reviews != null) {
                    m.setMovieReviews(reviews);
                    Log.i(LOG_TAG, m.getOriginalTitle() + " " + m.getMovieReviews().toString());
                }

            }
        }

        private List<Movie.Trailer> getTrailersFromAPICall(Integer movieId) {
            String trailerJsonStr = getJsonStringFromAPICall(movieId, Utils.TRAILER_FLAG);
            try {
                return new JsonParser(trailerJsonStr, Utils.TRAILER_FLAG).getTrailersList();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        private List<String> getReviewsFromAPICall(Integer movieId) {
            String reviewJsonStr = getJsonStringFromAPICall(movieId, Utils.REVIEW_FLAG);
            if (reviewJsonStr != null) {
                try {
                    return new JsonParser(reviewJsonStr, Utils.REVIEW_FLAG).getReviewsList();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        private String getJsonStringFromAPICall(Integer movieId, Integer flag) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = null;
                if (flag.equals(Utils.TRAILER_FLAG)) {
                    url = Utils.buildTrailerReviewAPIUrl(movieId, Utils.TRAILER_FLAG);
                } else if (flag.equals(Utils.REVIEW_FLAG)) {
                    url = Utils.buildTrailerReviewAPIUrl(movieId, Utils.REVIEW_FLAG);
                }
                if (url != null) {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                }
                if (urlConnection != null) {
                    InputStream inputStream = urlConnection.getInputStream();

                    String jsonStr = new BufferedReader(
                            new InputStreamReader(inputStream)).readLine();

                    return jsonStr;
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}























