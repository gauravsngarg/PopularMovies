package gauravsngarg.com.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gauravsngarg.com.popularmovies.adapter.MovieAdapter;
import gauravsngarg.com.popularmovies.model.MovieItem;
import gauravsngarg.com.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListitemClickListner {

    private RecyclerView mMoviesList;
    private List<MovieItem> list;
    private MovieAdapter adapter;

    private ProgressBar mProgressBar;

    private boolean MENU_MOST_POPULAR_VALUE = false;
    private boolean MENU_HIGH_RATED_VALUE = true;

    private String MENU_MOST_POPULAR_KEY;
    private String MENU_HIGH_RATED_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MENU_MOST_POPULAR_KEY =getResources().getString(R.string.menu_most_popular_key);
        MENU_HIGH_RATED_KEY = getString(R.string.menu_high_rated_key);

        if(savedInstanceState != null){
            MENU_MOST_POPULAR_VALUE = Boolean.parseBoolean(savedInstanceState.getString(MENU_MOST_POPULAR_KEY));
            MENU_HIGH_RATED_VALUE = Boolean.parseBoolean(savedInstanceState.getString(MENU_HIGH_RATED_KEY));
        }

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_indicator);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        mMoviesList.setLayoutManager(grid);
        mMoviesList.setHasFixedSize(true);
        list = new ArrayList<MovieItem>();

        new ShowMovieListTask().execute(makeSearchQuery(1));
        // new ShowMovieListTask().execute(makeSearchQuery(2));
        // new ShowMovieListTask().execute(makeSearchQuery(3));


    }

    public URL makeSearchQuery(int page) {
        //API Link:https://developers.themoviedb.org/3/movies/get-popular-movies
        URL url = NetworkUtils.buildUrl(getString(R.string.api_key), page);
        return url;
    }

    @Override
    public void onMovieListItemClick(int clickedItemIndex) {
        String movieClicked = list.get(clickedItemIndex).getMovieTitle();
        MovieItem itemClicked = list.get(clickedItemIndex);

        Intent i = new Intent(this, MoviePage.class);
        i.putExtra("title", itemClicked.getMovieTitle());
        i.putExtra("overview", itemClicked.getMoviePlotSynopsis());
        i.putExtra("rating", itemClicked.getMovieUserRating());
        i.putExtra("release_date", itemClicked.getMovieReleaseDate());
        i.putExtra("url", itemClicked.getMoviePosterPath());

        startActivity(i);
    }

    public class ShowMovieListTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchURL = params[0];

            String movieList = null;

            try {
                movieList = NetworkUtils.getResponseFromHttpUrl(searchURL);
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
                        sortByMostPopular();
                        MENU_MOST_POPULAR_VALUE = false;
                        MENU_HIGH_RATED_VALUE = true;
                    } else if (MENU_MOST_POPULAR_VALUE) {
                        sortByHighRated();
                        MENU_MOST_POPULAR_VALUE = true;
                        MENU_HIGH_RATED_VALUE = false;
                    }

                    adapter = new MovieAdapter(list.size(), list, MainActivity.this);
                    mMoviesList.setAdapter(adapter);
                }

                mProgressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(MENU_HIGH_RATED_KEY, MENU_HIGH_RATED_VALUE +"");
        outState.putString(MENU_MOST_POPULAR_KEY, MENU_MOST_POPULAR_VALUE + "");
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
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_sortby_highrated) {
            sortByHighRated();
            adapter = new MovieAdapter(list.size(), list, MainActivity.this);
            mMoviesList.setAdapter(adapter);

            MENU_HIGH_RATED_VALUE = false;
            MENU_MOST_POPULAR_VALUE = true;
            return true;
        } else if (item.getItemId() == R.id.action_sortby_mostpopular) {
            sortByMostPopular();
            adapter = new MovieAdapter(list.size(), list, MainActivity.this);
            mMoviesList.setAdapter(adapter);

            MENU_HIGH_RATED_VALUE = true;
            MENU_MOST_POPULAR_VALUE = false;
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void sortByHighRated() {
        Collections.sort(list, new Comparator<MovieItem>() {
            @Override
            public int compare(MovieItem o1, MovieItem o2) {
                if (Float.parseFloat(o1.getMovieUserRating()) > Float.parseFloat(o2.getMovieUserRating()))
                    return -1;
                else if (Float.parseFloat(o1.getMovieUserRating()) < Float.parseFloat(o2.getMovieUserRating()))
                    return 1;
                else
                    return 0;
            }
        });
    }

    private void sortByMostPopular() {
        Collections.sort(list, new Comparator<MovieItem>() {
            @Override
            public int compare(MovieItem o1, MovieItem o2) {
                if (Float.parseFloat(o1.getMoviePopularity()) > Float.parseFloat(o2.getMoviePopularity()))
                    return -1;
                else if (Float.parseFloat(o1.getMoviePopularity()) < Float.parseFloat(o2.getMoviePopularity()))
                    return 1;
                else
                    return 0;
            }
        });
    }
}
