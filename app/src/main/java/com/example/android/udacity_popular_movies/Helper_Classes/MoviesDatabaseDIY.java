package com.example.android.udacity_popular_movies.Helper_Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDatabaseDIY extends SQLiteOpenHelper {
    private static final int SQL_VERSION = 3;
    private static final String DATABASE_TITLE = "movies.db";

    public MoviesDatabaseDIY(Context context) {
        super(context, DATABASE_TITLE, null, SQL_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieBasic.MovieEntry.TABLE_NAME
                + " (" +
                MovieBasic.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieBasic.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieBasic.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieBasic.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

