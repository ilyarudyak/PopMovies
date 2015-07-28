package com.ilyarudyak.android.popmovies.db;

import android.provider.BaseColumns;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class Contract {

    // class that defines column names of the movie table
    public static final class MovieTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "movie";

        public static final String DB_TMDB_ID = "tmdb_id";
        public static final String DB_TITLE = "title";
        public static final String DB_POSTER_PATH = "poster_path";
        public static final String DB_RELEASE_DATE = "release_date";
        public static final String DB_USER_RATING = "user_rating";
        public static final String DB_PLOT_SYNOPSIS = "plot_synopsis";
    }

    // class that defines column names of the trailer table
    public static final class TrailerTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "trailer";

        public static final String DB_MOVIE_ID = "movie_id";
        public static final String DB_NAME = "name";
        public static final String DB_PATH = "path";
    }

    // class that defines column names of the trailer table
    public static final class ReviewTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "review";

        public static final String DB_MOVIE_ID = "movie_id";
        public static final String DB_REVIEW = "review";
    }


}













