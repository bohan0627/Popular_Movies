package com.example.android.udacity_popular_movies.Helper_Classes;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Movies {

    @SerializedName("results")
    private List<Movie> movieLists = new ArrayList<>();

    public List<Movie> getMovies() {
        return movieLists;
    }
}

