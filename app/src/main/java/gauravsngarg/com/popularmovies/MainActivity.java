package gauravsngarg.com.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gauravsngarg.com.popularmovies.adapter.MovieAdapter;
import gauravsngarg.com.popularmovies.model.MovieItem;
import gauravsngarg.com.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private static int NUMBER_OF_LISTITEMS = 20;

    private RecyclerView mMoviesList;

    private List<MovieItem> list;

    private MovieAdapter adapter;

    //public final String api_key = getApplicationContext().getString(R.string.api_key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        mMoviesList.setLayoutManager(grid);




        mMoviesList.setHasFixedSize(true);

        new ShowMovieListTask().execute(makeSearchQuery());



    }

    public URL makeSearchQuery(){
        URL url =  NetworkUtils.buildUrl(getString(R.string.api_key));
        return url;
    }

    public class ShowMovieListTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

           // Toast.makeText(MainActivity.this, "onPost: JSON" + (json==null), Toast.LENGTH_SHORT).show();
            if(json != null){
                //ToDo Add View to update after Download
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    list = new ArrayList<MovieItem>();
                    for(int i =0 ; i < jsonArray.length(); i++){

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



                   // Toast.makeText(MainActivity.this, list.get(0).getMovieTitle(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!list.isEmpty()) {
                    adapter = new MovieAdapter(list.size(), MainActivity.this, list);
                    mMoviesList.setAdapter(adapter);
                }

            }
        }
    }


}
