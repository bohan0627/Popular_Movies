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
