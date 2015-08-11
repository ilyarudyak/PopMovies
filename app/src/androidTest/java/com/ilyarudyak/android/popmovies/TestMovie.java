package com.ilyarudyak.android.popmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.db.MovieContract;

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

    public void testBuildDetailBundleFromMovie() throws Throwable {

        Bundle b = Movie.buildDetailBundle(m);
        assertEquals(m.getId(), Integer.valueOf(b.getInt(Movie.TMDB_ID)));
        assertEquals(m.getOriginalTitle(), b.getString(Movie.TMDB_ORIGINAl_TITLE));
        assertEquals(m.getPosterPathAbsolute(), b.getString(Movie.TMDB_POSTER_PATH_ABSOLUTE));
        assertEquals(m.getYear(), b.getString(Movie.RELEASE_YEAR));
        assertEquals(Double.toString(m.getUserRating()), b.getString(Movie.TMDB_USER_RATING));
        assertEquals(m.getPlotSynopsis(), b.getString(Movie.TMDB_PLOT_SYNOPSIS));

        List<Movie.Trailer> trailers = b.getParcelableArrayList(Movie.TRAILER_LIST);
        assertEquals(2, trailers.size());
        assertEquals(m.getMovieTrailers().get(0).getTrailerName(), trailers.get(0).getTrailerName());
        assertEquals(m.getMovieTrailers().get(0).getTrailerPathAbsolute(), trailers.get(0).getTrailerPathAbsolute());

        assertEquals(2, b.getStringArrayList(Movie.REVIEW_LIST).size());
        assertEquals(m.getMovieReviews().get(0), b.getStringArrayList(Movie.REVIEW_LIST).get(0));
    }
    public void testbuildDetailBundleFromCursor() throws Throwable {

        String plot = "Twenty-two years after the events of Jurassic Park, " +
                "Isla Nublar now features a fully functioning dinosaur theme park, " +
                "Jurassic World, as originally envisioned by John Hammond.";

        // clear database
        mContext.getContentResolver().delete(
                MovieContract.MovieTable.CONTENT_URI,
                null,
                null
        );

        // insert JW movie
        ContentValues cv = TestUtils.createJWMovieContentValues();
        mContext.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI, cv);

        // query database
        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        c.moveToFirst();

        // create and check bundle
        Bundle b = Movie.buildDetailBundle(c);
        assertEquals(135397, b.getInt(Movie.TMDB_ID));
        assertEquals("Jurassic World", b.getString(Movie.TMDB_ORIGINAl_TITLE));
        assertEquals("http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
                b.getString(Movie.TMDB_POSTER_PATH_ABSOLUTE));
        assertEquals("2015", b.getString(Movie.RELEASE_YEAR));
        assertEquals(7.0, Double.parseDouble(b.getString(Movie.TMDB_USER_RATING)));
        assertEquals(plot, b.getString(Movie.TMDB_PLOT_SYNOPSIS));
        c.close();

    }


}















