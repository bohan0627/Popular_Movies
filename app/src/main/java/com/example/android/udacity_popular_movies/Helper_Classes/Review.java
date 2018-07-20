package com.example.android.udacity_popular_movies.Helper_Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

    @SerializedName("id")
    private String movieId;
    @SerializedName("author")
    private String movieAuthor;
    @SerializedName("content")
    private String movieContent;
    @SerializedName("url")
    private String movieUrl;

    public String getContent() {
        return movieContent;
    }

    public String getAuthor() {
        return movieAuthor;
    }

    public String getUrl() {
        return movieUrl;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            Review review = new Review();
            review.movieId = source.readString();
            review.movieAuthor = source.readString();
            review.movieContent = source.readString();
            review.movieUrl = source.readString();
            return review;
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int describeContents() {
        return -1;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(movieId);
        parcel.writeString(movieAuthor);
        parcel.writeString(movieContent);
        parcel.writeString(movieUrl);
    }
}
