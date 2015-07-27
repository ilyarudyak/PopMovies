package com.ilyarudyak.android.popmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilyarudyak on 7/28/15.
 */
public class JsonReviewParser {

    private String reviewJsonStr;
    private List<String> reviewsList;

    public JsonReviewParser(String trailerJsonStr)
            throws JSONException {

        this.reviewJsonStr = trailerJsonStr;
        reviewsList = new ArrayList<>();
        getReviesDataFromJson();
    }

    public List<String> getReviewsList() {
        return reviewsList;
    }

    private void getReviesDataFromJson() throws JSONException {

        JSONObject resultJson = new JSONObject(reviewJsonStr);
        JSONArray reviewsArray = resultJson.getJSONArray(Movie.TMDB_RESULTS);

        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewJson = reviewsArray.getJSONObject(i);
            reviewsList.add(reviewJson.getString(Movie.TMDB_REVIEW));
        }
    }
}
