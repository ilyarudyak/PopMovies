package com.ilyarudyak.android.popmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.db.MovieContract;

/**
 * Created by ilyarudyak on 8/11/15.
 */
public class DbUtils {

    public static Bundle getBundleFromUri(Context context, Uri movieUri) {

        Bundle b;
        Integer tmdbId;

        // query movie table
        Cursor cMovie = context.getContentResolver().query(movieUri,
                null, null, null, null);
        b = Movie.buildDetailBundle(cMovie);
        tmdbId = cMovie.getInt(cMovie.getColumnIndex(MovieContract.MovieTable.DB_TMDB_ID));
        cMovie.close();

        // query trailer data and add to bundle
        String selectionTrailer = MovieContract.TrailerTable.DB_MOVIE_TMDB_ID + "=" + tmdbId;
        Cursor cTrailer = context.getContentResolver().query(MovieContract.TrailerTable.CONTENT_URI,
                null, selectionTrailer, null, null);
        Movie.addTrailersToBundle(cTrailer, b);
        cTrailer.close();

        // query review data and add to bundle
        String selectionReview = MovieContract.ReviewTable.DB_MOVIE_TMDB_ID + "=" + tmdbId;
        Cursor cReview = context.getContentResolver().query(MovieContract.ReviewTable.CONTENT_URI,
                null, selectionReview, null, null);
        Movie.addReviewsToBundle(cReview, b);
        cReview.close();

        return b;
    }
}
