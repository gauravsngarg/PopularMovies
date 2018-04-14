package gauravsngarg.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by GG on 14/04/18.
 */

public class NetworkUtils {

    final static String THE_MOVIE_DB_API_URL
            = "https://api.themoviedb.org/3/movie/popular";

    final static String PARAM_API_KEY = "api_key";

    private static String MOVIE_API_KEY = null;

    final static String PARAM_Language = "language";

    final static String language = "en-US";

    final static String PARAM_PAGE = "page";

     static String page = "1";

    public static URL buildUrl(String api_key, int mPage) {
        MOVIE_API_KEY = api_key;
        page = mPage +"";
        Uri builtURI = Uri.parse(THE_MOVIE_DB_API_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, MOVIE_API_KEY)
                .appendQueryParameter(PARAM_Language, language)
                .appendQueryParameter(PARAM_PAGE, mPage+"")
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
