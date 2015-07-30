package com.ilyarudyak.android.popmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.JsonParser;
import com.ilyarudyak.android.popmovies.data.Movie;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    // sort order can be by most popular or by highest-rated
    public static final String MOST_POPULAR = "popularity.desc";
    public static final String HIGHEST_RATED = "vote_average.desc";

    public static final Integer MOVIE_FLAG =            0;
    public static final Integer TRAILER_FLAG =          1;
    public static final Integer REVIEW_FLAG =           2;
    public static final Integer FAVORITE_MOVIE_FLAG =   3;

    // -------------------- build urls --------------------

    public static URL buildMoviesAPIUrl(String sortParameter)
            throws MalformedURLException {

        final String API_BASE_URL =
                "http://api.themoviedb.org/3/discover/movie";
        final String SORT_PARAM = "sort_by";
        final String API_KEY = "api_key";
        final String KEY = "99ee31c251ccebfbe8786aa49d9c6fe8";

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortParameter)
                .appendQueryParameter(API_KEY, KEY)
                .build();

        return new URL(builtUri.toString());
    }
    public static URL buildFavoriteMoviesAPIUrl(Integer movieId)
            throws MalformedURLException {
        final String API_BASE_URL =
                "http://api.themoviedb.org/3/movie";
        final String API_KEY = "api_key";
        final String KEY = "99ee31c251ccebfbe8786aa49d9c6fe8";

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(API_KEY, KEY)
                .build();

        return new URL(builtUri.toString());
    }
    public static URL buildTrailerReviewAPIUrl(Integer movieId, Integer flag)
            throws MalformedURLException {

        final String API_BASE_URL =
                "http://api.themoviedb.org/3/movie";
        final String VIDEOS = "videos";
        final String REVIEWS = "reviews";
        final String API_KEY = "api_key";
        final String KEY = "99ee31c251ccebfbe8786aa49d9c6fe8";

        Uri builtUri;
        if (flag.equals(TRAILER_FLAG)) {
            builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .appendPath(VIDEOS)
                    .appendQueryParameter(API_KEY, KEY)
                    .build();
        } else if (flag.equals(REVIEW_FLAG)) {
            builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .appendPath(REVIEWS)
                    .appendQueryParameter(API_KEY, KEY)
                    .build();
        } else {
            throw new MalformedURLException("wrong flag: " + flag);
        }

        return new URL(builtUri.toString());

    }

    // -------------------- network calls --------------------

    /** given a URL, establishes an HttpUrlConnection and retrieves
     *  the web page content as a InputStream, which it returns as
     *  a string. code from here:
     *  http://developer.android.com/training/basics/network-ops/connecting.html#download */
    public static String downloadJsonString(URL url) throws IOException {
        InputStream is = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // starts the query
            conn.connect();
//            int response = conn.getResponseCode();
//            Log.d(LOG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // convert the InputStream into a string
            return readIt(is);

            // makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    private static String readIt(InputStream is) throws IOException {
        return new BufferedReader(new InputStreamReader(is)).readLine();
    }

    public static List<Movie> getMoviesFromNetwork(String sortParameter) {

        try {
            URL url = buildMoviesAPIUrl(sortParameter);
            String moviewJsonString = downloadJsonString(url);
            return new JsonParser(moviewJsonString, NetworkUtils.MOVIE_FLAG).getMoviesList();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        }
    }
    public static List<Movie> getFavoriteMoviesFromNetwork(Context context) {

        Set<String> movies = FavoritesUtils.getFavorities(context);
        List<Movie> moviesList = new ArrayList<>();
        for (String movieIdString : movies) {
            try {
                URL url = buildFavoriteMoviesAPIUrl(Integer.parseInt(movieIdString));
                String favoriteMoviewJsonString = downloadJsonString(url);
                moviesList.add(new JsonParser(favoriteMoviewJsonString,
                        NetworkUtils.FAVORITE_MOVIE_FLAG).getMoviesList().get(0));
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            }
        }
        return moviesList;
    }
    public static List<Movie> getFavoriteMoviesFromNetworkTest(Set<String> movies) {

        List<Movie> moviesList = new ArrayList<>();
        for (String movieIdString : movies) {
            try {
                URL url = buildFavoriteMoviesAPIUrl(Integer.parseInt(movieIdString));
                String favoriteMoviewJsonString = downloadJsonString(url);
                moviesList.add(new JsonParser(favoriteMoviewJsonString,
                        NetworkUtils.FAVORITE_MOVIE_FLAG).getMoviesList().get(0));
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "exception", e);
                return null;
            }
        }
        return moviesList;
    }
    public static List<Movie.Trailer> getTrailersFromNetwork (Integer movieId) {

        try {
            URL url = buildTrailerReviewAPIUrl(movieId, NetworkUtils.TRAILER_FLAG);
            String trailerJsonString = downloadJsonString(url);
            return new JsonParser(trailerJsonString, NetworkUtils.TRAILER_FLAG).getTrailersList();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        }
    }
    public static List<String> getReviewsFromNetwork(Integer movieId) {
        try {
            URL url = buildTrailerReviewAPIUrl(movieId, NetworkUtils.REVIEW_FLAG);
            String reviewJsonString = downloadJsonString(url);
            return new JsonParser(reviewJsonString, NetworkUtils.REVIEW_FLAG).getReviewsList();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "exception", e);
            return null;
        }
    }


}














