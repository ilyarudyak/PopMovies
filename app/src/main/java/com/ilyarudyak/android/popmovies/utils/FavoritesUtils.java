package com.ilyarudyak.android.popmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ilyarudyak on 7/30/15.
 */
public class FavoritesUtils {

    private static final String LOG_TAG = FavoritesUtils.class.getSimpleName();

    // name of shared prefs file, that contains favorite movies ids
    public static final String PREFS_NAME = "FavoriteMovies";
    public static final String FAVORITES = "favorites";

    public static Set<String> getFavorities(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0)
                .getStringSet(FAVORITES, new HashSet<String>());
    }

    public static void putFavorities(Context c, Integer movieId) {

        // get set of favorite movies and add new elemant
        Set<String> favorites = getFavorities(c);
        if (favorites == null) {
            favorites = new HashSet<>();
        }
        favorites.add(Integer.toString(movieId));
        Log.d(LOG_TAG, favorites.toString());

        // put updated set back into prefs file
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putStringSet(FAVORITES, favorites).apply();
    }
}
