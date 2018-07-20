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