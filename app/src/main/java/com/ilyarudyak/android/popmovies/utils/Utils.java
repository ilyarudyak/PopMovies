package com.ilyarudyak.android.popmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    // sort order can be by most popular or by highest-rated
    public static final String MOST_POPULAR = "popularity.desc";
    public static final String HIGHEST_RATED = "vote_average.desc";

    public static final Integer MOVIE_FLAG =   0;
    public static final Integer TRAILER_FLAG = 1;
    public static final Integer REVIEW_FLAG =  2;

    // -------------------- build urls --------------------

    public static URL buildMoviesAPIUrl(String sortParameter) {

        final String API_BASE_URL =
                "http://api.themoviedb.org/3/discover/movie";
        final String SORT_PARAM = "sort_by";
        final String API_KEY = "api_key";
        final String KEY = "99ee31c251ccebfbe8786aa49d9c6fe8";

        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortParameter)
                .appendQueryParameter(API_KEY, KEY)
                .build();
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static URL buildTrailerReviewAPIUrl(Integer movieId, Integer flag) {

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
            Log.d(LOG_TAG, "wrong flag: " + flag);
            return null;
        }

        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
