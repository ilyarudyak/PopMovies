package com.ilyarudyak.android.popmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.ilyarudyak.android.popmovies.db.MovieContract.MovieTable;

/**
 * Created by ilyarudyak on 8/6/15.
 */
public class MovieProvider extends ContentProvider {

    public static final String TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;

    private MovieDbHelper mMovieDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        // movie id is a string in this matcher
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIES_ID);
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
                c = db.query(MovieTable.DB_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MOVIES_ID:
                String id = MovieTable.getMovieId(uri);
                c = db.query(MovieTable.DB_TABLE_NAME, projection,
                        // WHERE _ID=id
                        BaseColumns._ID +"=" + id,
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
                return MovieTable.CONTENT_TYPE;
            case MOVIES_ID:
                return MovieTable.CONTENT_ITEM_TYPE;
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
                long id = db.insertOrThrow(MovieTable.DB_TABLE_NAME, null, contentValues);
                return MovieTable.buildMovieUri(id);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        // this makes delete all rows return the number of rows deleted
        if (selection == null) selection = "1";

        switch(match) {
            case MOVIES:
                // delete everything from table and return the number of rows deleted
                return db.delete(MovieTable.DB_TABLE_NAME, selection, selectionArgs);
            case MOVIES_ID:
                String id = MovieTable.getMovieId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(MovieTable.DB_TABLE_NAME, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("this method is not implemented");
    }
}
