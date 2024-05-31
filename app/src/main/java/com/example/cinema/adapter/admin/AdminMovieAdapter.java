package com.example.cinema.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema.constant.ConstantKey;
import com.example.cinema.databinding.ItemMovieAdminBinding;
import com.example.cinema.model.Movie;
import com.example.cinema.util.GlideUtils;

import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminMovieAdapter extends RecyclerView.Adapter<AdminMovieAdapter.MovieViewHolder> {


    private Context mContext;
    // List to hold the movie items
    private final List<Movie> mListMovies;
    // Listener interface to handle edit, delete, and click actions
    private final IManagerMovieListener iManagerMovieListener;

    // Interface for movie management listener
    public interface IManagerMovieListener {
        void editMovie(Movie movie); // Method to handle movie edit action
        void deleteMovie(Movie movie); // Method to handle movie delete action
        void clickItemMovie(Movie movie); // Method to handle movie item click action
    }

    //Constructor

    public AdminMovieAdapter(Context mContext, List<Movie> mListMovies, IManagerMovieListener iManagerMovieListener) {
        this.mContext = mContext;
        this.mListMovies = mListMovies;
        this.iManagerMovieListener = iManagerMovieListener;
    }

    //Methods
    // Method to release context when no longer needed
    public void release() {
        if (mContext != null) {
            mContext = null;
        }
    }
    // Method to create view holder
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieAdminBinding itemMovieAdminBinding = ItemMovieAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(itemMovieAdminBinding);
    }

    // Method to bind data to view holder
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mListMovies.get(position);
        if (movie == null) {
            return;
        }
        // Bind movie data to views in the view holder
        GlideUtils.loadUrl(movie.getImage(), holder.mItemMovieAdminBinding.imgMovie); // Load movie image
        holder.mItemMovieAdminBinding.tvName.setText(movie.getName()); // Set movie name
        holder.mItemMovieAdminBinding.tvCategory.setText(movie.getCategoryName()); // Set movie category name
        holder.mItemMovieAdminBinding.tvDescription.setText(movie.getDescription()); // Set movie description
        String strPrice = movie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE; // Format price with currency unit
        holder.mItemMovieAdminBinding.tvPrice.setText(strPrice); // Set movie price
        holder.mItemMovieAdminBinding.tvDate.setText(movie.getDate()); // Set movie date

        // Set click listeners for edit, delete, and item click actions

        holder.mItemMovieAdminBinding.imgEdit.setOnClickListener(v -> iManagerMovieListener.editMovie(movie));
        holder.mItemMovieAdminBinding.imgDelete.setOnClickListener(v -> iManagerMovieListener.deleteMovie(movie));
        holder.mItemMovieAdminBinding.layoutItem.setOnClickListener(v -> iManagerMovieListener.clickItemMovie(movie));
    }

    // Method to get item count
    @Override
    public int getItemCount() {
        if (mListMovies != null) {
            return mListMovies.size();
        }
        return 0;
    }

    // View holder class for movie item
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieAdminBinding mItemMovieAdminBinding;

        public MovieViewHolder(@NonNull ItemMovieAdminBinding itemMovieAdminBinding) {
            super(itemMovieAdminBinding.getRoot());
            this.mItemMovieAdminBinding = itemMovieAdminBinding;
        }
    }
}
