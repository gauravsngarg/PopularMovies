package gauravsngarg.com.popularmovies.model;

import android.support.annotation.NonNull;


@SuppressWarnings("ALL")
public class MovieItem implements Comparable<MovieItem> {

    private String MovieId;
    private String movieTitle;
    private String moviePosterPath;
    private String moviePlotSynopsis;
    private String movieUserRating;
    private String movieReleaseDate;
    private String moviePopularity;

    public MovieItem() {

    }

    public String getMovieId() {
        return MovieId;
    }

    public void setMovieId(String movieId) {
        MovieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    public String getMoviePlotSynopsis() {
        return moviePlotSynopsis;
    }

    public void setMoviePlotSynopsis(String moviePlotSynopsis) {
        this.moviePlotSynopsis = moviePlotSynopsis;
    }

    public String getMovieUserRating() {
        return movieUserRating;
    }

    public void setMovieUserRating(String movieUserRating) {
        this.movieUserRating = movieUserRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMoviePopularity() {
        return moviePopularity;
    }

    public void setMoviePopularity(String moviePopularity) {
        this.moviePopularity = moviePopularity;
    }

    @Override
    public int compareTo(@NonNull MovieItem o) {
        if (Float.parseFloat(moviePopularity) > Float.parseFloat(o.moviePopularity))
            return -1;
        else if (Float.parseFloat(moviePopularity) < Float.parseFloat(o.moviePopularity))
            return 1;
        else
            return 0;
    }
}
