package gauravsngarg.com.popularmovies.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;


public class GenerateMovieThumbnailsURL {

    private final static String BASE_MOVIE_THUMBNAIL_URL = "https://image.tmdb.org/t/p/";

    private final static String imageQuality = "w342";

    public static URL buildURL(String imageLink) {

        Uri builtURI = Uri.parse(BASE_MOVIE_THUMBNAIL_URL).buildUpon()
                .appendPath(imageQuality)
                .appendEncodedPath(imageLink)
                .build();


        URL url = null;
        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }
}
