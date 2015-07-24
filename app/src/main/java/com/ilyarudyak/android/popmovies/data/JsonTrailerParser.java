package com.ilyarudyak.android.popmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parse json string that we get using API call
 * http://api.themoviedb.org/3/movie/id/videos?api_key=...
 * We get youtube key only for the first trailer in the file
 * and add it to the provided Movie object.
 */
public class JsonTrailerParser {

    public static final String TMDB_RESULTS = "results";

    private String trailerKey;

    public JsonTrailerParser(String trailerJsonStr)
            throws JSONException {

        JSONObject resultJson = new JSONObject(trailerJsonStr);
        JSONArray trailersArray = resultJson.getJSONArray(TMDB_RESULTS);

        JSONObject trailerJson = trailersArray.getJSONObject(0);
        trailerKey = trailerJson.getString(Movie.TMDB_YOUTUBE_TRAILER_KEY);
    }

    public String getTrailerKey() {
        return trailerKey;
    }
}
