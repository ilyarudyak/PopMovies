package com.ilyarudyak.android.popmovies;

import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class TestMovie extends AndroidTestCase {

    private Movie m;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m = new Movie(
                135397,
                "Jurassic World",
                "Twenty-two years after the events of Jurassic Park...",
                "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
                "2015-07-17",
                7.2
        );

        List<Movie.Trailer> trailers = new ArrayList<>();
        trailers.add(new Movie.Trailer("lP-sUUUfamw", "Official Trailer 3"));
        trailers.add(new Movie.Trailer("bvu-zlR5A8Q", "Teaser"));
        m.setMovieTrailers(trailers);

        List<String> reviews = new ArrayList<>();
        reviews.add("This is the first review");
        reviews.add("And this is the second one");
        m.setMovieReviews(reviews);
    }

    public void testBuildPosterPathAbsolute() throws Throwable {
        String expectedPath = "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        assertEquals(expectedPath, m.getPosterPathAbsolute());
    }
    public void testGetYear() throws Throwable {
        String expectedYear = "2015";
        assertEquals(expectedYear, m.getYear());
    }
    public void testTrailerGetters() throws Throwable {

        assertEquals(2, m.getMovieTrailers().size());

        String expectedName = "Official Trailer 3";
        assertEquals(expectedName, m.getMovieTrailers().get(0).getTrailerName());
    }
    public void testTrailerBuildPathAbsolute() throws Throwable {
        String expectedUrl = "https://www.youtube.com/watch?v=bvu-zlR5A8Q";
        assertEquals(expectedUrl, m.getMovieTrailers().get(1).getTrailerPathAbsolute());
    }
    public void testReviewGetters() throws Throwable {
        String expectedReview = "This is the first review";
        assertEquals(expectedReview, m.getMovieReviews().get(0));
    }


}















