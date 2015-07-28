package com.ilyarudyak.android.popmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse JSON string from API call to TMDB into
 * list of Movies, Trailers or Reviews based on
 * provided flag.
 */
public class JsonParser {

    public static final Integer MOVIE =   0;
    public static final Integer TRAILER = 1;
    public static final Integer REVIEW =  2;

    private String jsonString;

    private List<Movie> moviesList;
    private List<Movie.Trailer> trailersList;
    private List<String> reviewsList;

    public JsonParser(String jsonString, Integer flag)
            throws JSONException {
        this.jsonString = jsonString;

        if (flag.equals(MOVIE)) {
            moviesList = new ArrayList<>();
            getMoviesDataFromJson();
        } else if (flag.equals(TRAILER)) {
            trailersList = new ArrayList<>();
            getTrailersDataFromJson();
        } else if (flag.equals(REVIEW)) {
            reviewsList = new ArrayList<>();
            getReviewsDataFromJson();
        } else {
            throw new JSONException("This is an incorrect flag: " + flag);
        }
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }
    public List<Movie.Trailer> getTrailersList() {
        return trailersList;
    }
    public List<String> getReviewsList() {
        return reviewsList;
    }

    // helper methods to get data from json
    private void getMoviesDataFromJson() throws JSONException {

        JSONObject resultJson = new JSONObject(jsonString);
        JSONArray moviesArray = resultJson.getJSONArray(Movie.TMDB_RESULTS);

        for(int i = 0; i < moviesArray.length(); i++) {

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
    private void getTrailersDataFromJson() throws JSONException {

        JSONObject resultJson = new JSONObject(jsonString);
        JSONArray trailersArray = resultJson.getJSONArray(Movie.TMDB_RESULTS);

        for(int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailerJson = trailersArray.getJSONObject(i);
            trailersList.add(new Movie.Trailer(
                    trailerJson.getString(Movie.TMDB_TRAILER_KEY),
                    trailerJson.getString(Movie.TMDB_TRAILER_NAME)
            ));
        }
    }
    private void getReviewsDataFromJson() throws JSONException {

        JSONObject resultJson = new JSONObject(jsonString);
        JSONArray reviewsArray = resultJson.getJSONArray(Movie.TMDB_RESULTS);

        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewJson = reviewsArray.getJSONObject(i);
            reviewsList.add(reviewJson.getString(Movie.TMDB_REVIEW));
        }
    }
}
