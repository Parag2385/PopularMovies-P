package com.example.parag.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by parag on 08-10-2017.
 */

public class MovieReview implements Parcelable {

    @Json(name = "author")
    private String mAuthor;

    @Json(name = "content")
    private String mReview;

    public MovieReview(String mAuthor, String mReview) {
        this.mAuthor = mAuthor;
        this.mReview = mReview;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmReview() {
        return mReview;
    }

    private MovieReview(Parcel in) {
        mAuthor = in.readString();
        mReview = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mReview);
    }
}
