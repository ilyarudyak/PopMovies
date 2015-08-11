package com.ilyarudyak.android.popmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ilyarudyak on 7/30/15.
 */
public class FavPrefUtils {

    private static final String LOG_TAG = FavPrefUtils.class.getSimpleName();

    // name of shared prefs file, that contains favorite movies ids
    public static final String PREFS_NAME = "FavoriteMovies";
    public static final String FAVORITIES = "favorities";

    public static Set<String> getFavorities(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0)
                .getStringSet(FAVORITIES, new HashSet<String>());
    }

    public static void putFavorities(Context c, Integer movieId) {

        // get set of favorite movies and add new elemant
        Set<String> favorites = getFavorities(c);
        if (favorites == null) {
            favorites = new HashSet<>();
        }
        favorites.add(Integer.toString(movieId));
//        Log.d(LOG_TAG, favorites.toString());

        // put updated set back into prefs file
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putStringSet(FAVORITIES, favorites).apply();
    }
    public static void removeFromFavorities(Context c, Integer movieId) {

        // get set of favorite movies and remove elemant
        Set<String> favorites = getFavorities(c);
        favorites.remove(Integer.toString(movieId));

        // put updated set back into prefs file
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putStringSet(FAVORITIES, favorites).apply();
    }


}
