package com.example.parag.popularmovies.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.parag.popularmovies.utilities.QueryUtils;

/**
 * Created by parag on 08-10-2017.
 */

public class MovieReviewLoader extends AsyncTaskLoader {

    private final String mUrl;
    public MovieReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchMovieReviews(mUrl);
    }
}
