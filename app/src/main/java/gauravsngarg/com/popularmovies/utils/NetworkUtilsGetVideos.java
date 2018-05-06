package gauravsngarg.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


//https://api.themoviedb.org/3/movie/299536/videos?api_key=key&language=en-US

public class NetworkUtilsGetVideos {
    private final static String THE_MOVIE_DB_API_URL
            = "https://api.themoviedb.org/3/movie/";

    private final static String PARAM_API_KEY = "api_key";

    private final static String PARAM_Language = "language";

    private final static String language = "en-US";

    private final static String PARAM_VIDEOS = "videos";

    public static URL buildUrl(String id, String api_key) {
        String MOVIE_API_KEY = api_key;
        Uri builtURI = Uri.parse(THE_MOVIE_DB_API_URL).buildUpon()
                .appendPath(id)
                .appendPath(PARAM_VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_API_KEY)
                .appendQueryParameter(PARAM_Language, language)
                .build();


        URL url = null;
        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
