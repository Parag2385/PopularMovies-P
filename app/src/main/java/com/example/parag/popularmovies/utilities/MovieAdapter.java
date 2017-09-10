package com.example.parag.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private ArrayList<Movie> moviesList;
    private Context mContext;

    public MovieAdapter(Context mContext,  ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        this.mContext = mContext;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(IMAGE_URL + moviesList.get(position).getmMoviePoster())
                .into(holder.movieImage);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView)itemView.findViewById(R.id.img_movies);
        }
    }

}
