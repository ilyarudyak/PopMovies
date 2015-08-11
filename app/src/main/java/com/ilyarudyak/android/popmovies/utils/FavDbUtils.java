package com.ilyarudyak.android.popmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.ilyarudyak.android.popmovies.data.Movie;
import com.ilyarudyak.android.popmovies.db.MovieContract;

/**
 * Created by ilyarudyak on 8/11/15.
 */
public class FavDbUtils {

    public static Bundle getBundleFromUri(Context context, Uri movieUri) {

        Bundle b;
        Integer tmdbId;

        // query movie table
        Cursor cMovie = context.getContentResolver().query(movieUri,
                null, null, null, null);
        b = Movie.buildDetailBundle(cMovie);
        tmdbId = cMovie.getInt(cMovie.getColumnIndex(MovieContract.MovieTable.DB_TMDB_MOVIE_ID));
        cMovie.close();

        // query trailer data and add to bundle
        String selectionTrailer = MovieContract.TrailerTable.DB_TMDB_MOVIE_ID + "=" + tmdbId;
        Cursor cTrailer = context.getContentResolver().query(MovieContract.TrailerTable.CONTENT_URI,
                null, selectionTrailer, null, null);
        Movie.addTrailersToBundle(cTrailer, b);
        cTrailer.close();

        // query review data and add to bundle
        String selectionReview = MovieContract.ReviewTable.DB_TMDB_MOVIE_ID + "=" + tmdbId;
        Cursor cReview = context.getContentResolver().query(MovieContract.ReviewTable.CONTENT_URI,
                null, selectionReview, null, null);
        Movie.addReviewsToBundle(cReview, b);
        cReview.close();

        return b;
    }

    public static void insertFavMovieIntoDb(Context context, Bundle b) {

        ContentValues cv = Movie.buildMovieContentValues(b);
        context.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI,cv);

        ContentValues[] cvTrailers = Movie.buildTrailersContentValues(b);
        context.getContentResolver().bulkInsert(MovieContract.TrailerTable.CONTENT_URI, cvTrailers);

        ContentValues[] cvReviews = Movie.buildReviewsContentValues(b);
        context.getContentResolver().bulkInsert(MovieContract.ReviewTable.CONTENT_URI, cvReviews);
    }

    public static void deleteFavMovieFromDb(Context context, Integer tmdbId) {

        String selectionMovie = MovieContract.MovieTable.DB_TMDB_MOVIE_ID + "=" + tmdbId;
        context.getContentResolver().delete(MovieContract.MovieTable.CONTENT_URI,
                selectionMovie, null);

        String selectionTrailer = MovieContract.TrailerTable.DB_TMDB_MOVIE_ID + "=" + tmdbId;
        context.getContentResolver().delete(MovieContract.TrailerTable.CONTENT_URI,
                selectionTrailer, null);

        String selectionReview = MovieContract.ReviewTable.DB_TMDB_MOVIE_ID + "=" + tmdbId;
        context.getContentResolver().delete(MovieContract.ReviewTable.CONTENT_URI,
                selectionReview, null);

    }
}
