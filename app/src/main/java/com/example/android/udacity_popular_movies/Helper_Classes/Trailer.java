package com.example.android.udacity_popular_movies.Helper_Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailer implements Parcelable {


    @SerializedName("id")
    private String movieId;
    @SerializedName("key")
    private String movieKey;
    @SerializedName("name")
    private String movieName;
    @SerializedName("site")
    private String movieSite;
    @SerializedName("size")
    private String movieSize;


    private Trailer() {
    }

    public String getName() {
        return movieName;
    }

    public String getKey() {
        return movieKey;
    }

    public String getTrailerUrl() {
        return "http://www.youtube.com/watch?v=" + movieKey;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            Trailer trailer = new Trailer();
            trailer.movieId = source.readString();
            trailer.movieKey = source.readString();
            trailer.movieName = source.readString();
            trailer.movieSite = source.readString();
            trailer.movieSize = source.readString();
            return trailer;
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public int describeContents() {
        return -1;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(movieId);
        parcel.writeString(movieKey);
        parcel.writeString(movieName);
        parcel.writeString(movieSite);
        parcel.writeString(movieSize);
    }
}