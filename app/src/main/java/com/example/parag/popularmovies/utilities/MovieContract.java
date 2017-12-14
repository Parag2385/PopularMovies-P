package com.example.parag.popularmovies.utilities;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by parag on 09-10-2017.
 */

public class MovieContract {

    static final String CONTENT_AUTHORITY = "com.example.parag.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_MOVIE = "movielist";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static Uri contentItemUri(String movieID) {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }

        static final String TABLE_NAME = "movielist";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_COVER = "cover";
        public static final String MOVIE_POSTER = "poster";
        public static final String MOVIE_RELEASE_DATE = "date";
        public static final String MOVIE_LANGUAGE = "laguage";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_VOTE_PERCENTAGE = "percentage";
        public static final String MOVIE_VOTE_COUNT = "vote";
    }
}
