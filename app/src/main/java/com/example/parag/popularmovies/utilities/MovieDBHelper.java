package com.example.parag.popularmovies.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.parag.popularmovies.utilities.MovieContract.MovieEntry;

/**
 * Created by parag on 09-10-2017.
 */

class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_OVERVIEW + " TEXT, " +
                MovieEntry.MOVIE_LANGUAGE + " TEXT, " +
                MovieEntry.MOVIE_RELEASE_DATE + " TEXT, " +
                MovieEntry.MOVIE_VOTE_PERCENTAGE + " REAL, " +
                MovieEntry.MOVIE_VOTE_COUNT + " INTEGER, " +
                MovieEntry.MOVIE_POSTER + " TEXT, " +
                MovieEntry.MOVIE_COVER + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
