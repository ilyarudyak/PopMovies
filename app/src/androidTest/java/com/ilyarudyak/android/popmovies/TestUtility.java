package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.utils.Utils;

import java.net.URL;


public class TestUtility extends AndroidTestCase {

    // test building URLs
    public void testBuildMoviesAPIUrl() throws Throwable {

        String expectedUrlPopular = "http://api.themoviedb.org/3/discover/movie?" +
                "sort_by=popularity.desc&api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = Utils.buildMoviesAPIUrl(Utils.MOST_POPULAR);
        if (result != null) {
            assertEquals(expectedUrlPopular, result.toString());
        } else {
            fail("result is null");
        }
    }
    public void testBuildTrailerReviewAPIUrl() throws Throwable {

        String expectedUrlTrailer = "http://api.themoviedb.org/3/movie/135397/videos?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = Utils.buildTrailerReviewAPIUrl(135397, Utils.TRAILER_FLAG);
        if (result != null) {
            assertEquals(expectedUrlTrailer, result.toString());
        } else {
            fail("result is null");
        }

        String expectedUrlReview = "http://api.themoviedb.org/3/movie/135397/reviews?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        result = Utils.buildTrailerReviewAPIUrl(135397, Utils.REVIEW_FLAG);
        if (result != null) {
            assertEquals(expectedUrlReview, result.toString());
        } else {
            fail("result is null");
        }
    }
}