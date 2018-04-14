package gauravsngarg.com.popularmovies.model;

/**
 * Created by GG on 14/04/18.
 */

public class MovieItem {

    String movieTitle;
    String moviePosterPath;
    String moviePlotSynopsis;
    String movieUserRating;
    String movieReleaseDate;
    String moviePopularity;

    public MovieItem(){

    }

    public MovieItem(String movieTitle, String moviePosterPath,
                     String moviePlotSynopsis, String movieUserRating,
                     String movieReleaseDate, String moviePopularity){
        this.movieTitle = movieTitle;
        this.moviePosterPath = moviePosterPath;
        this.moviePlotSynopsis = moviePlotSynopsis;
        this.movieUserRating = movieUserRating;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePopularity = moviePopularity;
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
}
