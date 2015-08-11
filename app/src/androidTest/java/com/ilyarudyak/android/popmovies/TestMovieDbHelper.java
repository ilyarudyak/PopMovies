package com.ilyarudyak.android.popmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.db.MovieDbHelper;
import com.ilyarudyak.android.popmovies.db.MovieContract.MovieTable;
import com.ilyarudyak.android.popmovies.db.MovieContract.ReviewTable;
import com.ilyarudyak.android.popmovies.db.MovieContract.TrailerTable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMovieDbHelper extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    private SQLiteDatabase db;
    private Cursor c;

    public void setUp() {
        db = new MovieDbHelper(mContext).getWritableDatabase();
    }

    public void testCreateDb() throws Throwable {

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Set<String> tables = new HashSet<>(Arrays.asList(
                MovieTable.DB_TABLE_NAME,
                TrailerTable.DB_TABLE_NAME,
                ReviewTable.DB_TABLE_NAME
        ));

        // cursor should contain 5 rows including
        // 3 our tables and 2 technical ones
        while (c.moveToNext()) {
            tables.remove(c.getString(0));
        }

        assertEquals(0, tables.size());
        c.close();
    }

    public void testTrailerTable() throws Throwable {

        // insert entry to database
        String query = "INSERT INTO trailer VALUES (NULL, 135397, 'Teaser'," +
                "'https://www.youtube.com/watch?v=bvu-zlR5A8Q')";
        db.execSQL(query);

        c = db.rawQuery("SELECT * FROM trailer", null);
        assertEquals(1, c.getCount());

        c.moveToFirst();
        assertEquals(135397, c.getInt(1));
        assertEquals("Teaser", c.getString(2));
        assertEquals("https://www.youtube.com/watch?v=bvu-zlR5A8Q", c.getString(3));

        c.close();
    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }
}
