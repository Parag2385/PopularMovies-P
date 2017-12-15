package com.example.parag.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parag.popularmovies.models.MovieTrailer;
import com.example.parag.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by parag on 04-10-2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private final ArrayList<MovieTrailer> moviesList;
    private final Context mContext;

    public MovieTrailerAdapter(Context context, ArrayList<MovieTrailer> moviesList) {
        this.moviesList = moviesList;
        this.mContext = context;

    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieTrailerAdapter.ViewHolder holder, int position) {

        String movieTrailerTitle = moviesList.get(position).getmMovieTrailerTitle();
        holder.mTextView.setText(movieTrailerTitle);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(VIDEO_URL + moviesList.get(holder.getAdapterPosition()).getmMovieTrailerKey()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTextView;
        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.movie_trailer);
        }
    }
}
