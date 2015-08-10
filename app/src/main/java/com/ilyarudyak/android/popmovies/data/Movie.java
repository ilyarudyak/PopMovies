package com.ilyarudyak.android.popmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.ilyarudyak.android.popmovies.DetailActivity;
import com.ilyarudyak.android.popmovies.db.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to encapsulate a movie.
 */
public class Movie {

    public static final String TMDB_RESULTS =                   "results";

    public static final String TMDB_ID =                         "id";
    public static final String TMDB_ORIGINAl_TITLE =             "original_title";
    public static final String TMDB_POSTER_PATH_RELATIVE =       "poster_path";
    public static final String TMDB_RELEASE_DATE =               "release_date";
    public static final String RELEASE_YEAR =                    "release_year";
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

    public static final String TMDB_TRAILER_KEY =                   "key";
    public static final String TMDB_TRAILER_NAME =                  "name";
    public static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?";
    public static final String TRAILER_LIST = "com.ilyarudyak.android.popmovies.data.trailer_list";

    public static final String TMDB_REVIEW =                        "content";
    public static final String REVIEW_LIST = "com.ilyarudyak.android.popmovies.data.review_list";

    public static final String MOVIE_BUNDLE = "bundle";
    
    private List<Trailer> movieTrailers;
    private List<String> movieReviews;

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

        movieTrailers = new ArrayList<>();
        movieReviews = new ArrayList<>();
    }

    // getters
    public Integer getId() {
        return id;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getPlotSynopsis() {
        return plotSynopsis;
    }
    public String getPosterPathRelative() {
        return posterPathRelative;
    }
    public String getPosterPathAbsolute() {
        return posterPathAbsolute;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public Double getUserRating() {
        return userRating;
    }
    // get year from release date
    public String getYear() {
        if (releaseDate != null && releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPathRelative='" + posterPathRelative + '\'' +
                ", posterPathAbsolute='" + posterPathAbsolute + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating=" + userRating +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", movieTrailers=" + movieTrailers +
                ", movieReviews=" + movieReviews +
                '}';
    }

    // helper methods
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

    // ----------------- trailer & review -----------------

    public static class Trailer implements Parcelable {

        private String trailerName;
        private String trailerPathAbsolute;

        public Trailer(String trailerKey, String trailerName) {
            this.trailerPathAbsolute = buildPathAbsolute(trailerKey);
            this.trailerName = trailerName;
        }

        private String buildPathAbsolute(String trailerKey) {
            final String V = "v";
            return Uri.parse(TRAILER_BASE_URL).buildUpon()
                    .appendQueryParameter(V, trailerKey)
                    .build()
                    .toString();
        }

        public String getTrailerName() {
            return trailerName;
        }

        public String getTrailerPathAbsolute() {
            return trailerPathAbsolute;
        }

        @Override
        public String toString() {
            return "MovieTrailer{" +
                    "trailerName='" + trailerName + '\'' +
                    ", trailerPathAbsolute='" + trailerPathAbsolute + '\'' +
                    '}';
        }

        // ---------------- Parcelable methods ----------------

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(trailerName);
            parcel.writeString(trailerPathAbsolute);
        }

        public static final Parcelable.Creator<Trailer> CREATOR
                = new Parcelable.Creator<Trailer>() {
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };

        private Trailer(Parcel in) {
            trailerName = in.readString();
            trailerPathAbsolute = in.readString();
        }

    }
    public void setMovieTrailers(List<Trailer> trailers) {
        movieTrailers.addAll(trailers);
    }
    public List<Trailer> getMovieTrailers() {
        return movieTrailers;
    }

    public List<String> getMovieReviews() {
        return movieReviews;
    }
    public void setMovieReviews(List<String> movieReviews) {
        this.movieReviews.addAll(movieReviews);
    }

    // ----------------- build bundle etc. -----------------

    public static Bundle buildDetailBundle(Movie m) {

        Bundle bundle = new Bundle();
        bundle.putInt(TMDB_ID, m.getId());
        bundle.putString(TMDB_ORIGINAl_TITLE, m.getOriginalTitle());
        bundle.putString(TMDB_POSTER_PATH_ABSOLUTE, m.getPosterPathAbsolute());
        bundle.putString(RELEASE_YEAR, m.getYear());
        bundle.putString(TMDB_USER_RATING, Double.toString(m.getUserRating()));
        bundle.putString(TMDB_PLOT_SYNOPSIS, m.getPlotSynopsis());
        bundle.putParcelableArrayList(TRAILER_LIST,
                (ArrayList<? extends Parcelable>) m.getMovieTrailers());
        bundle.putStringArrayList(REVIEW_LIST,
                (ArrayList<String>) m.getMovieReviews());
        return bundle;
    }
    // cursor.moveToFirst() not included
    public static Bundle buildDetailBundle(Cursor c) {
        Bundle bundle = new Bundle();
        bundle.putInt(TMDB_ID, c.getInt(1));
        bundle.putString(TMDB_ORIGINAl_TITLE, c.getString(2));
        bundle.putString(TMDB_POSTER_PATH_ABSOLUTE, c.getString(3));
        bundle.putString(RELEASE_YEAR, c.getString(4));
        bundle.putString(TMDB_USER_RATING, Double.toString(c.getDouble(5)));
        bundle.putString(TMDB_PLOT_SYNOPSIS, c.getString(6));
        return bundle;
    }
    public static Intent buildDetailIntent(Context context, Movie m) {
        Intent intent = new Intent(context, DetailActivity.class)
                .putExtra(MOVIE_BUNDLE, buildDetailBundle(m));
        return intent;
    }

    public static ContentValues buildMovieContentValues(Movie m) {

        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieTable.DB_TMDB_ID, m.getId());
        cv.put(MovieContract.MovieTable.DB_TITLE, m.getOriginalTitle());
        cv.put(MovieContract.MovieTable.DB_POSTER_PATH_ABSOLUTE, m.getPosterPathAbsolute());
        cv.put(MovieContract.MovieTable.DB_RELEASE_YEAR, m.getReleaseDate());
        cv.put(MovieContract.MovieTable.DB_USER_RATING, m.getUserRating());
        cv.put(MovieContract.MovieTable.DB_PLOT_SYNOPSIS, m.getPlotSynopsis());

        return cv;

    }
    public static ContentValues buildMovieContentValues(Bundle b) {

        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieTable.DB_TMDB_ID, b.getInt(TMDB_ID, 0));
        cv.put(MovieContract.MovieTable.DB_TITLE, b.getString(TMDB_ORIGINAl_TITLE));
        cv.put(MovieContract.MovieTable.DB_POSTER_PATH_ABSOLUTE, b.getString(TMDB_POSTER_PATH_ABSOLUTE));
        cv.put(MovieContract.MovieTable.DB_RELEASE_YEAR, b.getString(RELEASE_YEAR));
        cv.put(MovieContract.MovieTable.DB_USER_RATING, b.getString(TMDB_USER_RATING));
        cv.put(MovieContract.MovieTable.DB_PLOT_SYNOPSIS, b.getString(TMDB_PLOT_SYNOPSIS));

        return cv;
    }
    public static ContentValues[] buildTrailersContentValues(Bundle b) {

        List<Trailer> trailers = b.getParcelableArrayList(TRAILER_LIST);
        Integer movieId = b.getInt(TMDB_ID, 0);

        ContentValues[] contentValues = new ContentValues[trailers.size()];
        int index = 0;
        for (Trailer t : trailers) {
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.TrailerTable.DB_MOVIE_ID, movieId);
            cv.put(MovieContract.TrailerTable.DB_NAME, t.getTrailerName());
            cv.put(MovieContract.TrailerTable.DB_PATH, t.getTrailerPathAbsolute());
            contentValues[index++] = cv;
        }

        return contentValues;
    }
    public static ContentValues[] buildReviewsContentValues(Bundle b) {

        List<String> reviews = b.getStringArrayList(REVIEW_LIST);
        Integer movieId = b.getInt(TMDB_ID, 0);

        ContentValues[] contentValues = new ContentValues[reviews.size()];
        int index = 0;
        for (String r : reviews) {
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.TrailerTable.DB_MOVIE_ID, movieId);
            cv.put(MovieContract.TrailerTable.DB_NAME, r);
            contentValues[index++] = cv;
        }

        return contentValues;
    }


}









