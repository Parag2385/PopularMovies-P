package com.example.parag.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by parag on 03-09-2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Movie implements Parcelable{

    @Json(name = "id")
    private int mMovieId;

    @Json(name = "poster_path")
    private String mMoviePoster;

    @Json(name = "title")
    private String mMovieTitle;

    @Json(name = "overview")
    private String mMovieOverview;

    @Json(name = "release_date")
    private String mMovieReleaseDate;

    @Json(name = "vote_average")
    private String mMovieVote;

    @Json(name = "vote_count")
    private String mMovieVoteCount;

    @Json(name = "original_language")
    private String mMovieLanguage;

    public Movie(int id, String moviePoster, String movieTitle, String movieOverview, String movieReleaseDate, String movieVote, String movieVoteCount, String movieLanguage) {
        this.mMovieId = id;
        this.mMoviePoster = moviePoster;
        this.mMovieTitle = movieTitle;
        this.mMovieOverview = movieOverview;
        this.mMovieReleaseDate = movieReleaseDate;
        this.mMovieVote = movieVote;
        this.mMovieVoteCount = movieVoteCount;
        this.mMovieLanguage = movieLanguage;
    }

    private Movie(Parcel in) {
        mMoviePoster = in.readString();
        mMovieTitle = in.readString();
        mMovieOverview = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieVote = in.readString();
        mMovieVoteCount = in.readString();
        mMovieLanguage = in.readString();
        mMovieId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getmMovieVote() {
        return mMovieVote;
    }

    public String getmMovieVoteCount() {
        return mMovieVoteCount;
    }

    public String getmMovieLanguage() {
        return mMovieLanguage;
    }

    public int getmMovieId(){return mMovieId;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMoviePoster);
        dest.writeString(mMovieTitle);
        dest.writeString(mMovieOverview);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mMovieVote);
        dest.writeString(mMovieVoteCount);
        dest.writeString(mMovieLanguage);
        dest.writeInt(mMovieId);
    }
}
