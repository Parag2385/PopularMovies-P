package com.example.parag.popularmovies.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.parag.popularmovies.BuildConfig;
import com.example.parag.popularmovies.R;
import com.example.parag.popularmovies.adapters.MovieReviewAdapter;
import com.example.parag.popularmovies.adapters.MovieTrailerAdapter;
import com.example.parag.popularmovies.loaders.MovieCoverLoader;
import com.example.parag.popularmovies.loaders.MovieReviewLoader;
import com.example.parag.popularmovies.loaders.MovieTrailerLoader;
import com.example.parag.popularmovies.models.Movie;
import com.example.parag.popularmovies.models.MovieCoverImage;
import com.example.parag.popularmovies.models.MovieReview;
import com.example.parag.popularmovies.models.MovieTrailer;
import com.example.parag.popularmovies.utilities.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.parag.popularmovies.R.id.language;
import static com.example.parag.popularmovies.utilities.MovieContract.MovieEntry.contentItemUri;

public class DetailActivity extends AppCompatActivity {
    private static final int MOVIE_TRAILER_LOADER_ID = 1;
    private static final int MOVIE_COVER_IMAGE_LOADER_ID = 2;
    private static final int MOVIE_REVIEW_LOADER_ID = 3;
    private static final int MOVIE_FAVORITE_LOADER_ID = 4;
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String MOVIE_DB_URL = "http://api.themoviedb.org/3/movie";
    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String API_KEY = BuildConfig.API_KEY;
    private Movie movie;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private ImageView coverImageView;

    private ProgressBar pb1;
    private ProgressBar pb2;
    private ProgressBar pb3;
    private TextView NoReviews;
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieVote;
    private TextView mMovieVoteCount;
    private TextView mMovieDate;
    private TextView mMovieLanguage;
    private TextView mMovieOverview;
    private Boolean isFavourite = false;

    private String title;
    private String movieCoverImageUrl;
    private String imageURL;
    private String vote;
    private String voteCount;
    private String date;
    private String languageCode;
    private String overView;
    private String coverUrl;
    private String trailerTitle;
    private FloatingActionButton favoriteButton;
    private int movieId;
    private int movieDBId;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieVote = (TextView) findViewById(R.id.movie_vote);
        mMovieVoteCount = (TextView) findViewById(R.id.vote_count);
        mMovieDate = (TextView) findViewById(R.id.movie_date);
        mMovieLanguage = (TextView) findViewById(language);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        coverImageView = (ImageView) findViewById(R.id.cover_poster);
        TextView mTrailerEmptyTextView = (TextView) findViewById(R.id.trailer_empty_textview);
        TextView mReviewEmptyTextView = (TextView) findViewById(R.id.review_empty_textview);
        pb1 = (ProgressBar) findViewById(R.id.pb_1);
        pb2 = (ProgressBar) findViewById(R.id.pb_2);
        pb3 = (ProgressBar) findViewById(R.id.pb_3);
        NoReviews = (TextView) findViewById(R.id.no_review);
        NoReviews.setVisibility(View.INVISIBLE);

        pb1.setVisibility(View.VISIBLE);
        pb2.setVisibility(View.VISIBLE);
        pb3.setVisibility(View.VISIBLE);
        mTrailerEmptyTextView.setVisibility(View.INVISIBLE);
        mReviewEmptyTextView.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        favoriteButton = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent = this.getIntent();
        if (intent != null) {

            String strdata = intent.getExtras().getString("Intent");
            if (strdata.equals("From DBCursorAdapter")) {

                movieDBId = intent.getIntExtra("id", 1);
                movieId = intent.getIntExtra("actualId", 2);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(MOVIE_FAVORITE_LOADER_ID, null, databaseLoader);
                setTitle(title);
            } else if (strdata.equals("From_MovieAdapter")) {

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    movie = bundle.getParcelable("movie");
                } else {
                    movie = savedInstanceState.getParcelable("movie");
                }
                movieId = movie.getmMovieId();
                imageURL = movie.getmMoviePoster();
                title = movie.getmMovieTitle();
                vote = movie.getmMovieVote();
                voteCount = movie.getmMovieVoteCount();
                date = movie.getmMovieReleaseDate();
                languageCode = movie.getmMovieLanguage();
                overView = movie.getmMovieOverview();
                setData();
            }
        }

        if (isNetworkAvailable()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_TRAILER_LOADER_ID, null, movieTrailerCallbacks);
            loaderManager.initLoader(MOVIE_COVER_IMAGE_LOADER_ID, null, movieCoverImageCallback);
            loaderManager.initLoader(MOVIE_REVIEW_LOADER_ID, null, movieReviewCallback);

            mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
            mTrailerRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mTrailerRecyclerView.setLayoutManager(layoutManager);
            mTrailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mTrailerRecyclerView.setNestedScrollingEnabled(false);

            mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
            mReviewRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            mReviewRecyclerView.setLayoutManager(layoutManager2);
            mReviewRecyclerView.setNestedScrollingEnabled(false);
        } else {

            pb1.setVisibility(View.GONE);
            pb2.setVisibility(View.GONE);
            pb3.setVisibility(View.GONE);
            mTrailerEmptyTextView.setVisibility(View.VISIBLE);
            mReviewEmptyTextView.setVisibility(View.VISIBLE);
        }

    }


    private void setData() {
        setTitle(title);

        if (TextUtils.isEmpty(imageURL) || imageURL.equals("null")) {
            mMoviePoster.setImageResource(R.drawable.not_available);
        } else {
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL + imageURL)
                    .error(R.drawable.not_available)
                    .into(mMoviePoster);
        }

        mMovieTitle.setText(title);
        double votePercentage = Double.parseDouble(vote);
        votePercentage = votePercentage * 10;
        String displayVote = String.format("%.0f", votePercentage) + "%";
        mMovieVote.setText(displayVote);
        mMovieVoteCount.setText(voteCount += getString(R.string.votes));

        if (TextUtils.isEmpty(date) || date.equals("")) {
            mMovieDate.setText(R.string.not_available);
        } else {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date inputDate = input.parse(date);
                mMovieDate.setText(output.format(inputDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Locale loc;
        String languageName = null;
        if (languageCode != null) {
            switch (languageCode) {
                case "en":
                    loc = new Locale(languageCode);
                    languageName = loc.getDisplayName(loc);
                    break;

                case "es":
                    loc = new Locale(languageCode);
                    languageName = loc.getDisplayName(loc);
                    break;

                case "hi":
                    loc = new Locale(languageCode);
                    languageName = loc.getDisplayName(loc);
                    break;

                case "ja":
                    loc = new Locale(languageCode);
                    languageName = loc.getDisplayName(loc);
                    break;

                case "it":
                    loc = new Locale(languageCode);
                    languageName = loc.getDisplayName(loc);
                    break;

                default:
                    languageName = getString(R.string.not_available);
                    break;
            }
        }
        mMovieLanguage.setText(languageName);

        if (TextUtils.isEmpty(overView) || overView.equals("")) {
            mMovieOverview.setText(R.string.not_available);
        } else {
            mMovieOverview.setText(overView);
        }

        if (coverUrl != null) {
            pb1.setVisibility(View.INVISIBLE);
            Picasso.with(getBaseContext())
                    .load(IMAGE_URL + coverUrl)
                    .error(R.drawable.not_available)
                    .into(coverImageView);
        }

        String[] projection = new String[]{MovieEntry.MOVIE_ID};
        Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            int movieIdIndex = cursor.getColumnIndex(MovieEntry.MOVIE_ID);
            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(movieIdIndex);
                cursor.moveToNext();
                if (id == movieId) {
                    isFavourite = true;
                    favoriteButton.setImageResource(R.drawable.heart_icon_red);
                }
            }
        } finally {
            cursor.close();
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    isFavourite = false;
                    favoriteButton.setImageResource(R.drawable.heart_icon_red_border);

                    long deleted = getContentResolver().delete(MovieEntry.contentItemUri(String.valueOf(movieDBId)), null, null);

                    if (deleted > 0) {
                        Toast.makeText(getBaseContext(), "Movie deleted from Favorites", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addMovieDataToDB(movieId, title, overView, languageCode,
                            date, vote, voteCount, imageURL, movieCoverImageUrl);
                    favoriteButton.setImageResource(R.drawable.heart_icon_red);
                    isFavourite = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addMovieDataToDB(int movieId, String title, String overView, String language,
                                  String outputDate, String vote, String voteCount, String imageURL, String movieCoverImageUrl) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.MOVIE_ID, movieId);
        contentValues.put(MovieEntry.MOVIE_TITLE, title);
        contentValues.put(MovieEntry.MOVIE_OVERVIEW, overView);
        contentValues.put(MovieEntry.MOVIE_LANGUAGE, language);
        contentValues.put(MovieEntry.MOVIE_RELEASE_DATE, outputDate);
        contentValues.put(MovieEntry.MOVIE_VOTE_PERCENTAGE, vote);
        contentValues.put(MovieEntry.MOVIE_VOTE_COUNT, voteCount);
        contentValues.put(MovieEntry.MOVIE_POSTER, imageURL);
        contentValues.put(MovieEntry.MOVIE_COVER, movieCoverImageUrl);

        Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(getBaseContext(), "Movie added to Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private LoaderManager.LoaderCallbacks<List<MovieTrailer>> movieTrailerCallbacks =
            new LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {
                @Override
                public Loader<List<MovieTrailer>> onCreateLoader(int id, Bundle args) {

                    Uri baseUri = Uri.parse(MOVIE_DB_URL);
                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    uriBuilder.appendPath(String.valueOf(movieId));
                    uriBuilder.appendPath("videos");
                    uriBuilder.appendQueryParameter("api_key", API_KEY);
                    return new MovieTrailerLoader(getBaseContext(), uriBuilder.toString());
                }

                @Override
                public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> movieTrailers) {
                    if (movieTrailers != null && !movieTrailers.isEmpty()) {
                        MovieTrailerAdapter movieTrailerAdapter = new MovieTrailerAdapter(getBaseContext(), (ArrayList<MovieTrailer>) movieTrailers);
                        mTrailerRecyclerView.setAdapter(movieTrailerAdapter);
                        pb2.setVisibility(View.INVISIBLE);
                        trailerTitle = movieTrailers.get(0).getmMovieTrailerTitle();
                        videoUrl = movieTrailers.get(0).getmMovieTrailerKey();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<MovieTrailer>> loader) {
                    getLoaderManager().restartLoader(MOVIE_TRAILER_LOADER_ID, null, this);
                }

            };

    private LoaderManager.LoaderCallbacks<List<MovieCoverImage>> movieCoverImageCallback
            = new LoaderManager.LoaderCallbacks<List<MovieCoverImage>>() {

        @Override
        public Loader<List<MovieCoverImage>> onCreateLoader(int id, Bundle args) {
            Uri baseUri = Uri.parse(MOVIE_DB_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendPath(String.valueOf(movieId));
            uriBuilder.appendPath("images");
            uriBuilder.appendQueryParameter("api_key", API_KEY);
            return new MovieCoverLoader(getBaseContext(), uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<MovieCoverImage>> loader, List<MovieCoverImage> data) {

            if (data != null && !data.isEmpty()) {
                movieCoverImageUrl = data.get(0).getmMovieCoverImage();
                pb1.setVisibility(View.INVISIBLE);
                Picasso.with(getBaseContext())
                        .load(IMAGE_URL + movieCoverImageUrl)
                        .error(R.drawable.not_available)
                        .into(coverImageView);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieCoverImage>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<List<MovieReview>> movieReviewCallback
            = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {

        @Override
        public Loader<List<MovieReview>> onCreateLoader(int id, Bundle args) {
            Uri baseUri = Uri.parse(MOVIE_DB_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendPath(String.valueOf(movieId));
            uriBuilder.appendPath("reviews");
            uriBuilder.appendQueryParameter("api_key", API_KEY);
            return new MovieReviewLoader(getBaseContext(), uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> reviewList) {
            if (reviewList != null && !reviewList.isEmpty()) {
                MovieReviewAdapter movieReviewAdapter = new MovieReviewAdapter(getBaseContext(), (ArrayList<MovieReview>) reviewList);
                mReviewRecyclerView.setAdapter(movieReviewAdapter);
                pb3.setVisibility(View.INVISIBLE);
            } else {
                pb3.setVisibility(View.INVISIBLE);
                NoReviews.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieReview>> loader) {
            getLoaderManager().restartLoader(MOVIE_REVIEW_LOADER_ID, null, this);
        }
    };

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
                        String id = contentItemUri(String.valueOf(movieDBId)).getPathSegments().get(1);
                        String mSelection = MovieEntry._ID + " = ?";
                        String[] mSelectionArgs = new String[]{id};
                        return getContext().getContentResolver()
                                .query(contentItemUri(String.valueOf(movieDBId)),
                                        null,
                                        mSelection,
                                        mSelectionArgs,
                                        null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {
            try {
                if (mCursor != null && mCursor.getCount() != 0) {

                    int posterIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_POSTER);
                    int titleIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_TITLE);
                    int overViewIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_OVERVIEW);
                    int languageIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_LANGUAGE);
                    int dateIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_RELEASE_DATE);
                    int percentageIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_VOTE_PERCENTAGE);
                    int voteIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_VOTE_COUNT);
                    int coverIndex = mCursor.getColumnIndex(MovieEntry.MOVIE_COVER);
                    mCursor.moveToFirst();

                    imageURL = mCursor.getString(posterIndex);
                    title = mCursor.getString(titleIndex);
                    overView = mCursor.getString(overViewIndex);
                    languageCode = mCursor.getString(languageIndex);
                    date = mCursor.getString(dateIndex);
                    vote = mCursor.getString(percentageIndex);
                    voteCount = mCursor.getString(voteIndex);
                    coverUrl = mCursor.getString(coverIndex);
                    setData();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            getLoaderManager().restartLoader(MOVIE_FAVORITE_LOADER_ID, null, this);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = updateShareActionProvider();
            startActivity(Intent.createChooser(shareIntent, "Share using"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent updateShareActionProvider() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " - " + trailerTitle + ":\n "
                + VIDEO_URL + videoUrl);

        return sharingIntent;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
