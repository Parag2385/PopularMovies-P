package com.example.parag.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parag.popularmovies.models.MovieReview;
import com.example.parag.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by parag on 08-10-2017.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MovieReview> movieReviews;

    public MovieReviewAdapter(Context mContext, ArrayList<MovieReview> movieReviews) {
        this.mContext = mContext;
        this.movieReviews = movieReviews;
    }

    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.review_list,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.ViewHolder holder, int position) {
        String author = movieReviews.get(position).getmAuthor();
        String review = movieReviews.get(position).getmReview();

        holder.mAuthorTextView.setText(author);
        holder.mReviewTextView.setText(review);
    }

    @Override
    public int getItemCount() {
        return movieReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthorTextView;
        private TextView mReviewTextView;
        ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.author_name);
            mReviewTextView = (TextView) itemView.findViewById(R.id.review);
        }
    }
}
