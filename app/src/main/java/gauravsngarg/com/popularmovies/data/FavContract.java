package gauravsngarg.com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "gauravsngarg.com.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "favs";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class FavEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        public static final String TABLE_NAME = "favlist";
        public static final String COLUMN_MOVIE_ID = "mov_id";
        public static final String COULUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "overview";
        public static final String COLUMN_MOVIE_USER_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";


    }
}