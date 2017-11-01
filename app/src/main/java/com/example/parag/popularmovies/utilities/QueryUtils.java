package com.example.parag.popularmovies.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.parag.popularmovies.models.Movie;
import com.example.parag.popularmovies.models.MovieCoverImage;
import com.example.parag.popularmovies.models.MovieReview;
import com.example.parag.popularmovies.models.MovieTrailer;
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

    public static List<Movie> fetchMovieData(String requestURL) {

        URL url = createUrl(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFeatureFromJSON(jsonResponse);
    }

    public static List<MovieTrailer> fetchMovieTrailerData(String requestURL) {
        URL url = createUrl(requestURL);

        String jsonResponseForTrailer = null;
        try {
            jsonResponseForTrailer = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractFeatureFromJsonForTrailer(jsonResponseForTrailer);
    }

    public static List<MovieCoverImage> fetchMovieImage(String requestURL) {
        URL url = createUrl(requestURL);

        String jsonResponseForTrailer = null;
        try {
            jsonResponseForTrailer = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractFeatureFromJsonForCoverPoster(jsonResponseForTrailer);
    }

    public static List<MovieReview> fetchMovieReviews(String requestURL) {
        URL url = createUrl(requestURL);

        String jsonResponseForReviews = null;
        try {
            jsonResponseForReviews = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFeatureFromJSONForReviews(jsonResponseForReviews);
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;

        if (url == null) {
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
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v("QueryUtils", " " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder outputStream = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
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

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        String photoURL;
        int movieId;
        String movieTitle;
        String movieOverview;
        String movieReleaseDate;
        String movieVote;
        String movieVoteCount;
        String movieLanguage;

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = baseJsonObject.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentMovie = resultArray.getJSONObject(i);
                movieId = currentMovie.getInt("id");
                movieTitle = currentMovie.getString("title");
                movieOverview = currentMovie.getString("overview");
                movieReleaseDate = currentMovie.getString("release_date");
                movieVote = currentMovie.getString("vote_average");
                movieVoteCount = currentMovie.getString("vote_count");
                movieLanguage = currentMovie.getString("original_language");
                photoURL = currentMovie.getString("poster_path");

                movies.add(new Movie(movieId, photoURL, movieTitle, movieOverview, movieReleaseDate, movieVote, movieVoteCount, movieLanguage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static ArrayList<MovieTrailer> extractFeatureFromJsonForTrailer(String jsonResponse) {

        ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
        String trailerTitle;
        String trailerKey;

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = baseJsonObject.getJSONArray("results");
            for (int i = 0; i < 3; i++) {
                //Trailer limited to 3 only.
                JSONObject currentMovieTrailer = resultArray.getJSONObject(i);
                trailerTitle = currentMovieTrailer.getString("name");
                trailerKey = currentMovieTrailer.getString("key");

                movieTrailers.add(new MovieTrailer(trailerTitle, trailerKey));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("QueryUtils","There are no trailers yet");
        }
        return movieTrailers;
    }

    private static ArrayList<MovieCoverImage> extractFeatureFromJsonForCoverPoster(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<MovieCoverImage> movieImage = new ArrayList<>();
        String imageUrl;
        int width;
        int height;
        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = baseJsonObject.getJSONArray("backdrops");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentMovieImage = resultArray.getJSONObject(i);
                width = currentMovieImage.getInt("width");
                height = currentMovieImage.getInt("height");
                imageUrl = currentMovieImage.getString("file_path");
                if ((width == 1920 && height == 1080) || (width >= 1280 && height >= 720)) {
                    movieImage.add(new MovieCoverImage(imageUrl));
                    return movieImage;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<MovieReview> extractFeatureFromJSONForReviews(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<MovieReview> movieReviews = new ArrayList<>();
        String mAuthor;
        String mReview;
        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = baseJsonObject.getJSONArray("results");
            for (int i = 0; i < 3; i++) {
                JSONObject currentReviewObject = resultArray.getJSONObject(i);
                mAuthor = currentReviewObject.getString("author");
                mReview = currentReviewObject.getString("content");
                movieReviews.add(new MovieReview(mAuthor, mReview));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("QueryUtils","There are no reviews yet");
        }
        return movieReviews;
    }
}
