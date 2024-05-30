package com.example.cinema.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema.constant.ConstantKey;
import com.example.cinema.databinding.ItemRevenueBinding;
import com.example.cinema.model.Revenue;

import java.util.List;

public class AdminRevenueAdapter extends RecyclerView.Adapter<AdminRevenueAdapter.AdminRevenueViewHolder> {

    // List to hold revenue items
    private final List<Revenue> mListRevenue;

    // Constructor
    public AdminRevenueAdapter(List<Revenue> mListRevenue) {
        this.mListRevenue = mListRevenue;
    }

    //Method
    // Method to create view holder
    @NonNull
    @Override
    public AdminRevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRevenueBinding itemRevenueBinding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminRevenueViewHolder(itemRevenueBinding);
    }

    //Method onBindViewHolder()
    @Override
    public void onBindViewHolder(@NonNull AdminRevenueViewHolder holder, int position) {
        Revenue revenue = mListRevenue.get(position);
        if (revenue == null) {
            return;
        }
        // Bind revenue data to views in the view holder
        holder.mItemRevenueBinding.tvStt.setText(String.valueOf(position + 1)); // Set serial number (position + 1)
        holder.mItemRevenueBinding.tvMovieName.setText(revenue.getMovieName()); // Set movie name
        holder.mItemRevenueBinding.tvQuantity.setText(String.valueOf(revenue.getQuantity())); // Set quantity sold
        String total = revenue.getTotalPrice() + ConstantKey.UNIT_CURRENCY; // Format total price with currency unit
        holder.mItemRevenueBinding.tvTotalPrice.setText(total); // Set total price
    }

    //Method getItemCount()
    @Override
    public int getItemCount() {
        if (mListRevenue != null) {
            return mListRevenue.size();
        }
        return 0;
    }


    // View holder class for revenue item
    public static class AdminRevenueViewHolder extends RecyclerView.ViewHolder {

        private final ItemRevenueBinding mItemRevenueBinding;

        public AdminRevenueViewHolder(@NonNull ItemRevenueBinding itemRevenueBinding) {
            super(itemRevenueBinding.getRoot());
            this.mItemRevenueBinding = itemRevenueBinding;
        }
    }
}
