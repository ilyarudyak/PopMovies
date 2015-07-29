package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;


public class TestNetworkUtils extends AndroidTestCase {

    private static final String LOG_TAG = TestNetworkUtils.class.getSimpleName();
    // test building URLs
    public void testBuildMoviesAPIUrl() throws Throwable {

        String expectedUrlPopular = "http://api.themoviedb.org/3/discover/movie?" +
                "sort_by=popularity.desc&api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = NetworkUtils.buildMoviesAPIUrl(NetworkUtils.MOST_POPULAR);
        if (result != null) {
            assertEquals(expectedUrlPopular, result.toString());
        } else {
            fail("result is null");
        }
    }
    public void testBuildTrailerReviewAPIUrl() throws Throwable {

        String expectedUrlTrailer = "http://api.themoviedb.org/3/movie/135397/videos?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = NetworkUtils.buildTrailerReviewAPIUrl(135397, NetworkUtils.TRAILER_FLAG);
        if (result != null) {
            assertEquals(expectedUrlTrailer, result.toString());
        } else {
            fail("result is null");
        }

        String expectedUrlReview = "http://api.themoviedb.org/3/movie/135397/reviews?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        result = NetworkUtils.buildTrailerReviewAPIUrl(135397, NetworkUtils.REVIEW_FLAG);
        if (result != null) {
            assertEquals(expectedUrlReview, result.toString());
        } else {
            fail("result is null");
        }
    }

    // test network calls
    public void testGetReviewsFromNetwork() throws Throwable {

        Integer movieId0 = 135397; // Jurassic World
        Integer movieId1 = 87101; // Terminator Genisys

        // we comment this - users can add more reviews and tests will not work
//        List<String> list0 = NetworkUtils.getReviewsFromNetwork(movieId0);
//        if (list0 != null) {
//            assertEquals(2, list0.size());
//        }
//        List<String> list1 = NetworkUtils.getReviewsFromNetwork(movieId1);
//        if (list1 != null) {
//            assertEquals(0, list1.size());
//        }

        List<Movie> movies = NetworkUtils.getMoviesFromNetwork(NetworkUtils.HIGHEST_RATED);
        for (Movie m : movies) {
            if (NetworkUtils.getReviewsFromNetwork(m.getId()) == null) {
                Log.d(LOG_TAG, "here we got an exception" + m.getId());
            }
        }
    }
}






