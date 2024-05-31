package com.example.cinema.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema.databinding.ItemCategoryAdminBinding;
import com.example.cinema.model.Category;
import com.example.cinema.util.GlideUtils;

import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {

    private final List<Category> mListCategory; // List to hold categories
    private final IManagerCategoryListener iManagerCategoryListener; // Listener interface for managing category actions

    // Interface for category management listener
    public interface IManagerCategoryListener {
        // Method to handle category edit action
        void editCategory(Category category);
        // Method to handle category delete action

        void deleteCategory(Category category);
    }

    // Constructor
    public AdminCategoryAdapter(List<Category> mListCategory, IManagerCategoryListener iManagerCategoryListener) {
        this.mListCategory = mListCategory;
        this.iManagerCategoryListener = iManagerCategoryListener;
    }

    //Methods
    // Method to create view holder
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryAdminBinding itemCategoryAdminBinding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(itemCategoryAdminBinding);
    }

    // Method to bind data to view holder
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null) {
            return;
        }
        GlideUtils.loadUrl(category.getImage(), holder.mItemCategoryAdminBinding.imgCategory); // Load category image using Glide
        holder.mItemCategoryAdminBinding.tvCategoryName.setText(category.getName());// Set category name
        // Set click listeners for edit and delete icons
        holder.mItemCategoryAdminBinding.imgEdit.setOnClickListener(v -> iManagerCategoryListener.editCategory(category));
        holder.mItemCategoryAdminBinding.imgDelete.setOnClickListener(v -> iManagerCategoryListener.deleteCategory(category));
    }
    // Method to get item count
    @Override
    public int getItemCount() {
        if (mListCategory != null) {
            return mListCategory.size();
        }
        return 0; // Return into 0 if null
    }

    // View holder class for category item
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryAdminBinding mItemCategoryAdminBinding; // Binding for category item views

        public CategoryViewHolder(@NonNull ItemCategoryAdminBinding itemCategoryAdminBinding) {
            super(itemCategoryAdminBinding.getRoot());
            this.mItemCategoryAdminBinding = itemCategoryAdminBinding;
        }
    }
}
