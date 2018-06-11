package gauravsngarg.com.popularmovies.data;

import android.provider.BaseColumns;


public class FavListContract {

    public static final class FavListEntry implements BaseColumns {
        public static final String TABLE_NAME = "favlist";
        public static final String COLUMN_MOVIE_ID = "mov_id";
        public static final String COULUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "overview";
        public static final String COLUMN_MOVIE_USER_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
       // public static final String COLUMN_MOVIE_TRAILER = "trailer";

    }
}
