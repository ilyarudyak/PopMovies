package com.ilyarudyak.android.popmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


/**
 * Created by ilyarudyak on 8/6/15.
 */
public class MovieProvider extends ContentProvider {

    public static final String TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;
    private static final int TRAILERS = 200;
    private static final int TRAILERS_ID = 201;
    private static final int REVIEWS = 300;
    private static final int REVIEWS_ID = 301;

    private MovieDbHelper mMovieDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILERS);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEWS);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIES_ID);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*", TRAILERS_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", REVIEWS_ID);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        Cursor c;
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                // we don't use SQLiteQueryBuilder and
                // call db itself
                c = db.query(MovieContract.MovieTable.DB_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MOVIES_ID:
                String movieId = MovieContract.MovieTable.getMovieId(uri);
                c = db.query(MovieContract.MovieTable.DB_TABLE_NAME, projection,
                        // WHERE _ID=id
                        BaseColumns._ID +"=" + movieId,
                        selectionArgs, null, null, sortOrder);
                break;
            case TRAILERS:
                // we don't use SQLiteQueryBuilder and
                // call db itself
                c = db.query(MovieContract.TrailerTable.DB_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TRAILERS_ID:
                String trailerId = MovieContract.TrailerTable.getTrailerId(uri);
                c = db.query(MovieContract.TrailerTable.DB_TABLE_NAME, projection,
                        // WHERE _ID=id
                        BaseColumns._ID +"=" + trailerId,
                        selectionArgs, null, null, sortOrder);
                break;
            case REVIEWS:
                // we don't use SQLiteQueryBuilder and
                // call db itself
                c = db.query(MovieContract.ReviewTable.DB_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case REVIEWS_ID:
                String reviewId = MovieContract.ReviewTable.getReviewId(uri);
                c = db.query(MovieContract.ReviewTable.DB_TABLE_NAME, projection,
                        // WHERE _ID=id
                        BaseColumns._ID +"=" + reviewId,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                return MovieContract.MovieTable.CONTENT_TYPE;
            case MOVIES_ID:
                return MovieContract.TrailerTable.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MovieContract.TrailerTable.CONTENT_TYPE;
            case TRAILERS_ID:
                return MovieContract.MovieTable.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return MovieContract.ReviewTable.CONTENT_TYPE;
            case REVIEWS_ID:
                return MovieContract.ReviewTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIES:
                long movieId = db.insertOrThrow(MovieContract.MovieTable.DB_TABLE_NAME, null, contentValues);
                return MovieContract.MovieTable.buildMovieUri(movieId);
            case TRAILERS:
                long trailerId = db.insertOrThrow(MovieContract.TrailerTable.DB_TABLE_NAME, null, contentValues);
                return MovieContract.TrailerTable.buildTrailerUri(trailerId);
            case REVIEWS:
                long reviewId = db.insertOrThrow(MovieContract.ReviewTable.DB_TABLE_NAME, null, contentValues);
                return MovieContract.ReviewTable.buildReviewUri(reviewId);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String id;
        String selectionCriteria;

        // this makes delete all rows return the number of rows deleted
        if (selection == null) selection = "1";

        switch(match) {
            case MOVIES:
                // delete everything from table and return the number of rows deleted
                return db.delete(MovieContract.MovieTable.DB_TABLE_NAME, selection, selectionArgs);
            case MOVIES_ID:
                id = MovieContract.MovieTable.getMovieId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(MovieContract.MovieTable.DB_TABLE_NAME, selectionCriteria, selectionArgs);
            case TRAILERS_ID:
                id = MovieContract.TrailerTable.getTrailerId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(MovieContract.MovieTable.DB_TABLE_NAME, selectionCriteria, selectionArgs);
            case REVIEWS_ID:
                id = MovieContract.ReviewTable.getReviewId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(MovieContract.MovieTable.DB_TABLE_NAME, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("this method is not implemented");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRAILERS:
                db.beginTransaction();
                int insertCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(MovieContract.TrailerTable.DB_TABLE_NAME, null, value);
                        if (id != -1) {
                            insertCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                // TODO do we need this notifications?
//                getContext().getContentResolver().notifyChange(uri, null);
                return insertCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}















