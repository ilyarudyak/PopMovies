package com.ilyarudyak.android.popmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.db.MovieContract;
import com.ilyarudyak.android.popmovies.db.MovieDbHelper;

/**
 * Created by ilyarudyak on 8/6/15.
 */
public class TestMovieProvider extends AndroidTestCase {

    public static final String TAG = TestMovieProvider.class.getSimpleName();

    private SQLiteDatabase db;
    private Cursor c;

    @Override
    public void setUp() {
        db = new MovieDbHelper(mContext).getWritableDatabase();
    }


    public void testQuery() throws Throwable {

        String plot = "Twenty-two years after the events of Jurassic Park, " +
                "Isla Nublar now features a fully functioning dinosaur theme park, " +
                "Jurassic World, as originally envisioned by John Hammond.";

        // insert entry to database
        String query = "INSERT INTO movie VALUES (" +
                "NULL, " +
                "135397, " +
                "'Jurassic World', " +
                "'http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg', " +
                "'2015-06-12', " +
                "7.0, " +
                "'Twenty-two years after the events of Jurassic Park, Isla Nublar now " +
                "features a fully functioning dinosaur theme park, Jurassic World, as " +
                "originally envisioned by John Hammond.')";
        db.execSQL(query);

        // query db and check entry
        c = mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        assertEquals("problems from testQuery()", 1, c.getCount());

        c.moveToFirst();
        assertEquals(135397, c.getInt(1));
        assertEquals("Jurassic World", c.getString(2));
        assertEquals("http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", c.getString(3));
        assertEquals("2015-06-12", c.getString(4));
        assertEquals(7.0, c.getDouble(5));
        assertEquals(plot, c.getString(6));
        c.close();

    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }
}
