package com.ilyarudyak.android.popmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse json string that we get using API call
 * http://api.themoviedb.org/3/movie/id/videos?api_key=...
 * We get youtube key only for the first trailer in the file
 * and add it to the provided Movie object.
 */
public class JsonTrailerParser {

    private String trailerJsonStr;
    private List<Movie.Trailer> trailersList;

    public JsonTrailerParser(String trailerJsonStr)
            throws JSONException {

        this.trailerJsonStr = trailerJsonStr;
        trailersList = new ArrayList<>();
        getTrailersDataFromJson();
    }

    public List<Movie.Trailer> getTrailersList() {
        return trailersList;
    }

    private void getTrailersDataFromJson() throws JSONException {

        JSONObject resultJson = new JSONObject(trailerJsonStr);
        JSONArray trailersArray = resultJson.getJSONArray(Movie.TMDB_RESULTS);

        for(int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailerJson = trailersArray.getJSONObject(i);
            trailersList.add(new Movie.Trailer(
                trailerJson.getString(Movie.TMDB_TRAILER_KEY),
                trailerJson.getString(Movie.TMDB_TRAILER_NAME)
            ));
        }
    }






}
