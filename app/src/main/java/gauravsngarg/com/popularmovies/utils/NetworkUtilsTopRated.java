package gauravsngarg.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by gaurav.g4 on 29-04-2018.
 */

public class NetworkUtilsTopRated {

    private final static String THE_MOVIE_DB_API_URL
            = "https://api.themoviedb.org/3/movie/top_rated";

    private final static String PARAM_API_KEY = "api_key";

    private final static String PARAM_Language = "language";

    private final static String language = "en-US";

    private final static String PARAM_PAGE = "page";

    public static URL buildUrl(String api_key, int mPage) {
        String MOVIE_API_KEY = api_key;
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
