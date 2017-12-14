package com.example.parag.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by parag on 04-10-2017.
 */

public class MovieTrailer implements Parcelable{

    @Json(name = "name")
    private String mMovieTrailerTitle;

    @Json(name = "key")
    private String mMovieTrailerKey;

    public MovieTrailer(String mMovieTrailerTitle, String mMovieTrailerKey) {
        this.mMovieTrailerTitle = mMovieTrailerTitle;
        this.mMovieTrailerKey = mMovieTrailerKey;
    }

    private MovieTrailer(Parcel in) {
        mMovieTrailerTitle = in.readString();
        mMovieTrailerKey = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getmMovieTrailerTitle() {
        return mMovieTrailerTitle;
    }

    public String getmMovieTrailerKey() {
        return mMovieTrailerKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieTrailerTitle);
        dest.writeString(mMovieTrailerKey);
    }
}
