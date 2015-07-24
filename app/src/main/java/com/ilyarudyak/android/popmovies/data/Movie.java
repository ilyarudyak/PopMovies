package com.ilyarudyak.android.popmovies.data;

import android.net.Uri;

/**
 * Model class to encapsulate a movie.
 */
public class Movie {

    // stage 1
    public static final String TMDB_ID =                         "id";
    public static final String TMDB_ORIGINAl_TITLE =             "original_title";
    public static final String TMDB_POSTER_PATH_RELATIVE =       "poster_path";
    public static final String TMDB_RELEASE_DATE =               "release_date";
    public static final String TMDB_USER_RATING =                "vote_average";
    public static final String TMDB_PLOT_SYNOPSIS =              "overview";

    public static final String TMDB_POSTER_PATH_ABSOLUTE =       "poster_path_absolute";

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";

    private Integer id;                         // id
    private String originalTitle;               // original_title
    private String posterPathRelative;          // poster_path
    private String posterPathAbsolute;          // n/a
    private String releaseDate;                 // release_date
    private Double userRating;                  // vote_average
    private String plotSynopsis;                // overview

    // stage 2
    public static final String TMDB_TRAILER_KEY =          "key";
    public static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?";
    public static final String TRAILER_PATH_ABSOLUTE = "trailer_path_absolute";


    private String trailerPathAbsolute;


    public Movie(Integer id, String originalTitle,
                 String plotSynopsis, String posterPathRelative,
                 String releaseDate, Double userRating) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.posterPathRelative = posterPathRelative;
        this.releaseDate = releaseDate;
        this.userRating = userRating;

        buildPosterPathAbsolute();
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    // we need get method only for absolute path
    public String getPosterPathAbsolute() {
        return posterPathAbsolute;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getUserRating() {
        return userRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPathAbsolute + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating='" + userRating + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                '}';
    }

    private void buildPosterPathAbsolute() {

        posterPathAbsolute = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                // we delete slash - we use addPath method
                // that adds slash after size and relative path contains
                // another slash
                .appendPath(posterPathRelative.replace("/", ""))
                .build()
                .toString();

    }

    // stage 2
    public String getTrailerPathAbsolute() {
        return trailerPathAbsolute;
    }

    public void setTrailerPathAbsolute(String key) {
        buildYoutubeTrailerPathAbsolute(key);
    }

    private void buildYoutubeTrailerPathAbsolute(String key) {

        final String V = "v";
        trailerPathAbsolute = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter(V, key)
                .build()
                .toString();
    }
}
