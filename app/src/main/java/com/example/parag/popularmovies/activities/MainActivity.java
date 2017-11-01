package com.example.parag.popularmovies.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parag.popularmovies.BuildConfig;
import com.example.parag.popularmovies.R;
import com.example.parag.popularmovies.adapters.DBCursorAdapter;
import com.example.parag.popularmovies.adapters.MovieAdapter;
import com.example.parag.popularmovies.loaders.MovieLoader;
import com.example.parag.popularmovies.models.Movie;
import com.example.parag.popularmovies.utilities.MovieContract;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int MOVIE_LOADER_ID = 1;
    private static final int MOVIE_DATABASE_LOADER_ID = 2;
    private static final String MOVIE_DB_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY = BuildConfig.API_KEY;
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private String sortBy;
    private String sort;
    private ProgressBar progressBar;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mEmptyTextView.setVisibility(View.INVISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null){
            sort = savedInstanceState.getString("sort");
            if (!TextUtils.equals(sort,getString(R.string.settings_order_by_favourite_value))) {
                mMovies = savedInstanceState.getParcelableArrayList("my_list");
                MovieAdapter movieAdapter = new MovieAdapter(this, (ArrayList<Movie>) mMovies);
                mRecyclerView.setAdapter(movieAdapter);
            }else {
                getLoaderManager().initLoader(MOVIE_DATABASE_LOADER_ID, null, databaseLoader);
                setTitle("Favorite Movies");
                Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("scroll");
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("my_list", (ArrayList<Movie>) mMovies);
        savedInstanceState.putString("sort", sortBy);
        savedInstanceState.putParcelable("scroll", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void loadData() {
        if (isNetworkAvailable()) {
            LoaderManager loaderManager = getLoaderManager();
            if (sortBy.equals(getString(R.string.settings_order_by_favourite_value))) {
                loaderManager.initLoader(MOVIE_DATABASE_LOADER_ID, null, databaseLoader);
                setTitle("Favorite Movies");

            } else if (sortBy.equals(getString(R.string.settings_order_by_popular_value))) {
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this).forceLoad();
                setTitle("Popular Movies");

            } else {
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this).forceLoad();
                setTitle("Top Rated Movies");

            }
        } else {

            if (sortBy.equals(getString(R.string.settings_order_by_popular_value)) ||
                    sortBy.equals(getString(R.string.settings_order_by_rating_values))) {
                toastMessage();
            }
            progressBar.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_DATABASE_LOADER_ID, null, databaseLoader);
            setTitle("Favorite Movies");
        }
    }


    @Override
    protected void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        if (!TextUtils.equals(sort, sortBy)) {
            loadData();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void toastMessage() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_textview);
        text.setText("There is no Internet connection," + "\n" + " so showing Favorites instead!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, actionBarHeight);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(MOVIE_DB_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(sortBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMovies = movies;
        progressBar.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.movie_not_available);

        if (mMovies != null && !mMovies.isEmpty()) {
            MovieAdapter movieAdapter = new MovieAdapter(this, (ArrayList<Movie>) mMovies);
            mRecyclerView.setAdapter(movieAdapter);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.settings_menu) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private LoaderManager.LoaderCallbacks<Cursor> databaseLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(getBaseContext()) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public Cursor loadInBackground() {
                    try {
                        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                    } catch (Exception e) {
                        Log.e("DetailActivity", "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() != 0) {
                progressBar.setVisibility(View.GONE);
                mEmptyTextView.setVisibility(View.GONE);
                DBCursorAdapter dbCursorAdapter = new DBCursorAdapter(getBaseContext(), data);
                mRecyclerView.setAdapter(dbCursorAdapter);
            } else {
                progressBar.setVisibility(View.GONE);
                mEmptyTextView.setVisibility(View.VISIBLE);
                mEmptyTextView.setText(R.string.no_favorites);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            getLoaderManager().restartLoader(MOVIE_DATABASE_LOADER_ID, null, this);
        }
    };

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
