package com.example.android.udacity_popular_movies.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.*;
import android.view.*;
import android.support.v7.widget.*;
import android.database.*;

import android.content.Intent;

import com.example.android.udacity_popular_movies.Helper_Classes.Command;
import com.example.android.udacity_popular_movies.Helper_Classes.MovieBasic;
import com.example.android.udacity_popular_movies.Helper_Classes.MovieDetailsFragment;
import com.example.android.udacity_popular_movies.R;
import com.example.android.udacity_popular_movies.Helper_Classes.MovieDeatilsFetch;
import com.example.android.udacity_popular_movies.Helper_Classes.MovieListAdapter;
import com.example.android.udacity_popular_movies.Helper_Classes.Movie;


import java.util.*;

public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieDeatilsFetch.Listener, MovieListAdapter.Callbacks {

    private static final String EXTRA_MOVIES = "EXTRA_MOVIES";
    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";

    private boolean mTwoPane;
    private RetainedFragment movieRetainedFragment;
    private MovieListAdapter movieAdapter;
    private String movieSortBy = MovieDeatilsFetch.TOP_RATED;

    @BindView(R.id.movie_list)
    RecyclerView movieRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar movieToolbar;

    private static final int FAVORITE_MOVIES_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        movieToolbar.setTitle(R.string.title_movie_list);
        setSupportActionBar(movieToolbar);

        String tag = RetainedFragment.class.getName();
        this.movieRetainedFragment = (RetainedFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (this.movieRetainedFragment == null) {
            this.movieRetainedFragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add(this.movieRetainedFragment, tag).commit();
        }

        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources()
                .getInteger(R.integer.grid_number_cols)));

        movieAdapter = new MovieListAdapter(new ArrayList<Movie>(), this);
        movieRecyclerView.setAdapter(movieAdapter);

        mTwoPane = findViewById(R.id.movie_detail_container) != null;

        if (savedInstanceState != null) {
            movieSortBy = savedInstanceState.getString(EXTRA_SORT_BY);
            if (savedInstanceState.containsKey(EXTRA_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
                movieAdapter.add(movies);
                findViewById(R.id.progress).setVisibility(View.GONE);

                if (movieSortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
                    getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
                }

            }
            updateEmptyState();
        } else {
            fetchMovies(movieSortBy);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movies = movieAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_MOVIES, movies);
        }
        outState.putString(EXTRA_SORT_BY, movieSortBy);

        if (!movieSortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_movie_list, menu);

        switch (movieSortBy) {
            case MovieDeatilsFetch.TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case MovieDeatilsFetch.MOST_POPULAR:
                menu.findItem(R.id.sort_by_most_popular).setChecked(true);
                break;
            case MovieDeatilsFetch.MY_FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                if (movieSortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                movieSortBy = MovieDeatilsFetch.TOP_RATED;
                fetchMovies(movieSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                if (movieSortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                movieSortBy = MovieDeatilsFetch.MOST_POPULAR;
                fetchMovies(movieSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                movieSortBy = MovieDeatilsFetch.MY_FAVORITES;
                item.setChecked(true);
                fetchMovies(movieSortBy);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void open(Movie movie, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailsFragment.ARG_MOVIE, movie);
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, fragment).commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsFragment.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onFetchFinished(Command command) {
        if (command instanceof MovieDeatilsFetch.NotifyAboutTaskCompletionCommand) {
            movieAdapter.add(((MovieDeatilsFetch.NotifyAboutTaskCompletionCommand) command).getMovies());
            updateEmptyState();
            findViewById(R.id.progress).setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieAdapter.add(cursor);
        updateEmptyState();
        findViewById(R.id.progress).setVisibility(View.GONE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        return new CursorLoader(this, MovieBasic.MovieEntry.CONTENT_URI, MovieBasic.MovieEntry.MOVIE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        return;
    }

    private void fetchMovies(String sortBy) {
        if (!sortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            MovieDeatilsFetch.NotifyAboutTaskCompletionCommand command =
                    new MovieDeatilsFetch.NotifyAboutTaskCompletionCommand(this.movieRetainedFragment);
            new MovieDeatilsFetch(sortBy, command).execute();
        } else {
            getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
        }
    }

    private void updateEmptyState() {
        if (movieAdapter.getItemCount() == 0) {
            if (movieSortBy.equals(MovieDeatilsFetch.MY_FAVORITES)) {
                findViewById(R.id.empty_state_container).setVisibility(View.GONE);
                findViewById(R.id.empty_favorites_container).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.empty_state_container).setVisibility(View.VISIBLE);
                findViewById(R.id.empty_favorites_container).setVisibility(View.GONE);
            }
        } else {
            findViewById(R.id.empty_state_container).setVisibility(View.GONE);
            findViewById(R.id.empty_favorites_container).setVisibility(View.GONE);
        }
    }

    public static class RetainedFragment extends Fragment implements MovieDeatilsFetch.Listener {

        private boolean moviePaused = false;
        private Command movieWaitingCommand = null;

        public RetainedFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            moviePaused = true;
        }

        @Override
        public void onResume() {
            super.onResume();
            moviePaused = false;
            if (movieWaitingCommand != null) {
                onFetchFinished(movieWaitingCommand);
            }
        }

        @Override
        public void onFetchFinished(Command command) {
            if (getActivity() instanceof com.example.android.udacity_popular_movies.Helper_Classes.MovieDeatilsFetch.Listener && !moviePaused) {
                MovieDeatilsFetch.Listener listener = (com.example.android.udacity_popular_movies.Helper_Classes.MovieDeatilsFetch.Listener) getActivity();
                listener.onFetchFinished(command);
                movieWaitingCommand = null;
            } else {
                movieWaitingCommand = command;
            }
        }
    }
}
