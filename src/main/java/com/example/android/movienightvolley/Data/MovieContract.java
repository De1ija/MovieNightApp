package com.example.android.movienightvolley.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    private MovieContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.movienightvolley";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_USER_VOTE_COUNT = "userVoteCount";
    }
}

