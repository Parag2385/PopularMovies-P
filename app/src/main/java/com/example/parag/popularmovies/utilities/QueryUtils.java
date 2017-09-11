package com.example.parag.popularmovies.utilities;


import android.text.TextUtils;
import android.util.Log;
import com.example.parag.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by parag on 03-09-2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public final class QueryUtils {

    public static List<Movie> fetchMovieData(String requestURL){

        URL url = createUrl(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFeatureFromJSON(jsonResponse);
    }



    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;

        if (url == null){
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.v("QueryUtils"," "+urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder outputStream = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                outputStream.append(line);
                line = reader.readLine();
            }
        }
        return outputStream.toString();
    }

    private static URL createUrl(String movieRequestURL) {
        URL mUrl;

        try {
            mUrl = new URL(movieRequestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return mUrl;
    }

    private static ArrayList<Movie> extractFeatureFromJSON(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        String photoURL;
        String movieTitle;
        String movieOverview;
        String movieReleaseDate;
        String movieVote;
        String movieVoteCount;
        String movieLanguage;

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = baseJsonObject.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++){
                JSONObject currentMovie = resultArray.getJSONObject(i);
                movieTitle = currentMovie.getString("title");
                movieOverview = currentMovie.getString("overview");
                movieReleaseDate = currentMovie.getString("release_date");
                movieVote = currentMovie.getString("vote_average");
                movieVoteCount = currentMovie.getString("vote_count");
                movieLanguage = currentMovie.getString("original_language");
                photoURL = currentMovie.getString("poster_path");

                movies.add(new Movie(photoURL,movieTitle,movieOverview, movieReleaseDate, movieVote, movieVoteCount, movieLanguage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }


}
