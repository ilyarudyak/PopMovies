package com.ilyarudyak.android.popmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static String[] mThumbIds = {"http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", "http://image.tmdb.org/t/p/w185/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", "http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg", "http://image.tmdb.org/t/p/w185/qey0tdcOp9kCDdEZuJ87yE3crSe.jpg", "http://image.tmdb.org/t/p/w185/aMEsvTUklw0uZ3gk3Q6lAj6302a.jpg", "http://image.tmdb.org/t/p/w185/saF3HtAduvrP9ytXDxSnQJP3oqx.jpg", "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "http://image.tmdb.org/t/p/w185/A7HtCxFe7Ms8H7e7o2zawppbuDT.jpg", "http://image.tmdb.org/t/p/w185/3zQvuSAUdC3mrx9vnSEpkFX0968.jpg", "http://image.tmdb.org/t/p/w185/rDycdoAXtBb7hoWlBpZqbwk2F44.jpg", "http://image.tmdb.org/t/p/w185/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg", "http://image.tmdb.org/t/p/w185/2i0JH5WqYFqki7WDhUW56Sg0obh.jpg", "http://image.tmdb.org/t/p/w185/xxX0v4vyfVc3Z8DEsbLJODnMOfQ.jpg", "http://image.tmdb.org/t/p/w185/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg", "http://image.tmdb.org/t/p/w185/oAISjx6DvR2yUn9dxj00vP8OcJJ.jpg", "http://image.tmdb.org/t/p/w185/qrFwjJ5nvFnpBCmXLI4YoeHJNBH.jpg", "http://image.tmdb.org/t/p/w185/69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg", "http://image.tmdb.org/t/p/w185/9gm3lL8JMTTmc3W4BmNMCuRLdL8.jpg", "http://image.tmdb.org/t/p/w185/5vHssUeVe25bMrof1HyaPyWgaP.jpg", "http://image.tmdb.org/t/p/w185/gCBw0AQDhlo0bNetkjsSRWzrxpW.jpg"};

    private GridView gridView;
    private ImageAdapter mImageAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) v.findViewById(R.id.gridView);
        mImageAdapter = new ImageAdapter(getActivity(),
                new ArrayList<String>());
        gridView.setAdapter(mImageAdapter);


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask().execute("popularity.desc");
    }

    // ------------------- helper classes -------------------

    // our custom adapter
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mMoviesList;

        public ImageAdapter(Context context, List<String> moviesList) {
            mContext = context;
            mMoviesList = moviesList;
        }

        public void clear() {
            mMoviesList = new ArrayList<>();
        }

        public void addAll(List<String> list) {
            mMoviesList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mMoviesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ImageView imageView;
            // check to see if we have a view
            if (convertView == null) {
                // no view - so create a new one
                imageView = new ImageView(mContext);
            } else {
                // use the recycled view object
                imageView = (ImageView) convertView;
            }

            Picasso.with(mContext)
                    .load(mMoviesList.get(position))
                    .placeholder(R.raw.place_holder)
                    .error(R.raw.big_problem)
                    .noFade().resize(150, 150)
                    .centerCrop()
                    .into(imageView);
            return imageView;
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            String[] result = getDataFromAPICall(params);
            Log.i(LOG_TAG, Arrays.toString(result));
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mImageAdapter.clear();
                mImageAdapter.addAll(Arrays.asList(result));
            }
        }

        // ------------------- helper methods -------------------

        /**
         * Take the String representing the list of movies in JSON Format and
         * pull out the data we need to construct the wireframes.
         */
        private String[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            // names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";

            JSONObject forecastJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray(TMDB_RESULTS);

            String[] moviesPostersUrls = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++) {

                // get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);

                moviesPostersUrls[i] = buildPosterUrlString(
                        movie.getString(TMDB_POSTER_PATH)
                );

            }

            return moviesPostersUrls;
        }


        private String buildPosterUrlString(String relativePath) {

            final String POSTER_BASE_URL =
                    "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185";

            Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                    .appendPath(POSTER_SIZE)
                    .appendPath(relativePath.replace("/", ""))
                    .build();

            return builtUri.toString();
        }

        private URL buildAPIUrl(String parameter) {

            // construct the URL for the TMDB query
            // we use two parameters: popularity and vote_average
            final String API_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY = "api_key";
            final String KEY = "99ee31c251ccebfbe8786aa49d9c6fe8";

            Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, parameter)
                    .appendQueryParameter(API_KEY, KEY)
                    .build();
            try {
                return new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String[] getDataFromAPICall(String...params) {

            HttpURLConnection urlConnection = null;

            try {

                URL url = buildAPIUrl(params[0]);
                if (url != null) {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                }
                if (urlConnection != null) {
                    InputStream inputStream = urlConnection.getInputStream();

                    String moviesJsonStr = new BufferedReader(
                            new InputStreamReader(inputStream)).readLine();

                    return getMoviesDataFromJson(moviesJsonStr);
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
    }
}























