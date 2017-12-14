package com.example.parag.popularmovies.models;

import com.squareup.moshi.Json;

/**
 * Created by parag on 04-10-2017.
 */

public class MovieCoverImage {

    @Json(name = "file_path")
    private String mMovieCoverImage;

    public MovieCoverImage(String imageUrl){
        this.mMovieCoverImage = imageUrl;
    }


    public String getmMovieCoverImage() {
        return mMovieCoverImage;
    }
}
