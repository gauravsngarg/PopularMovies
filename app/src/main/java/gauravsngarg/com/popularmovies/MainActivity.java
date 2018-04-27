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

    private static Boolean MENU_FLAG_MOST_POPULAR = false;
    private static Boolean MENU_FLAG_HIGH_RATED = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


            //ToDo Add Progress Bar Start
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
                //ToDo Add View to update after Download ProgressBar Remove
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
                    adapter = new MovieAdapter(list.size(), list, MainActivity.this);
                    mMoviesList.setAdapter(adapter);
                }

                mProgressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moviesort, menu);
        menu.getItem(0).setVisible(MENU_FLAG_MOST_POPULAR);
        menu.getItem(1).setVisible(MENU_FLAG_HIGH_RATED);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.getItem(0).setVisible(MENU_FLAG_MOST_POPULAR);
        menu.getItem(1).setVisible(MENU_FLAG_HIGH_RATED);


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sortby_highrated) {
            sortByHighRated(item);
            adapter = new MovieAdapter(list.size(), list, MainActivity.this);
            mMoviesList.setAdapter(adapter);

            MENU_FLAG_HIGH_RATED = false;
            MENU_FLAG_MOST_POPULAR = true;

            return true;
        } else if (item.getItemId() == R.id.action_sortby_mostpopular) {
            sortByMostPopular(item);
            adapter = new MovieAdapter(list.size(), list, MainActivity.this);
            mMoviesList.setAdapter(adapter);

            MENU_FLAG_HIGH_RATED = true;
            MENU_FLAG_MOST_POPULAR = false;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByHighRated(MenuItem item) {
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

    private void sortByMostPopular(MenuItem item) {
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
