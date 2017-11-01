package com.example.parag.popularmovies.loaders;

import android.content.Context;
import android.content.AsyncTaskLoader;

import com.example.parag.popularmovies.utilities.QueryUtils;

/**
 * Created by parag on 03-09-2017.
 */

public class MovieLoader extends AsyncTaskLoader {

    private final String mUrl;

    public MovieLoader(Context context, String url) {
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
        return QueryUtils.fetchMovieData(mUrl);
    }
}
