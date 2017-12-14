package com.example.parag.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.parag.popularmovies.R;
import com.example.parag.popularmovies.activities.DetailActivity;
import com.example.parag.popularmovies.utilities.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by parag on 13-10-2017.
 */

public class DBCursorAdapter extends RecyclerView.Adapter<DBCursorAdapter.MovieViewHolder> {

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private Cursor mCursor;
    private Context mContext;

    public DBCursorAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.mCursor = cursor;
    }


    @Override
    public DBCursorAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_list, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DBCursorAdapter.MovieViewHolder holder, final int position) {
        if (mCursor != null) {
            final int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int movieActualIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);
            int posterIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER);
            mCursor.moveToPosition(position);

            final String posterUrl = mCursor.getString(posterIndex);
            final int movieId = mCursor.getInt(idIndex);
            final int movieActualID = mCursor.getInt(movieActualIdIndex);

            Picasso.with(mContext)
                    .load(IMAGE_URL + posterUrl)
                    .into(holder.movieImage);

            holder.movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent movieIntent = new Intent(mContext, DetailActivity.class);
                    movieIntent.putExtra("id", movieId);
                    movieIntent.putExtra("actualId", movieActualID);
                    movieIntent.putExtra("Intent", "From DBCursorAdapter");
                    movieIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(movieIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView movieImage;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.img_movies);
        }
    }
}
