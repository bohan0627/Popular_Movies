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

import java.util.*;
import butterknife.*;

import com.example.android.udacity_popular_movies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final ArrayList<Movie> movieLists;
    private final Callbacks mCallbacks;

    public MovieListAdapter(ArrayList<Movie> movies, Callbacks callbacks) {
        movieLists = movies;
        this.mCallbacks = callbacks;
    }

    public interface Callbacks {
        void open(Movie movie, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_content_movie, parent, false);

        final Context context = view.getContext();
        int gridColumnsNumber = context.getResources()
                .getInteger(com.example.android.udacity_popular_movies.R.integer.grid_number_cols);

        view.getLayoutParams().height = (int) (parent.getWidth() / gridColumnsNumber * Movie.POSTER_ASPECT_RATIO);

        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        viewHolder.cleanUp();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int viewPosition) {
        final Movie movie = movieLists.get(viewPosition);
        final Context context = viewHolder.movieView.getContext();

        viewHolder.mMovie = movie;
        viewHolder.movieTitleView.setText(movie.getTitle());

        String posterUrl = movie.getPosterUrl(context);
        if (posterUrl == null) {
            viewHolder.movieTitleView.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(movie.getPosterUrl(context))
                .config(Bitmap.Config.RGB_565)
                .error(R.mipmap.yoda)
                .into(viewHolder.movieImageView,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                if (viewHolder.mMovie.getId() != movie.getId()) {
                                    viewHolder.cleanUp();
                                } else {
                                    viewHolder.movieImageView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError() {
                                viewHolder.movieTitleView.setVisibility(View.VISIBLE);
                            }
                        }
                );

        viewHolder.movieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.open(movie, viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieLists.size();
    }

    public ArrayList<Movie> getMovies() {
        return movieLists;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View movieView;
        @BindView(com.example.android.udacity_popular_movies.R.id.thumbnail)
        ImageView movieImageView;
        @BindView(R.id.title)
        TextView movieTitleView;
        public Movie mMovie;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            movieView = view;
        }

        public void cleanUp() {
            final Context context = movieView.getContext();
            Picasso.with(context).cancelRequest(movieImageView);
            movieImageView.setImageBitmap(null);
            movieImageView.setVisibility(View.INVISIBLE);
            movieTitleView.setVisibility(View.GONE);
        }

    }

    public void add(List<Movie> movies) {
        movieLists.clear();
        movieLists.addAll(movies);
        notifyDataSetChanged();
    }


    public void add(Cursor cursor) {
        movieLists.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieBasic.MovieEntry.COL_MOVIE_ID);
                String title = cursor.getString(MovieBasic.MovieEntry.COL_MOVIE_TITLE);
                String posterPath = cursor.getString(MovieBasic.MovieEntry.COL_MOVIE_POSTER_PATH);
                String overview = cursor.getString(MovieBasic.MovieEntry.COL_MOVIE_OVERVIEW);
                String rating = cursor.getString(MovieBasic.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
                String releaseDate = cursor.getString(MovieBasic.MovieEntry.COL_MOVIE_RELEASE_DATE);
                String backdropPath = cursor.getString(MovieBasic .MovieEntry.COL_MOVIE_BACKDROP_PATH);
                Movie movie = new Movie(id, title, posterPath, overview, rating, releaseDate, backdropPath);
                movieLists.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

}

