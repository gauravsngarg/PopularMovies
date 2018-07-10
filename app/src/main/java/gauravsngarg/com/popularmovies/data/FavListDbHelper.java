package gauravsngarg.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movielist.db";
    private static final int DATABASE_VERSION = 1;


    public FavListDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_FAV_LIST_TABLE = "" +
                "CREATE TABLE " +
                FavListContract.FavListEntry.TABLE_NAME + "( " +
                FavListContract.FavListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavListContract.FavListEntry.COLUMN_MOVIE_ID + " TEXT, " +
                FavListContract.FavListEntry.COULUMN_MOVIE_TITLE + " TEXT, " +
                FavListContract.FavListEntry.COLUMN_MOVIE_PLOT_SYNOPSIS + " TEXT, " +
                FavListContract.FavListEntry.COLUMN_MOVIE_RELEASE_YEAR + " INTEGER, " +
                FavListContract.FavListEntry.COLUMN_MOVIE_USER_RATING + " INTEGER, " +
                FavListContract.FavListEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                " UNIQUE (" + FavListContract.FavListEntry.COULUMN_MOVIE_TITLE + ") ON CONFLICT REPLACE" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAV_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavListContract.FavListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
