package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.data.Movie;

public class TestMovie extends AndroidTestCase {

    private Movie m;
    /*
        This gets run before every test.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m = new Movie(
                135397,
                "Jurassic World",
                "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
                "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
                "2015-07-17",
                7.2
        );
    }

    public void testBuildPosterPathAbsolute() throws Throwable {
        String expectedPath = "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        assertEquals(expectedPath, m.getPosterPathAbsolute());
    }

    public void testGetYear() throws Throwable {
        String expectedYear = "2015";
        assertEquals(expectedYear, m.getYear());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}















