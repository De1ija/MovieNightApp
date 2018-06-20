package com.example.android.movienightvolley.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android.movienightvolley.Data.Movie;
import com.example.android.movienightvolley.Adapters.MoviePosterAdapter;
import com.example.android.movienightvolley.Network.VolleyCallback;
import com.example.android.movienightvolley.Network.VolleyNetwork;
import com.example.android.movienightvolley.R;
import com.example.android.movienightvolley.Adapters.RecyclerViewItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainAcivity";

    public ArrayList<Movie> mMovieList;
    private RecyclerView mMoviePosterRecyclerView;
    private TextView mNoInternetTextView;
    public MoviePosterAdapter mMoviePosterAdapter;
    public static int mScreenWidth;

    private String mOrderBySharePrefs;
    private String mMoviePageNumberSharePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getScreenWidth();
        getSharedPrefs();

        mMoviePosterRecyclerView = findViewById(R.id.moviePoster_rv);
        mMoviePosterRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMoviePosterAdapter = new MoviePosterAdapter(MainActivity.this, mMovieList);
        mMoviePosterRecyclerView.setAdapter(mMoviePosterAdapter);
        mMoviePosterRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener
                (this, mMoviePosterRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent detailMovieIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                Movie movie = mMovieList.get(position);
                detailMovieIntent.putExtra("Movie", movie);
                startActivity(detailMovieIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //show overview as alert dialog
            }
        }));

        //checking if there is internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            mMovieList = new ArrayList<>();
            VolleyNetwork.getInstance(this).fetchMovieData(mOrderBySharePrefs, mMoviePageNumberSharePrefs, new VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject response) {
                    parseJsonToMovieList(response);
                    mMoviePosterAdapter.updateAdapter(mMovieList);
                }
            });
        }else{
            mNoInternetTextView = findViewById(R.id.no_internet_tv);
            mNoInternetTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if(id == R.id.action_favorites){
            Intent settingsIntent = new Intent(this, FavoritesActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * -------------Helper methods-------------
    */
    private void parseJsonToMovieList(JSONObject response){
        try {
            JSONArray jsonArray = response.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                int movieId = movieJson.getInt("id");
                String posterPath = ImageUrlBuilder(movieJson.getString("poster_path"));
                String title = movieJson.getString("title");
                String synopsis = movieJson.getString("overview");
                String releaseDate = movieJson.getString("release_date");
                double userRating = movieJson.getDouble("vote_average");
                int userVoteCount = movieJson.getInt("vote_count");
                mMovieList.add(new Movie(movieId, posterPath, title, synopsis,
                        releaseDate, userRating, userVoteCount));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    private String ImageUrlBuilder(String url){
        Uri baseUri = Uri.parse(IMAGE_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon().appendEncodedPath(url);
        return uriBuilder.toString();
    }

    private void getSharedPrefs(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mOrderBySharePrefs = sharedPrefs.getString(getString(R.string.settings_preference_order_by_key),
                getString(R.string.settings_preference_order_by_default));
        mMoviePageNumberSharePrefs = sharedPrefs.getString(getString(R.string.settings_preference_movie_number_key),
                getString(R.string.settings_preference_movie_number_default));
    }

    private void getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
    }

}
