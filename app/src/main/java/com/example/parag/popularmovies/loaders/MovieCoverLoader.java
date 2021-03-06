package com.example.parag.popularmovies.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.parag.popularmovies.utilities.QueryUtils;

/**
 * Created by parag on 04-10-2017.
 */

public class MovieCoverLoader extends AsyncTaskLoader {

    private final String mUrl;

    public MovieCoverLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if (mUrl == null){
            return null;
        }
        return QueryUtils.fetchMovieImage(mUrl);
    }
}
