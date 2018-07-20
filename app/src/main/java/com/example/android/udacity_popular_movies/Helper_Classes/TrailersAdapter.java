package com.example.android.udacity_popular_movies.Helper_Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.udacity_popular_movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {


    private final ArrayList<Trailer> movieTrailers;
    private final Callbacks movieCallbacks;

    public interface Callbacks {
        void watch(Trailer trailer, int position);
    }

    public TrailersAdapter(ArrayList<Trailer> trailers, Callbacks callbacks) {
        movieTrailers = trailers;
        movieCallbacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content_trailers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder trailersHolder, final int position) {
        final Trailer trailer = movieTrailers.get(position);
        final Context context = trailersHolder.movieView.getContext();

        float paddingLeft = 0;
        if (position == 0) {
            paddingLeft = context.getResources().getDimension(R.dimen.detail_horizontal_padding);
        }

        float paddingRight = 0;
        if (position + 1 != getItemCount()) {
            paddingRight = context.getResources().getDimension(R.dimen.detail_horizontal_padding) / 2;
        }

        trailersHolder.movieView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);

        trailersHolder.mTrailer = trailer;

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";

        Picasso.with(context)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .error(R.mipmap.yoda)
                .into(trailersHolder.movieThumbnailView);

        trailersHolder.movieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieCallbacks.watch(trailer, trailersHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View movieView;
        @BindView(R.id.trailer_thumbnail)
        ImageView movieThumbnailView;
        public Trailer mTrailer;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            movieView = view;
        }
    }

    public void add(List<Trailer> trailers) {
        movieTrailers.clear();
        movieTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> getTrailers() {
        return movieTrailers;
    }
}
