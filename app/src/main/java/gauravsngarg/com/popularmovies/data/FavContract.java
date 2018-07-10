package gauravsngarg.com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavContract {

    public static final String AUTHORITY = "gauravsngarg.com.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASKS = "favs";

    public static final class FavEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        public static final String TABLE_NAME = "favlist";
        public static final String COLUMN_MOVIE_ID = "mov_id";
        public static final String COULUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "overview";
        public static final String COLUMN_MOVIE_USER_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_YEAR = "release_year";



    }
}