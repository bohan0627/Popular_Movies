package com.example.android.udacity_popular_movies.Helper_Classes;


import android.os.AsyncTask;
import android.support.annotation.StringDef;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.android.udacity_popular_movies.BuildConfig;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.udacity_popular_movies.Helper_Classes.Movie.LOG_TAG;

public class MovieDeatilsFetch extends AsyncTask<Void,Void,List<Movie>> {

    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String MY_FAVORITES = "favorites";

    public  interface Listener {
        void onFetchFinished(Command command);
    }

    @StringDef({MOST_POPULAR, TOP_RATED,MY_FAVORITES})
    public @interface SORT_BY {
    }

    private final NotifyAboutTaskCompletionCommand movieCommand;
    private
    @SORT_BY
    String movieSortBy;


    public static class NotifyAboutTaskCompletionCommand implements Command {
        private MovieDeatilsFetch.Listener movieListener;
        private List<Movie> movieList;

        public NotifyAboutTaskCompletionCommand(MovieDeatilsFetch.Listener listener) {
            movieListener = listener;
        }


        @Override
        public void execute() {
            movieListener.onFetchFinished(this);
        }

        public List<Movie> getMovies() {
            return movieList;
        }
    }

    public MovieDeatilsFetch(@SORT_BY String sortBy, NotifyAboutTaskCompletionCommand command) {
        movieCommand = command;
        movieSortBy = sortBy;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            movieCommand.movieList = movies;
        }
        else {
            movieCommand.movieList = new ArrayList<>();
        }
        movieCommand.execute();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBService service = retrofit.create(TMDBService.class);
        Call<Movies> call = service.discoverMovies(movieSortBy,
                BuildConfig.TMDB_MOVIE_API_KEY);
        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            return movies.getMovies();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Something unexpected happened when loading ", e);
        }
        return null;
    }

}
