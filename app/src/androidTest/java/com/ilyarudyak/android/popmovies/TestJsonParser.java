package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ilyarudyak.android.popmovies.data.JsonParser;
import com.ilyarudyak.android.popmovies.data.Movie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TestJsonParser extends AndroidTestCase {

    private static String LOG_TAG = TestJsonParser.class.getSimpleName();

    public void testJsonMovieParser() throws Throwable {

        String file = "assets/moviesJson.json";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
        String jsonStr = new BufferedReader(
                new InputStreamReader(inputStream)).readLine();

        JsonParser mp = new JsonParser(jsonStr, JsonParser.MOVIE);
        List<Movie> movies = mp.getMoviesList();

        Log.d(LOG_TAG, "## of movies: " + movies.size());

        Movie jw = movies.get(0);
        assertEquals(135397, jw.getId());
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










