package com.ilyarudyak.android.popmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.db.MovieContract;

/**
 * Created by ilyarudyak on 8/8/15.
 */
public class TestMovieProvider2 extends AndroidTestCase {

    public static final String TAG = TestMovieProvider2.class.getSimpleName();

    @Override
    public void setUp() {

        mContext.getContentResolver().delete(
                MovieContract.MovieTable.CONTENT_URI,
                null,
                null
        );
    }

    public void testInsert() throws Throwable {

        String plot = "Twenty-two years after the events of Jurassic Park, " +
                "Isla Nublar now features a fully functioning dinosaur theme park, " +
                "Jurassic World, as originally envisioned by John Hammond.";

        ContentValues cv = TestUtils.createJWMovieContentValues();
        mContext.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI, cv);

        // query db and check entry
        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        assertEquals("problems from testInsert()", 1, c.getCount());

        c.moveToFirst();
        assertEquals(135397, c.getInt(1));
        assertEquals("Jurassic World", c.getString(2));
        assertEquals("http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", c.getString(3));
        assertEquals("2015-06-12", c.getString(4));
        assertEquals(7.0, c.getDouble(5));
        assertEquals(plot, c.getString(6));
        c.close();
    }

    public void testDelete() throws Throwable {

        // insert entry to database
        ContentValues cv = TestUtils.createJWMovieContentValues();
        Uri uri = mContext.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI, cv);

        mContext.getContentResolver().delete(uri, null, null);

        // query db and check entry
        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        assertEquals("problems from testDelete()", 0, c.getCount());
        c.close();

    }
}
