package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.utility.Utility;

import java.net.URL;


public class TestUtility extends AndroidTestCase {

    // test building URLs
    public void testBuildMoviesAPIUrl() throws Throwable {

        String expectedUrlPopular = "http://api.themoviedb.org/3/discover/movie?" +
                "sort_by=popularity.desc&api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = Utility.buildMoviesAPIUrl(Utility.MOST_POPULAR);
        if (result != null) {
            assertEquals(expectedUrlPopular, result.toString());
        } else {
            fail("result is null");
        }
    }
    public void testBuildTrailerReviewAPIUrl() throws Throwable {

        String expectedUrlTrailer = "http://api.themoviedb.org/3/movie/135397/videos?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        URL result = Utility.buildTrailerReviewAPIUrl(135397, Utility.TRAILER_FLAG);
        if (result != null) {
            assertEquals(expectedUrlTrailer, result.toString());
        } else {
            fail("result is null");
        }

        String expectedUrlReview = "http://api.themoviedb.org/3/movie/135397/reviews?" +
                "api_key=99ee31c251ccebfbe8786aa49d9c6fe8";
        result = Utility.buildTrailerReviewAPIUrl(135397, Utility.REVIEW_FLAG);
        if (result != null) {
            assertEquals(expectedUrlReview, result.toString());
        } else {
            fail("result is null");
        }
    }
}