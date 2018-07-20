package com.example.android.udacity_popular_movies.Helper_Classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Reviews {

    @SerializedName("results")
    private List<Review> reviews = new ArrayList<>();

    public List<Review> getReviews() {
        return reviews;
    }
}
