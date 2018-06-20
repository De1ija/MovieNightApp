package com.example.android.movienightvolley.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movienightvolley.Activities.MainActivity;
import com.example.android.movienightvolley.Data.Movie;
import com.example.android.movienightvolley.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bane on 6/11/2018.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder>{

    private final Context mContext;
    private ArrayList<Movie> mAdapterMovieList;

    public MoviePosterAdapter (Context context, ArrayList<Movie> movieList){
        mContext = context;
        mAdapterMovieList = movieList;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item, parent, false);
        view.setFocusable(true);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        Movie movie = mAdapterMovieList.get(position);
        String movieUrl = movie.getPosterPath();
        int imageSize = MainActivity.mScreenWidth / 2;

        Picasso.get().load(movieUrl).resize(imageSize,imageSize).into(holder.moviePosterView);

    }

    @Override
    public int getItemCount() {
        if(mAdapterMovieList == null) return 0;
        return mAdapterMovieList.size();
    }

    public void updateAdapter(ArrayList<Movie> movieList){
        mAdapterMovieList = movieList;
        notifyDataSetChanged();
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder{

        private ImageView moviePosterView;

        public MoviePosterViewHolder(View view) {
            super(view);
            moviePosterView = view.findViewById(R.id.moviePoster_iv);
        }
    }
}

