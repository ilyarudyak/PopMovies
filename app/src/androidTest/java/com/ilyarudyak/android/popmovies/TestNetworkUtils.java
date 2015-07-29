package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.utils.NetworkUtils;

import java.net.URL;


public class TestNetworkUtils extends AndroidTestCase {

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
}