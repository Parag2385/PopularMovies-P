package com.example.parag.popularmovies.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.parag.popularmovies.DetailActivity;
import com.example.parag.popularmovies.Movie;
import com.example.parag.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by parag on 03-09-2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private static final int MOVIE_RESULT_CODE = 1;
    private final ArrayList<Movie> moviesList;
    private final Context mContext;

    public MovieAdapter(Context mContext, ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        this.mContext = mContext;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(IMAGE_URL + moviesList.get(position).getmMoviePoster())
                .into(holder.movieImage);

        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie movieSelected = moviesList.get(position);
                Bundle movieBundle = new Bundle();
                movieBundle.putParcelable("movie",movieSelected);

                Intent movieIntent = new Intent(mContext, DetailActivity.class);
                movieIntent.putExtras(movieBundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, v, "movie_poster");

                ((Activity) mContext).startActivityForResult(movieIntent,MOVIE_RESULT_CODE, options.toBundle());

            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.img_movies);
        }

    }
}
