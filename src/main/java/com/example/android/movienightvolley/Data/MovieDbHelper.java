package com.example.android.movienightvolley.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movienightvolley.Data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_EXPENSE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_USER_RATING + " REAL NOT NULL, "
                + MovieEntry.COLUMN_USER_VOTE_COUNT + " INTEGER NOT NULL, "
                + "CONSTRAINT movie_id UNIQUE(" + MovieEntry.COLUMN_MOVIE_ID + "));";
        sqLiteDatabase.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
