package com.example.android.udacity_popular_movies.Helper_Classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.udacity_popular_movies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {



    private final ArrayList<Review> movieReviews;
    private final Callbacks movieCallbacks;

    public ReviewsAdapter(ArrayList<Review> reviews, Callbacks callbacks) {
        movieReviews = reviews;
        movieCallbacks = callbacks;
    }

    public interface Callbacks {
        void read(Review review, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder reviewsHolder, final int position) {
        final Review review = movieReviews.get(position);

        reviewsHolder.movieReview = review;
        reviewsHolder.movieContentView.setText(review.getContent());
        reviewsHolder.movieAuthorView.setText(review.getAuthor());

        reviewsHolder.movieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieCallbacks.read(review, reviewsHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View movieView;
        @BindView(R.id.review_content)
        TextView movieContentView;
        @BindView(R.id.review_author)
        TextView movieAuthorView;
        public Review movieReview;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            movieView = view;
        }
    }

    public void add(List<Review> reviews) {
        movieReviews.clear();
        movieReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return movieReviews;
    }
}
