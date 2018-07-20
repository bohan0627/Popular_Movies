/*
   Copyright 2018 Bo Han

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.example.android.udacity_popular_movies.Helper_Classes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MoviesContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcherProvider = buildUriMatcher();
    static final int MOVIES = 200;
    static final int MOVIE_ID = 201;
    private MoviesDatabaseDIY moviesDB;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieBasic.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieBasic.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieBasic.PATH_MOVIE_ID, MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        moviesDB = new MoviesDatabaseDIY(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        switch (uriMatcherProvider.match(uri)) {
            case MOVIES: {
                cursor = moviesDB.getReadableDatabase().query(
                        MovieBasic.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_ID:{
                cursor = moviesDB.getReadableDatabase().query(
                        MovieBasic.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("This URI is unknown: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcherProvider.match(uri);
        switch (match) {
            case MOVIES:
                return MovieBasic.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("This URI is unknown: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = moviesDB.getWritableDatabase();
        final int match = uriMatcherProvider.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES: {
                long id = db.insert(MovieBasic.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri =MovieBasic.MovieEntry.buildMovieUri(id);
                } else {
                    throw new android.database.SQLException("Failed to populate row with " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("This URI is unknown: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = moviesDB.getWritableDatabase();
        final int match = uriMatcherProvider.match(uri);
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieBasic.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("This URI is unknown: " + uri);
        }

        if (rowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = moviesDB.getWritableDatabase();
        final int match = uriMatcherProvider.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieBasic.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("This URI is unknown: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
