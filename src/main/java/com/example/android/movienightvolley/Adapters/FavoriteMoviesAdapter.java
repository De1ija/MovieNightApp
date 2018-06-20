package com.example.android.movienightvolley.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movienightvolley.Activities.MainActivity;
import com.example.android.movienightvolley.R;
import com.example.android.movienightvolley.Data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder>{

    private Context mContext;
    private Cursor mCursor;

    public FavoriteMoviesAdapter (Context context) {
        mContext = context;
    }

    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_movie_list_item, parent, false);
        return new FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMoviesViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        int id = mCursor.getInt(mCursor.getColumnIndex(MovieEntry._ID));
        holder.itemView.setTag(id);

        String movieUrl = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        String title = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
        String releaseDate = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
        String movieReleaseYear = releaseDate.substring(0,4);
        String movieTitle = title + "   " + movieReleaseYear;
        Double userRating = mCursor.getDouble(mCursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING));
        String movieUserRating = String.valueOf(userRating);
        int userVoteCount = mCursor.getInt(mCursor.getColumnIndex(MovieEntry.COLUMN_USER_VOTE_COUNT));
        String movieUserVoteCount = String.valueOf(userVoteCount);

        int imageSize = MainActivity.mScreenWidth/3;
        Picasso.get().load(movieUrl).resize(imageSize, imageSize).into(holder.moviePoster);
        holder.movieTitle.setText(movieTitle);
        holder.movieUserRating.setText(movieUserRating);
        holder.movieUserVoteCount.setText(movieUserVoteCount);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder{
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieUserRating;
        TextView movieUserVoteCount;

        public FavoriteMoviesViewHolder(View view) {
            super(view);
            moviePoster = view.findViewById(R.id.movie_poster_iv);
            movieTitle = view.findViewById(R.id.movie_title_tv);
            movieUserRating = view.findViewById(R.id.movie_user_rating_tv);
            movieUserVoteCount = view.findViewById(R.id.movie_user_vote_count_tv);
        }
    }
}
