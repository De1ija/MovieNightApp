package com.example.android.movienightvolley.Activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.android.movienightvolley.Data.Movie;
import com.example.android.movienightvolley.Network.VolleyCallback;
import com.example.android.movienightvolley.Network.VolleyNetwork;
import com.example.android.movienightvolley.R;
import com.example.android.movienightvolley.Data.Review;
import com.example.android.movienightvolley.Adapters.ReviewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.movienightvolley.Data.MovieContract.MovieEntry;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String BASE_URL_YOUTUBE = "https://www.youtube.com/watch";

    public static final int MOVIE_DETAIL_LOADER = 2;

    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;
    private TextView mUserVoteCountTextView;
    private TextView mTitleTextView;
    private Button mFavoritesButton;
    private TextView mMovieOverviewTextView;
    private ListView mReviewListView;
    private LinearLayout mWatchMovieTrailer;
    private TextView mNoReviewsTextView;

    private ReviewAdapter mReviewAdapter;
    private ArrayList<Review> mReviewList;

    private Movie mMovie;
    private String mMovieTrailer;
    private Uri mFavoriteMovieURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mPosterImageView = findViewById(R.id.movie_poster_image_view);
        mReleaseDateTextView = findViewById(R.id.movie_release_date_text_view);
        mUserRatingTextView = findViewById(R.id.movie_user_rating_text_view);
        mUserVoteCountTextView = findViewById(R.id.movie_user_vote_count_text_view);
        mTitleTextView = findViewById(R.id.movie_title_text_view);
        mFavoritesButton = findViewById(R.id.favorites_button);
        mMovieOverviewTextView = findViewById(R.id.movie_overview_text_view);
        mReviewListView = findViewById(R.id.reviews_list_view);
        mWatchMovieTrailer = findViewById(R.id.movie_trailer_container_layout);
        mNoReviewsTextView = findViewById(R.id.no_reviews_text_view);

        mFavoriteMovieURI = getIntent().getData();
        if(mFavoriteMovieURI == null) {
            mMovie = getIntent().getParcelableExtra("Movie");
        }else{
            Cursor favMovieCursor = getContentResolver().query(mFavoriteMovieURI, null,
                    null, null, null);
            if(favMovieCursor != null) {
                mMovie = createMovieFromCursor(favMovieCursor);
            }
        }

        setUI(mMovie);
        getNetworkData();

        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMovieAsFavorite();
                mFavoritesButton.setBackgroundResource(R.drawable.favorite_button_marked_24dp);
            }
        });

        mWatchMovieTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mMovieTrailer));
                startActivity(trailerIntent);
            }
        });

        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(this, MovieEntry.CONTENT_URI,
                    null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
            if (id == mMovie.getMovieId()) {
                mFavoritesButton.setBackgroundResource(R.drawable.favorite_button_marked_24dp);
            }
            while (cursor.moveToNext()) {
                int nextId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
                if (nextId == mMovie.getMovieId()) {
                    mFavoritesButton.setBackgroundResource(R.drawable.favorite_button_marked_24dp);
                    return;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /*
    * -------------Helper methods-------------
    */

    private Movie createMovieFromCursor(Cursor cursor){
        cursor.moveToFirst();
        int FavoriteMovieId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
        String FavoritePosterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        String FavoriteReleaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
        String FavoriteTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
        String FavoriteOverview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
        double FavoriteUserRating = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING));
        int FavoriteUserVoteCount = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_USER_VOTE_COUNT));
        cursor.close();
        return new Movie(FavoriteMovieId, FavoritePosterPath, FavoriteTitle, FavoriteOverview,
                FavoriteReleaseDate, FavoriteUserRating, FavoriteUserVoteCount);
    }

    private void setUI(Movie movie){
        int imageSize = MainActivity.mScreenWidth / 2;
        Picasso.get().load(mMovie.getPosterPath()).resize(imageSize, imageSize).into(mPosterImageView);
        String releaseDate = movie.getReleaseDate().substring(0, 4);
        mReleaseDateTextView.setText(releaseDate);
        String userRating = String.valueOf(movie.getUserRating());
        mUserRatingTextView.setText(userRating);
        mUserVoteCountTextView.setText(String.valueOf(movie.getUserVoteCount()));
        mTitleTextView.setText(movie.getTitle());
        mMovieOverviewTextView.setText(movie.getOverview());
    }

    private void getNetworkData(){
        VolleyNetwork.getInstance(this).fetchMovieRewiew(mMovie, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                parseJsonToReviewList(response);
            }
        });
        VolleyNetwork.getInstance(this).fetchMovieTrailer(mMovie, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                parseJsonToMovieTrailer(response);
            }
        });
    }

    private void parseJsonToReviewList(JSONObject response){
        try {
            JSONArray jsonArray = response.getJSONArray("results");
            if (jsonArray.length() > 0){
                mReviewList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject review = jsonArray.getJSONObject(i) ;
                    mReviewList.add(new Review(review.getString("author"), review.getString("content")));
                    mReviewAdapter = new ReviewAdapter(MovieDetailActivity.this, mReviewList);
                    mReviewListView.setAdapter(mReviewAdapter);
                    mNoReviewsTextView.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonToMovieTrailer(JSONObject response){
        try {
            JSONArray jsonArray = response.getJSONArray("results");
            if (jsonArray.length() > 0){
                for (int i = 0; i < 1; i++) {
                    JSONObject movieTrailer = jsonArray.getJSONObject(i) ;
                    mMovieTrailer = createYoutubeMovieTrailerURL(movieTrailer.getString("key"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String createYoutubeMovieTrailerURL(String trailerKey){
        Uri baseUri = Uri.parse(BASE_URL_YOUTUBE);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendQueryParameter("v", trailerKey);
        return uriBuilder.toString();
    }

    private void saveMovieAsFavorite(){
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
        cv.put(MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        cv.put(MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        cv.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
        cv.put(MovieEntry.COLUMN_USER_RATING, mMovie.getUserRating());
        cv.put(MovieEntry.COLUMN_USER_VOTE_COUNT, mMovie.getUserVoteCount());
        Uri favoriteMovieUri = getContentResolver().insert(MovieEntry.CONTENT_URI, cv);
        if(favoriteMovieUri == null){
            Toast.makeText(this, "Already added as favorite", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Movie marked as favorite", Toast.LENGTH_LONG).show();
        }
    }
}
