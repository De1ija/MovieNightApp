package com.example.android.movienightvolley.Data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bane on 6/11/2018.
 */

public class Movie implements Parcelable{

    private int movieId;
    private String posterPath;
    private String releaseDate;
    private String title;
    private String overview;
    private double userRating;
    private int userVoteCount;

    public Movie(int movieId, String posterPath, String title,
                 String overview, String releaseDate, double userRating, int userVoteCount) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.userVoteCount = userVoteCount;

    }

    private Movie(Parcel in){
        movieId = in.readInt();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        overview = in.readString();
        userRating = in.readDouble();
        userVoteCount = in.readInt();
    }

    public int getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating(){
        return userRating;
    }

    public int getUserVoteCount(){
        return userVoteCount;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", userRating=" + userRating +
                ", userVoteCount=" + userVoteCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeDouble(userRating);
        parcel.writeInt(userVoteCount);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
