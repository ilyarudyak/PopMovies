package com.ilyarudyak.android.popmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class MovieContract {

    // content authority and base uri
    public static final String CONTENT_AUTHORITY = "com.ilyarudyak.android.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // we use this for create uris for each table
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    // class that defines column names of the movie table
    public static final class MovieTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "movie";

        public static final String DB_TMDB_ID = "tmdb_id";
        public static final String DB_TITLE = "title";
        public static final String DB_POSTER_PATH_ABSOLUTE = "poster_path_absolute";
        public static final String DB_RELEASE_YEAR = "release_date";
        public static final String DB_USER_RATING = "user_rating";
        public static final String DB_PLOT_SYNOPSIS = "plot_synopsis";

        // ------------ building movie Uri ------------

        // (1) content uri for this table
        // content://com.ilyarudyak.android.popmovies/movie
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        // (2) types for this table (for table and each entry)
        public static final String CONTENT_TYPE = ContentResolver
                .CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver
                .CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // (3) build uri based on id and return id based on uri
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    // class that defines column names of the trailer table
    public static final class TrailerTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "trailer";

        public static final String DB_MOVIE_TMDB_ID = "movie_id";
        public static final String DB_NAME = "name";
        public static final String DB_PATH = "path";

        // ------------ building trailer Uri ------------
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                                                .buildUpon()
                                                .appendPath(PATH_TRAILER)
                                                .build();

        public static final String CONTENT_TYPE = ContentResolver
                .CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver
                .CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getTrailerId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    // class that defines column names of the trailer table
    public static final class ReviewTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "review";

        public static final String DB_MOVIE_TMDB_ID = "movie_id";
        public static final String DB_REVIEW = "review";

        // ------------ building trailer Uri ------------
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_REVIEW)
                .build();

        public static final String CONTENT_TYPE = ContentResolver
                .CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver
                .CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getReviewId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}













