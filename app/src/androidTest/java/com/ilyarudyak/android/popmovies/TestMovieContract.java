package com.ilyarudyak.android.popmovies;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.ilyarudyak.android.popmovies.db.MovieContract.TrailerTable;

/**
 * Created by ilyarudyak on 8/6/15.
 */
public class TestMovieContract extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieContract.class.getSimpleName();

    public void testBuildTrailerUri() throws Throwable {

        String expectedUriString = "content://com.ilyarudyak.android.popmovies/trailer/10";
        assertEquals(expectedUriString, TrailerTable.buildTrailerUri(10).toString());
    }

    public void testGetTrailerId() throws Throwable {
        Uri uri = Uri.parse("content://com.ilyarudyak.android.popmovies/trailer/10");
        String expectedId = "10";
        assertEquals(expectedId, TrailerTable.getTrailerId(uri));
    }

}
