package com.ilyarudyak.android.popmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.db.Contract.MovieTable;
import com.ilyarudyak.android.popmovies.db.Contract.ReviewTable;
import com.ilyarudyak.android.popmovies.db.Contract.TrailerTable;
import com.ilyarudyak.android.popmovies.db.DbHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    private SQLiteDatabase db;

    public void setUp() {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        db = new DbHelper(this.mContext).getWritableDatabase();
    }

    public void testCreateDb() throws Throwable {

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
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
        db.close();
    }

    public void testTrailerTable() throws Throwable {

        String query = "INSERT INTO trailer VALUES (NULL, 135397, 'Teaser'," +
                "'https://www.youtube.com/watch?v=bvu-zlR5A8Q')";
        db.execSQL(query);

        Cursor c = db.rawQuery("SELECT * FROM trailer", null);
        assertEquals(1, c.getCount());

        c.moveToFirst();
        assertEquals(135397, c.getInt(1));
        assertEquals("Teaser", c.getString(2));
        assertEquals("https://www.youtube.com/watch?v=bvu-zlR5A8Q", c.getString(3));


        c.close();
        db.close();
    }

}
