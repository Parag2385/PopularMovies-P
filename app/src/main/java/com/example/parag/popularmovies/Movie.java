package com.example.parag.popularmovies;

/**
 * Created by parag on 03-09-2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Movie {

    private String mMoviePoster;
    private String mMovieTitle;
    private String mMovieOverview;
    private String mMovieReleaseDate;
    private String mMovieVote;
    private String mMovieVoteCount;
    private String mMovieLanguage;

    public Movie(String moviePoster, String movieTitle, String movieOverview, String movieReleaseDate, String movieVote, String movieVoteCount, String movieLanguage) {
        this.mMoviePoster = moviePoster;
        this.mMovieTitle = movieTitle;
        this.mMovieOverview = movieOverview;
        this.mMovieReleaseDate = movieReleaseDate;
        this.mMovieVote = movieVote;
        this.mMovieVoteCount = movieVoteCount;
        this.mMovieLanguage = movieLanguage;
    }
    public Movie(){

    }

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
}
