package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.JsonParser;
import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.utils.NetworkUtils;

import java.util.List;

public class TestJsonParser extends AndroidTestCase {

    private static String LOG_TAG = TestJsonParser.class.getSimpleName();

    public void testJsonMovieParser() throws Throwable {

        // we read movies2.json into string
        String jsonStr = TestUtils.getJsonMoviesString();
        JsonParser mp = new JsonParser(jsonStr, NetworkUtils.MOVIE_FLAG);
        List<Movie> movies = mp.getMoviesList();

        Log.d(LOG_TAG, "## of movies: " + movies.size());

        Movie jw = movies.get(0);
        Integer expectedId = 135397;
        assertEquals(expectedId, jw.getId());
        assertEquals("Jurassic World", jw.getOriginalTitle());
        assertEquals("/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", jw.getPosterPathRelative());
        assertEquals("2015-06-12", jw.getReleaseDate());
        assertEquals(7.0, jw.getUserRating());
        String overview = "Twenty-two years after the events of Jurassic Park, Isla Nublar now " +
                "features a fully functioning dinosaur theme park, Jurassic World, as originally " +
                "envisioned by John Hammond.";
        assertEquals(overview, jw.getPlotSynopsis());

        assertEquals("2015", jw.getYear());
        String expectedAbsolutePath = "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        assertEquals(expectedAbsolutePath, jw.getPosterPathAbsolute());
    }

    public void testJsonMovieParserFavorites() throws Throwable {

        // read from movie2.json
        String jsonStr = TestUtils.getJWJsonMovieString();
        JsonParser mp = new JsonParser(jsonStr, NetworkUtils.FAVORITE_MOVIE_FLAG);
        List<Movie> movies = mp.getMoviesList();

        Movie jw = movies.get(0);
        Integer expectedId = 135397;
        assertEquals(expectedId, jw.getId());
        assertEquals("Jurassic World", jw.getOriginalTitle());
        assertEquals("/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", jw.getPosterPathRelative());
        assertEquals("2015-06-12", jw.getReleaseDate());
        assertEquals(7.0, jw.getUserRating());
        String overview = "Twenty-two years after the events of Jurassic Park, Isla Nublar now " +
                "features a fully functioning dinosaur theme park, Jurassic World, as originally " +
                "envisioned by John Hammond.";
        assertEquals(overview, jw.getPlotSynopsis());

        assertEquals("2015", jw.getYear());
        String expectedAbsolutePath = "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        assertEquals(expectedAbsolutePath, jw.getPosterPathAbsolute());
    }
}










