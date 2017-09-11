package com.example.parag.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.example.parag.popularmovies.R.id.language;

public class DetailActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            movie = bundle.getParcelable("movie");
        }else {
            movie = savedInstanceState.getParcelable("movie");
        }
        ImageView mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        TextView mMovieTitle = (TextView) findViewById(R.id.movie_title);
        TextView mMovieVote = (TextView) findViewById(R.id.movie_vote);
        TextView mMovieVoteCount = (TextView)findViewById(R.id.vote_count);
        TextView mMovieDate = (TextView) findViewById(R.id.movie_date);
        TextView mMovieLanguage = (TextView) findViewById(language);
        TextView mMovieOverview = (TextView) findViewById(R.id.movie_overview);

        String imageURL = movie.getmMoviePoster();
        String title = movie.getmMovieTitle();
        String vote = movie.getmMovieVote();
        String voteCount = movie.getmMovieVoteCount();
        String date = movie.getmMovieReleaseDate();
        String languageCode = movie.getmMovieLanguage();
        String overView = movie.getmMovieOverview();

        if (movie != null && movie.getmMovieTitle() != null){
            setTitle(title);
        }

        if (TextUtils.isEmpty(imageURL) || imageURL.equals("null")) {
            mMoviePoster.setImageResource(R.drawable.not_available);
        }else {
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL + imageURL)
                    .into(mMoviePoster);
        }

        mMovieTitle.setText(title);

        double votePercentage = Double.parseDouble(vote);
        votePercentage = votePercentage * 10;
        vote = String.format("%.0f",votePercentage) + "%";
        mMovieVote.setText(vote);

        voteCount += " votes";
        mMovieVoteCount.setText(voteCount);

        if (TextUtils.isEmpty(date) || date.equals("")) {
            mMovieDate.setText(R.string.not_available);
        }else {
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
            switch (languageCode){
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

                default:
                    languageName = getString(R.string.not_available);
                    break;
            }
        }
        mMovieLanguage.setText(languageName);

        if (TextUtils.isEmpty(overView) || overView.equals("")){
            mMovieOverview.setText(R.string.not_available);
        }else {
            mMovieOverview.setText(overView);
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("movie",movie);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }


}
