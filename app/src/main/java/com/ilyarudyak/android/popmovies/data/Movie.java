package com.ilyarudyak.android.popmovies.data;

/**
 * Model class to encapsulate a movie.
 */
public class Movie {

    public static final String ID =             "id";
    public static final String ORIGINAl_TITLE = "original_title";
    public static final String POSTER_PATH =    "poster_path";
    public static final String RELEASE_DATE =   "release_date";
    public static final String PLOT_SYNOPSIS =  "overview";

    private int id;                     // id
    private String originalTitle;       // original_title
    private String posterPath;          // poster_path
    private String releaseDate;         // release_date
    private String userRating;          // vote_average
    private String plotSynopsis;        // overview

    //TODO add trailer, review on stage 2
    //TODO we have length on mockups but don't have it in JSON
    //TODO and we don't have a link to review in mockups

    public Movie(int id, String originalTitle,
                 String plotSynopsis, String posterPath,
                 String releadeDate, String userRating) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.posterPath = posterPath;
        this.releaseDate = releadeDate;
        this.userRating = userRating;
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

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating='" + userRating + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                '}';
    }
}
