package gauravsngarg.com.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gauravsngarg.com.popularmovies.adapter.MovieAdapter;
import gauravsngarg.com.popularmovies.data.FavContract;
import gauravsngarg.com.popularmovies.data.FavListContract;
import gauravsngarg.com.popularmovies.data.FavListDbHelper;
import gauravsngarg.com.popularmovies.model.MovieItem;
import gauravsngarg.com.popularmovies.utils.NetworkUtils;
import gauravsngarg.com.popularmovies.utils.NetworkUtilsTopRated;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListitemClickListner {

    private RecyclerView mMoviesList;
    private List<MovieItem> list;
    private MovieAdapter adapter;

    private ProgressBar mProgressBar;

    private boolean MENU_MOST_POPULAR_VALUE = false;
    private boolean MENU_HIGH_RATED_VALUE = true;
    private boolean MENU_SHOW_FAV_VALUE = true;

    private String MENU_MOST_POPULAR_KEY;
    private String MENU_HIGH_RATED_KEY;
    private String MENU_SHOW_FAV_KEY;

    private static final String API_KEY = BuildConfig.API_KEY;

    SQLiteDatabase mDb;
    FavListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FavListDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        MENU_MOST_POPULAR_KEY = getResources().getString(R.string.menu_most_popular_key);
        MENU_HIGH_RATED_KEY = getString(R.string.menu_high_rated_key);
        MENU_SHOW_FAV_KEY = getString(R.string.menu_show_fav_key);

        if (savedInstanceState != null) {
            MENU_MOST_POPULAR_VALUE = Boolean.parseBoolean(savedInstanceState.getString(MENU_MOST_POPULAR_KEY));
            MENU_HIGH_RATED_VALUE = Boolean.parseBoolean(savedInstanceState.getString(MENU_HIGH_RATED_KEY));
            MENU_SHOW_FAV_VALUE = Boolean.parseBoolean(savedInstanceState.getString(MENU_SHOW_FAV_KEY));
        }

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_indicator);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        mMoviesList.setLayoutManager(grid);
        mMoviesList.setHasFixedSize(true);
        list = new ArrayList<MovieItem>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!MENU_SHOW_FAV_VALUE) {
            MENU_HIGH_RATED_VALUE = true;
            MENU_MOST_POPULAR_VALUE = true;
            MENU_SHOW_FAV_VALUE = false;
            new ShowFavMovieListTask().execute();
        } else if (!MENU_MOST_POPULAR_VALUE)
            new ShowMovieListTask().execute(makeSearchQuery(1, 1));
        else if (!MENU_HIGH_RATED_VALUE)
            new ShowMovieListTask().execute(makeSearchQuery(1, 2));

    }

    public URL makeSearchQuery(int page, int order) {
        //API Link:https://developers.themoviedb.org/3/movies/get-popular-movies

        URL url = null;
        if (order == 1)
            url = NetworkUtils.buildUrl(API_KEY, page);
        else if (order == 2)
            url = NetworkUtilsTopRated.buildUrl(API_KEY, page);

        return url;
    }

    @Override
    public void onMovieListItemClick(int clickedItemIndex) {
        String movieClicked = list.get(clickedItemIndex).getMovieTitle();
        MovieItem itemClicked = list.get(clickedItemIndex);

        Intent i = new Intent(this, MoviePage.class);
        i.putExtra("id", itemClicked.getMovieId());
        i.putExtra("title", itemClicked.getMovieTitle());
        i.putExtra("overview", itemClicked.getMoviePlotSynopsis());
        i.putExtra("rating", itemClicked.getMovieUserRating());
        i.putExtra("release_date", itemClicked.getMovieReleaseDate());
        i.putExtra("url", itemClicked.getMoviePosterPath());
       // i.putExtra("favflag", !MENU_SHOW_FAV_VALUE);

        Log.d("Gaurav31", "Movie ID: " + itemClicked.getMovieId());

        startActivity(i);
    }

    public class ShowMovieListTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchURL = params[0];

            String movieList = null;

            try {
                if (MENU_MOST_POPULAR_VALUE)
                    movieList = NetworkUtils.getResponseFromHttpUrl(searchURL);
                else if (MENU_HIGH_RATED_VALUE)
                    movieList = NetworkUtilsTopRated.getResponseFromHttpUrl(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(String json) {

            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject js = jsonArray.getJSONObject(i);
                        MovieItem item = new MovieItem();
                        item.setMovieId(js.getString("id"));
                        item.setMovieTitle(js.getString("original_title"));
                        item.setMoviePopularity(js.getString("popularity"));
                        item.setMoviePosterPath(js.getString("poster_path"));
                        item.setMovieReleaseDate(js.getString("release_date"));
                        item.setMovieUserRating(js.getString("vote_average"));
                        item.setMoviePlotSynopsis(js.getString("overview"));

                        list.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!list.isEmpty()) {
                    if (MENU_HIGH_RATED_VALUE) {
                        MENU_MOST_POPULAR_VALUE = false;
                        MENU_HIGH_RATED_VALUE = true;
                    } else if (MENU_MOST_POPULAR_VALUE) {
                        MENU_MOST_POPULAR_VALUE = true;
                        MENU_HIGH_RATED_VALUE = false;
                    }

                    adapter = new MovieAdapter(list.size(), list, MainActivity.this);
                    mMoviesList.setAdapter(adapter);
                }

                MENU_SHOW_FAV_VALUE = true;
                mProgressBar.setVisibility(View.INVISIBLE);

            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "JSON file is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ShowFavMovieListTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {

            Cursor cursor = getContentResolver().query(FavContract.FavEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);


            int i = 0;
            cursor.moveToPosition(0);
            for (i = 0; cursor.moveToPosition(i); i++) {
                MovieItem item = new MovieItem();
                item.setMovieId(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_ID)));
                item.setMovieTitle(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COULUMN_MOVIE_TITLE)));
                item.setMoviePlotSynopsis(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_PLOT_SYNOPSIS)));
                item.setMovieReleaseDate(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_RELEASE_YEAR)));
                item.setMovieUserRating(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_USER_RATING)));
                item.setMoviePosterPath(cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_POSTER_PATH)));


                String mMovieTitle = cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_POSTER_PATH));
                String mID = cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COULUMN_MOVIE_TITLE));
                String mID1 = cursor.getString(cursor.getColumnIndex(FavListContract.FavListEntry.COLUMN_MOVIE_ID));
                Log.d("Gaurav31", mMovieTitle + " " + mID + " " + mID1);
                list.add(item);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new MovieAdapter(list.size(), list, MainActivity.this);
            mMoviesList.setAdapter(adapter);


            MENU_SHOW_FAV_VALUE = false;
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(MENU_HIGH_RATED_KEY, MENU_HIGH_RATED_VALUE + "");
        outState.putString(MENU_MOST_POPULAR_KEY, MENU_MOST_POPULAR_VALUE + "");
        outState.putString(MENU_SHOW_FAV_KEY, MENU_SHOW_FAV_VALUE + "");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moviesort, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(MENU_MOST_POPULAR_VALUE);
        menu.getItem(1).setVisible(MENU_HIGH_RATED_VALUE);
        menu.getItem(2).setVisible(MENU_SHOW_FAV_VALUE);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_sortby_highrated) {

            new ShowMovieListTask().execute(makeSearchQuery(1, 2));
            MENU_HIGH_RATED_VALUE = false;
            MENU_MOST_POPULAR_VALUE = true;
            return true;
        } else if (item.getItemId() == R.id.action_sortby_mostpopular) {
            new ShowMovieListTask().execute(makeSearchQuery(1, 1));

            MENU_HIGH_RATED_VALUE = true;
            MENU_MOST_POPULAR_VALUE = false;
            return true;
        }
        if (item.getItemId() == R.id.action_sortby_fav) {
            MENU_HIGH_RATED_VALUE = true;
            MENU_MOST_POPULAR_VALUE = true;
            new ShowFavMovieListTask().execute();


        }


        return super.onOptionsItemSelected(item);
    }
}