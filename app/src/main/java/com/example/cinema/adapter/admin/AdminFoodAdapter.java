package com.example.cinema.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema.constant.ConstantKey;
import com.example.cinema.databinding.ItemFoodBinding;
import com.example.cinema.model.Food;

import java.util.List;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.FoodViewHolder> {

    // List to hold the food items
    private final List<Food> mListFood;
    // Listener interface to handle edit and delete actions
    private final IManagerFoodListener iManagerFoodListener;

    // Interface for food management listener
    public interface IManagerFoodListener {
        void editFood(Food food); // Method to handle food edit action
        void deleteFood(Food food); // Method to handle food delete action
    }

    //Constructor
    public AdminFoodAdapter(List<Food> list, IManagerFoodListener listener) {
        this.mListFood = list;
        this.iManagerFoodListener = listener;
    }

    //Method
    // Method to create view holder
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = mListFood.get(position);
        if (food == null) {
            return;
        }
        // Bind food data to views in the view holder
        holder.mItemFoodBinding.tvName.setText(food.getName()); // Set food name
        String strPrice = food.getPrice() + ConstantKey.UNIT_CURRENCY; // Format price with currency unit
        holder.mItemFoodBinding.tvPrice.setText(strPrice); // Set food price
        holder.mItemFoodBinding.tvQuantity.setText(String.valueOf(food.getQuantity())); // Set food quantity
        // Set click listeners for edit and delete icons
        holder.mItemFoodBinding.imgEdit.setOnClickListener(v -> iManagerFoodListener.editFood(food));
        holder.mItemFoodBinding.imgDelete.setOnClickListener(v -> iManagerFoodListener.deleteFood(food));
    }

    // Method to get item count
    @Override
    public int getItemCount() {
        if (mListFood != null) {
            return mListFood.size();
        }
        return 0;
    }


    // View holder class for food item
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding mItemFoodBinding;

        public FoodViewHolder(@NonNull ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }
}
