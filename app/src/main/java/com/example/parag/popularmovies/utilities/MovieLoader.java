package com.example.parag.popularmovies.utilities;

import android.content.Context;
import android.content.AsyncTaskLoader;

/**
 * Created by parag on 03-09-2017.
 */

@SuppressWarnings("DefaultFileTemplate")
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
