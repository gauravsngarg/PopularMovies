package gauravsngarg.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gauravsngarg.com.popularmovies.adapter.ReviewsListAdapter;
import gauravsngarg.com.popularmovies.adapter.TrailerListAdapter;
import gauravsngarg.com.popularmovies.data.FavContract;
import gauravsngarg.com.popularmovies.data.FavListDbHelper;
import gauravsngarg.com.popularmovies.model.MovieReviews;
import gauravsngarg.com.popularmovies.model.MovieTrailer;
import gauravsngarg.com.popularmovies.utils.GenerateMovieThumbnailsURL;
import gauravsngarg.com.popularmovies.utils.NetworkUtilsGetReviews;
import gauravsngarg.com.popularmovies.utils.NetworkUtilsGetVideos;

public class MoviePage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        TrailerListAdapter.TrailerListitemClickListner {

    private static final String GET_Video_URL_EXTRA = "url";

    private static final int MOVIE_DOWNLOAD_LOADER_ID = 22;
    private static final int REVIEWS_DOWNLOAD_LOADER_ID = 24;

    private TextView tv_releaseyear;
    private TextView tv_overview;
    private TextView tv_user_rating;
    private TextView tv_release_date;
    private Button btn_mark_fav;

    private ImageView iv_movieposter;

    private String movieId;
    private ProgressBar progressBar;
    private List<MovieTrailer> listOftrailers;
    private RecyclerView recyclerView;
    private TrailerListAdapter videosadapter;

    private SQLiteDatabase mDb;
    private FavListDbHelper dbHelper;

    private URL url_poster;
    private String smal_poster_path;

    private RecyclerView review_recycler_view;
    private List<MovieReviews> listOfReviews;
    private ReviewsListAdapter reviewAdapter;
    private View divider_trailer_list;
    private View divider_overview;
    private boolean favFLag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_page);

        dbHelper = new FavListDbHelper(this);


        Bundle extras = getIntent().getExtras();

        listOftrailers = new ArrayList<>();
        listOfReviews = new ArrayList<>();

        tv_releaseyear = (TextView) findViewById(R.id.tv_releaseyear);
        tv_overview = (TextView) findViewById(R.id.movieoverview);
        tv_user_rating = (TextView) findViewById(R.id.user_rating);
        btn_mark_fav = (Button) findViewById(R.id.btn_mark_fav);

        iv_movieposter = (ImageView) findViewById(R.id.iv_movieposter);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        divider_trailer_list = (View) findViewById(R.id.trailerlist_divider);
        divider_overview = (View) findViewById(R.id.overview_divider);

        review_recycler_view = (RecyclerView) findViewById(R.id.review_recycler_view);

        movieId = extras.getString((getString(R.string.movie_id)));
        tv_overview.setText(extras.getString(getString(R.string.tv_overview)));
        tv_user_rating.setText(extras.getString(getString(R.string.tv_user_rating)) + "/10");

        favFLag = extras.getBoolean((getString(R.string.favflag)));
        if(favFLag){
            btn_mark_fav.setVisibility(View.INVISIBLE);
        }

       String[] result = (extras.getString(getString(R.string.tv_release_date))).split("-");
       tv_releaseyear.setText(result[0]);

       final String movie_title = extras.getString(getString(R.string.tv_title));
        getSupportActionBar().setTitle(movie_title);

        btn_mark_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(FavContract.FavEntry.COLUMN_MOVIE_ID, movieId);
                cv.put(FavContract.FavEntry.COULUMN_MOVIE_TITLE, movie_title);
                cv.put(FavContract.FavEntry.COLUMN_MOVIE_PLOT_SYNOPSIS, tv_overview.getText().toString());
                cv.put(FavContract.FavEntry.COLUMN_MOVIE_RELEASE_YEAR, tv_releaseyear.getText().toString());
                cv.put(FavContract.FavEntry.COLUMN_MOVIE_USER_RATING, tv_user_rating.getText().toString());
                cv.put(FavContract.FavEntry.COLUMN_MOVIE_POSTER_PATH, smal_poster_path.toString());


                Uri uri = getContentResolver().insert(FavContract.FavEntry.CONTENT_URI, cv);

            }
        });

        smal_poster_path = extras.getString(getString(R.string.movie_url));

        url_poster = GenerateMovieThumbnailsURL.buildURL(smal_poster_path);

        Picasso.with(this).load(url_poster.toString()).into(iv_movieposter);

        URL videoURL = NetworkUtilsGetVideos.buildUrl(movieId, getString(R.string.api_key));
        Bundle queryVideosBundle = new Bundle();
        queryVideosBundle.putString(GET_Video_URL_EXTRA, videoURL.toString());

        URL reviewsURL = NetworkUtilsGetReviews.buildUrl(movieId, getString(R.string.api_key));
        Bundle queryReviewsBundle = new Bundle();
        queryReviewsBundle.putString(GET_Video_URL_EXTRA, reviewsURL.toString());


        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIE_DOWNLOAD_LOADER_ID, queryVideosBundle, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        loaderManager.initLoader(REVIEWS_DOWNLOAD_LOADER_ID, queryReviewsBundle, this);
        LinearLayoutManager layoutManager_rev = new LinearLayoutManager((this));
        review_recycler_view.setLayoutManager(layoutManager_rev);
        review_recycler_view.setHasFixedSize(true);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {

        if (id == MOVIE_DOWNLOAD_LOADER_ID) {
            return new AsyncTaskLoader<String>(this) {
                String mJson;

                @Override
                protected void onStartLoading() {


                    if (args == null)
                        return;
                        progressBar.setVisibility(View.VISIBLE);
                    if (mJson != null)
                        deliverResult(mJson);
                    else
                        forceLoad();

                }

                @Override
                public String loadInBackground() {

                    String downloadVideoURL = args.getString(GET_Video_URL_EXTRA);

                    if (downloadVideoURL == null || TextUtils.isEmpty(downloadVideoURL)) {
                        return null;
                    }

                    try {
                        URL video_url = new URL(downloadVideoURL);
                        //URL reviews_url = new URL(downloadVideoURL);
                        String movieVideosResult = NetworkUtilsGetVideos.getResponseFromHttpUrl(video_url);
                        // String reviewsResult = NetworkUtilsGetReviews.getResponseFromHttpUrl(reviews_url);

                        return movieVideosResult;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(String data) {
                    mJson = data;
                    super.deliverResult(data);
                }
            };

        } else if (id == REVIEWS_DOWNLOAD_LOADER_ID) {
            return new AsyncTaskLoader<String>(this) {
                String mJson;

                @Override
                protected void onStartLoading() {


                    if (args == null)
                        return;
                        progressBar.setVisibility(View.VISIBLE);
                    if (mJson != null)
                        deliverResult(mJson);
                    else
                        forceLoad();

                }

                @Override
                public String loadInBackground() {

                    String downloadVideoURL = args.getString(GET_Video_URL_EXTRA);

                    if (downloadVideoURL == null || TextUtils.isEmpty(downloadVideoURL)) {
                        return null;
                    }

                    try {
                        URL reviews_url = new URL(downloadVideoURL);
                        String reviewsResult = NetworkUtilsGetReviews.getResponseFromHttpUrl(reviews_url);

                        return reviewsResult;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(String data) {
                    mJson = data;

                    super.deliverResult(data);

                }
            };

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if (data == null) {

        } else if (loader.getId() == MOVIE_DOWNLOAD_LOADER_ID) {
            listOftrailers.clear();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MovieTrailer item = new MovieTrailer();
                    JSONObject js = jsonArray.getJSONObject(i);
                    item.setVideoID(js.getString("id"));
                    item.setVideoName(js.getString("name"));
                    item.setVideoKey(js.getString("key"));
                    item.setVideoSite(js.getString("site"));
                    item.setVideoType(js.getString("type"));

                    String name = item.getVideoName();
                    if (name.contains("Official Trailer") || name.startsWith("Official"))
                        listOftrailers.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            videosadapter = new TrailerListAdapter(listOftrailers.size(), listOftrailers, MoviePage.this);
            recyclerView.setAdapter(videosadapter);
            if(listOftrailers.size()!= 0)
            divider_overview.setVisibility(View.VISIBLE);
        } else if (loader.getId() == REVIEWS_DOWNLOAD_LOADER_ID) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length()  && i < 4 ; i++) {
                    MovieReviews item = new MovieReviews();
                    JSONObject js = jsonArray.getJSONObject(i);
                    item.setAuthor(js.getString("author"));
                    item.setContent(js.getString("content"));
                    item.setUrl(js.getString("url"));
                    item.setId(js.getString("id"));
                    listOfReviews.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            reviewAdapter = new ReviewsListAdapter(listOfReviews.size(), listOfReviews);
            review_recycler_view.setAdapter(reviewAdapter);
            divider_trailer_list.setVisibility(View.VISIBLE);

        }

           progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onTrailerListItemClick(int clickedItemIndex) {

        MovieTrailer item = listOftrailers.get(clickedItemIndex);
        String key = item.getVideoKey();
        String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + key));
        startActivity(browserIntent);
    }
}
