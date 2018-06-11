package gauravsngarg.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movielist.db";
    private static final int DATABASE_VERSION = 1;

    Context mContext;

    public FavDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_FAV_LIST_TABLE = "" +
                "CREATE TABLE " +
                FavContract.FavEntry.TABLE_NAME + "( " +
                FavContract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavContract.FavEntry.COLUMN_MOVIE_ID + " TEXT, " +
                FavContract.FavEntry.COULUMN_MOVIE_TITLE + " TEXT, " +
                FavContract.FavEntry.COLUMN_MOVIE_PLOT_SYNOPSIS + " TEXT, " +
                FavContract.FavEntry.COLUMN_MOVIE_RELEASE_DATE + " DATE, " +
                FavContract.FavEntry.COLUMN_MOVIE_USER_RATING + " INTEGER, " +
                FavContract.FavEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
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
