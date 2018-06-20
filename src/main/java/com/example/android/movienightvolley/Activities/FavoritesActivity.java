package com.example.android.movienightvolley.Activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.movienightvolley.Adapters.FavoriteMoviesAdapter;
import com.example.android.movienightvolley.Adapters.RecyclerViewItemClickListener;
import com.example.android.movienightvolley.R;
import com.example.android.movienightvolley.Data.MovieContract.MovieEntry;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int FAVORITE_MOVIES_LOADER = 1;
    private RecyclerView mFavoriteMoviesRecyclerView;
    private FavoriteMoviesAdapter mFavoriteMovieAdapter;
    private TextView mNoFavoriteMovieTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mNoFavoriteMovieTextView = findViewById(R.id.no_favorite_movies_text_view);
        mFavoriteMoviesRecyclerView = findViewById(R.id.favorites_movie_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFavoriteMoviesRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFavoriteMoviesRecyclerView.getContext(),
                layoutManager.getOrientation());
        mFavoriteMoviesRecyclerView.addItemDecoration(dividerItemDecoration);
        mFavoriteMovieAdapter = new FavoriteMoviesAdapter(this);
        mFavoriteMoviesRecyclerView.setAdapter(mFavoriteMovieAdapter);
        mFavoriteMoviesRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener
                (this, mFavoriteMoviesRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent detailMovieIntent = new Intent(FavoritesActivity.this, MovieDetailActivity.class);
                        RecyclerView.ViewHolder viewHolder = mFavoriteMoviesRecyclerView.findViewHolderForAdapterPosition(position);
                        int id =(int) viewHolder.itemView.getTag();
                        String movieID = String.valueOf(id);
                        Uri uri = MovieEntry.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(movieID).build();
                        detailMovieIntent.setData(uri);
                        startActivity(detailMovieIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                String movieID = Integer.toString(id);
                Uri uri = MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(movieID).build();
                deleteMovieFromFavorites(uri);
            }
        }).attachToRecyclerView(mFavoriteMoviesRecyclerView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, MovieEntry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(!cursor.moveToFirst()){
            mFavoriteMoviesRecyclerView.setVisibility(View.GONE);
            mNoFavoriteMovieTextView.setVisibility(View.VISIBLE);
            mNoFavoriteMovieTextView.setText(R.string.no_favorite_movies_info);
        }else {
            mNoFavoriteMovieTextView.setVisibility(View.GONE);
            mFavoriteMoviesRecyclerView.setVisibility(View.VISIBLE);
            mFavoriteMovieAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete){
            deleteAllFavorites();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    *---------------Helper Methods-------------------
    */

    private void deleteAllFavorites(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.alert_dialog_delete_title);
        alertDialog.setMessage("About to delete all favorite movies,\n" +
                "you can delete single movie by swiping left or right.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
            }
        });
        alertDialog.show();
    }

    private void deleteMovieFromFavorites(final Uri movieUri){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.alert_dialog_delete_title);
        alertDialog.setMessage("About to delete this movie from favorites");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, FavoritesActivity.this);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(movieUri, null, null);
            }
        });
        alertDialog.show();
    }
}
