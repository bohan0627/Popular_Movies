package com.example.android.udacity_popular_movies.Helper_Classes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacity_popular_movies.R;
import com.squareup.picasso.Picasso;
import com.example.android.udacity_popular_movies.Activity.MovieDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MovieDetailsFragment extends Fragment implements TrailersFetch.Listener,
        TrailersAdapter.Callbacks, ReviewsFetch.Listener, ReviewsAdapter.Callbacks{

    public static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie mMovie;

    @BindView(R.id.movie_title)
    TextView movieTitleView;
    @BindView(R.id.movie_overview)
    TextView movieOverviewView;
    @BindView(R.id.movie_release_date)
    TextView movieReleaseDateView;
    @BindView(R.id.movie_user_rating)
    TextView movieRatingView;
    @BindView(R.id.movie_poster)
    ImageView moviePosterView;

    @BindView(R.id.text_overview)
    TextView movieOverviewStart;


    @BindViews({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star,
            R.id.rating_fourth_star, R.id.rating_fifth_star})
    List<ImageView> ratingStarViews;

    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";

    private TrailersAdapter movieTrailerListAdapter;
    private ReviewsAdapter movieReviewListAdapter;


    @BindView(R.id.trailers)
    RecyclerView movieRecyclerViewForTrailers;
    @BindView(R.id.reviews)
    RecyclerView movieRecyclerViewForReviews;

    @BindView(R.id.button_mark_as_favorite)
    Button movieButtonMarkAsFavorite;
    @BindView(R.id.button_remove_from_favorites)
    Button movieButtonRemoveFromFavorites;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof MovieDetailsActivity) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        ImageView movieBackdrop = activity.findViewById(R.id.movie_backdrop);
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(mMovie.getBackdropUrl(getContext()))
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.yoda)
                    .into(movieBackdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View basicView = inflater.inflate(R.layout.detail_movie, container, false);
        ButterKnife.bind(this, basicView);

        movieTitleView.setText(mMovie.getTitle());
        movieOverviewView.setText(mMovie.getOverview());
        movieOverviewView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        movieReleaseDateView.setText(mMovie.getReleaseDate(getContext()));

        Picasso.with(getContext())
                .load(mMovie.getPosterUrl(getContext()))
                .config(Bitmap.Config.RGB_565)
                .error(R.mipmap.yoda)
                .into(moviePosterView);

        updateRatingBar();

        updateFavoriteButtons();


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        movieRecyclerViewForTrailers.setLayoutManager(layoutManager);
        movieTrailerListAdapter = new TrailersAdapter(new ArrayList<Trailer>(), this);
        movieRecyclerViewForTrailers.setAdapter(movieTrailerListAdapter);
        movieRecyclerViewForTrailers.setNestedScrollingEnabled(false);


        movieReviewListAdapter = new ReviewsAdapter(new ArrayList<Review>(), this);
        movieRecyclerViewForReviews.setAdapter(movieReviewListAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            movieTrailerListAdapter.add(trailers);
        } else {
            fetchTrailers();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            movieReviewListAdapter.add(reviews);
        } else {
            fetchReviews();
        }

        return basicView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Trailer> trailers = movieTrailerListAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }

        ArrayList<Review> reviews = movieReviewListAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        return;
    }


    private void updateRatingBar() {
        if (mMovie.getUserRating() != null && !mMovie.getUserRating().isEmpty()) {
            String userRatingStr = getResources().getString(R.string.user_rating_movie,
                    mMovie.getUserRating());
            movieRatingView.setText(userRatingStr);

            float userRating = Float.valueOf(mMovie.getUserRating()) / 2;
            int integerPart = (int) userRating;

            for (int i = 0; i < integerPart; i++) {
                ratingStarViews.get(i).setImageResource(R.drawable.ic_star_black_36dp);
            }

            if (Math.round(userRating) > integerPart) {
                ratingStarViews.get(integerPart).setImageResource(R.drawable.ic_star_half_black_36dp);
            }

        } else {
            movieRatingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void watch(Trailer trailer, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl())));
    }

    @Override
    public void read(Review review, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(review.getUrl())));
    }

    @Override
    public void onFetchFinished(List<Trailer> trailers) {
        movieTrailerListAdapter.add(trailers);
        if (movieTrailerListAdapter.getItemCount() > 0) {
            Trailer trailer = movieTrailerListAdapter.getTrailers().get(0);
            updateShareActionProvider(trailer);
        }
    }

    @Override
    public void onReviewsFetchFinished(List<Review> reviews) {
        movieReviewListAdapter.add(reviews);
    }

    private void fetchTrailers() {
        TrailersFetch task = new TrailersFetch(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    private void fetchReviews() {
        ReviewsFetch task = new ReviewsFetch(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    public void markAsFavorite() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_ID,
                            mMovie.getId());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_TITLE,
                            mMovie.getTitle());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                            mMovie.getPoster());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                            mMovie.getOverview());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                            mMovie.getUserRating());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                            mMovie.getReleaseDate());
                    movieValues.put(MovieBasic.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                            mMovie.getBackdrop());
                    getContext().getContentResolver().insert(
                            MovieBasic.MovieEntry.CONTENT_URI,
                            movieValues
                    );
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    public void removeFromFavorites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {
                    getContext().getContentResolver().delete(MovieBasic.MovieEntry.CONTENT_URI,
                            MovieBasic.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(), null);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateFavoriteButtons() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    movieButtonRemoveFromFavorites.setVisibility(View.VISIBLE);
                    movieButtonMarkAsFavorite.setVisibility(View.GONE);
                } else {
                    movieButtonMarkAsFavorite.setVisibility(View.VISIBLE);
                    movieButtonRemoveFromFavorites.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        movieButtonMarkAsFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsFavorite();
                    }
                });


        movieButtonRemoveFromFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorites();
                    }
                });
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieBasic.MovieEntry.CONTENT_URI,
                new String[]{MovieBasic.MovieEntry.COLUMN_MOVIE_ID},
                MovieBasic.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    private void updateShareActionProvider(Trailer trailer) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovie.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailer.getName() + ": "
                + trailer.getTrailerUrl());

    }
}
