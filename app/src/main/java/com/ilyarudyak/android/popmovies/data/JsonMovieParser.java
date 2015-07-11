package com.ilyarudyak.android.popmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse JSON string from API call to TMDB into
 * List of Movies objects.
 */
public class JsonMovieParser {

    public static final String TMDB_RESULTS = "results";

    private String moviesJsonStr;
    private List<Movie> moviesList;

    public JsonMovieParser(String moviesJsonStr)
            throws JSONException {
        this.moviesJsonStr = moviesJsonStr;
        moviesList = new ArrayList<>();
        getMoviesDataFromJson();
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    /**
     * Take the String representing the list of movies in JSON Format and
     * pull out the data we need to construct the wireframes.
     */
    private void getMoviesDataFromJson()
            throws JSONException {

        JSONObject resultJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = resultJson.getJSONArray(TMDB_RESULTS);

        for(int i = 0; i < moviesArray.length(); i++) {

            // get the JSON object representing the movie
            JSONObject movieJson = moviesArray.getJSONObject(i);

            moviesList.add(new Movie(
                    movieJson.getInt(Movie.TMDB_ID),
                    movieJson.getString(Movie.TMDB_ORIGINAl_TITLE),
                    movieJson.getString(Movie.TMDB_PLOT_SYNOPSIS),
                    movieJson.getString(Movie.TMDB_POSTER_PATH_RELATIVE),
                    movieJson.getString(Movie.TMDB_RELEASE_DATE),
                    movieJson.getDouble(Movie.TMDB_USER_RATING)
            ));
        }
    }
}
