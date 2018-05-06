package gauravsngarg.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import gauravsngarg.com.popularmovies.utils.GenerateMovieThumbnailsURL;

public class MoviePage extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_overview;
    private TextView tv_user_rating;
    private TextView tv_release_date;

    private ImageView iv_movieposter;

    private String movieId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        Bundle extras = getIntent().getExtras();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_overview = (TextView) findViewById(R.id.movieoverview);
        tv_user_rating = (TextView) findViewById(R.id.user_rating);
        tv_release_date = (TextView) findViewById(R.id.release_date);

        iv_movieposter = (ImageView) findViewById(R.id.iv_movieposter);

        movieId = extras.getString((getString(R.string.movie_id)));
        tv_title.setText(extras.getString(getString(R.string.tv_title)));
        tv_overview.setText(extras.getString(getString(R.string.tv_overview)));
        tv_user_rating.setText(extras.getString(getString(R.string.tv_user_rating)));
        tv_release_date.setText(extras.getString(getString(R.string.tv_release_date)));

        URL url = GenerateMovieThumbnailsURL.buildURL(extras.getString(getString(R.string.movie_url)));

        Picasso.with(this).load(url.toString()).into(iv_movieposter);
    }
}
