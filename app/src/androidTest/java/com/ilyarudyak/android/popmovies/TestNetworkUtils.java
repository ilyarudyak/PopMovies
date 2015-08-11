package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.utils.ApiKey;
import com.ilyarudyak.android.popmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TestNetworkUtils extends AndroidTestCase {

    private static final String LOG_TAG = TestNetworkUtils.class.getSimpleName();
    // test building URLs
    public void testBuildMoviesAPIUrl() throws Throwable {

        String expectedUrlPopular = "http://api.themoviedb.org/3/discover/movie?" +
                "sort_by=popularity.desc&api_key=" + ApiKey.KEY;
        URL result = NetworkUtils.buildMoviesAPIUrl(NetworkUtils.MOST_POPULAR);
        if (result != null) {
            assertEquals(expectedUrlPopular, result.toString());
        } else {
            fail("result is null");
        }
    }
    public void testBuildFavoriteMoviesAPIUrl() throws Throwable {

        String expectedUrl = "http://api.themoviedb.org/3/movie/135397?" +
                "api_key=" + ApiKey.KEY;
        URL result = NetworkUtils.buildFavoriteMoviesAPIUrl(135397);
        if (result != null) {
            assertEquals(expectedUrl, result.toString());
        } else {
            fail("result is null");
        }
    }
    public void testBuildTrailerReviewAPIUrl() throws Throwable {

        String expectedUrlTrailer = "http://api.themoviedb.org/3/movie/135397/videos?" +
                "api_key=" + ApiKey.KEY;
        URL result = NetworkUtils.buildTrailerReviewAPIUrl(135397, NetworkUtils.TRAILER_FLAG);
        if (result != null) {
            assertEquals(expectedUrlTrailer, result.toString());
        } else {
            fail("result is null");
        }

        String expectedUrlReview = "http://api.themoviedb.org/3/movie/135397/reviews?" +
                "api_key=" + ApiKey.KEY;
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
    public void testGetFavoriteMoviesFromNetwork() throws Throwable {

        Set<String> favorites = new HashSet<>(Arrays.asList("135397", "211672", "87101"));
        String jw = "Jurassic World";
        String mi = "Minions";
        String tg = "Terminator Genisys";

        List<Movie> favoritesList = NetworkUtils.getFavoriteMoviesFromNetworkTest(favorites);
        for (Movie m : favoritesList) {
            if (m.getId().equals(135397)) {
                assertEquals(jw, m.getOriginalTitle());
            } else if (m.getId().equals(211672)) {
                assertEquals(mi, m.getOriginalTitle());
            } else if (m.getId().equals(87101)) {
                assertEquals(tg, m.getOriginalTitle());
            }
        }
    }
}






