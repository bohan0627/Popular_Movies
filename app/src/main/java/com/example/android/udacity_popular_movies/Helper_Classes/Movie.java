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

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.udacity_popular_movies.R;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Parcelable {

    public static final String LOG_TAG = Movie.class.getSimpleName();
    public static final float POSTER_ASPECT_RATIO = 1.5f;

    // Get movie datails
    @SerializedName("id")
    private long movieId;
    @SerializedName("original_title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String moviePoster;
    @SerializedName("overview")
    private String movieOverview;
    @SerializedName("vote_average")
    private String movieUserRating;
    @SerializedName("release_date")
    private String movieReleaseDate;
    @SerializedName("backdrop_path")
    private String movieBackdrop;

    public Movie(long id, String title, String poster, String overview, String userRating, String releaseDate, String backdrop) {
        movieId = id;
        movieTitle = title;
        moviePoster = poster;
        movieOverview = overview;
        movieUserRating = userRating;
        movieReleaseDate = releaseDate;
        movieBackdrop = backdrop;
    }

    private Movie() {
    }

    @Nullable
    public String getPosterUrl(Context context) {
        if (moviePoster != null && !moviePoster.isEmpty()) {
            return context.getResources().getString(R.string.url_for_downloading_poster) + moviePoster;
        }

        return null;
    }

    public String getReleaseDate(Context context) {
        String inputDatePattern = "YYYY-MM-DD";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDatePattern, Locale.US);
        if (movieReleaseDate != null && !movieReleaseDate.isEmpty()) {
            try {
                Date date = inputFormat.parse(movieReleaseDate);
                return DateFormat.getDateInstance().format(date);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Release Data not parsed successfully: " + movieReleaseDate);
            }
        } else {
            movieReleaseDate = context.getString(R.string.release_date_missing);
        }

        return movieReleaseDate;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            Movie movie = new Movie();
            movie.movieId = source.readLong();
            movie.movieTitle = source.readString();
            movie.moviePoster = source.readString();
            movie.movieOverview = source.readString();
            movie.movieUserRating = source.readString();
            movie.movieReleaseDate = source.readString();
            movie.movieBackdrop = source.readString();
            return movie;
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int describeContents() {
        return -1;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(movieId);
        parcel.writeString(movieTitle);
        parcel.writeString(moviePoster);
        parcel.writeString(movieOverview);
        parcel.writeString(movieUserRating);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieBackdrop);
    }

    @Nullable
    public String getTitle() {
        return movieTitle;
    }

    @Nullable
    public long getId() {
        return movieId;
    }

    @Nullable
    public String getReleaseDate() {
        return movieReleaseDate;
    }

    @Nullable
    public String getPoster() {
        return moviePoster;
    }

    @Nullable
    public String getOverview() {
        return movieOverview;
    }

    @Nullable
    public String getUserRating() {
        return movieUserRating;
    }

    @Nullable
    public String getBackdrop() {
        return movieBackdrop;
    }

    @Nullable
    public String getBackdropUrl(Context context) {
        if (movieBackdrop != null && !movieBackdrop.isEmpty()) {
            return context.getResources().getString(R.string.url_for_downloading_backdrop) +
                    movieBackdrop;
        }
        return null;
    }

}
