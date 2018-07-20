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

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.android.udacity_popular_movies.BuildConfig;

public class TrailersFetch extends AsyncTask<Long, Void, List<Trailer>> {

    public static String LOG_TAG;
    private final Listener movieListener;

    interface Listener {
        void onFetchFinished(List<Trailer> trailers);
    }

    public TrailersFetch(Listener listener) {
        movieListener = listener;
    }

    @Override
    protected List<Trailer> doInBackground(Long... params) {

        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBService service = retrofit.create(TMDBService.class);
        Call<Trailers> call = service.findTrailersById(movieId,
                BuildConfig.TMDB_MOVIE_API_KEY);
        try {
            Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return trailers.getTrailers();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Some problem happened loading trailers ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null) {
            movieListener.onFetchFinished(trailers);
        } else {
            movieListener.onFetchFinished(new ArrayList<Trailer>());
        }
    }
}
