package com.ilyarudyak.android.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilyarudyak.android.popmovies.db.MovieContract.MovieTable;
import com.ilyarudyak.android.popmovies.db.MovieContract.ReviewTable;
import com.ilyarudyak.android.popmovies.db.MovieContract.TrailerTable;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieTable.DB_TABLE_NAME + " (" +
                MovieTable._ID +                    " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieTable.DB_TMDB_ID +             " INTEGER UNIQUE NOT NULL, " +
                MovieTable.DB_TITLE +               " TEXT NOT NULL, " +
                MovieTable.DB_POSTER_PATH_ABSOLUTE +         " TEXT NOT NULL, " +
                MovieTable.DB_RELEASE_DATE +        " TEXT NOT NULL, " +
                MovieTable.DB_USER_RATING +         " REAL NOT NULL, " +
                MovieTable.DB_PLOT_SYNOPSIS +       " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerTable.DB_TABLE_NAME + " (" +
                TrailerTable._ID +                   " INTEGER PRIMARY KEY AUTOINCREMENT," +
                //TODO add reference
                TrailerTable.DB_MOVIE_ID +           " INTEGER NOT NULL, " +
                TrailerTable.DB_NAME +               " TEXT NOT NULL, " +
                TrailerTable.DB_PATH +               " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewTable.DB_TABLE_NAME + " (" +
                ReviewTable._ID +                    " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewTable.DB_MOVIE_ID +            " INTEGER NOT NULL, " +
                ReviewTable.DB_REVIEW +              " TEXT NOT NULL " +
                " );";


        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieTable.DB_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerTable.DB_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewTable.DB_TABLE_NAME);
        onCreate(db);
    }
}
