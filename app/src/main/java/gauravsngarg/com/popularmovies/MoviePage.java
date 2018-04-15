package gauravsngarg.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import gauravsngarg.com.popularmovies.utils.GenerateMovieThumbnailsURL;

public class MoviePage extends AppCompatActivity {

    TextView tv_title;
    TextView tv_overview;
    TextView tv_user_rating;
    TextView tv_release_date;

    ImageView iv_movieposter;



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

        tv_title.setText(extras.getString("title"));
        tv_overview.setText(extras.getString("overview"));
        tv_user_rating.setText(extras.getString("rating"));
        tv_release_date.setText(extras.getString("release_date"));

        String url1 = extras.getString("url");
        URL url = GenerateMovieThumbnailsURL.buildURL(url1);

        Picasso.with(this).load(url.toString()).into(iv_movieposter);
    }
}
