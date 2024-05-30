package com.example.cinema.adapter.admin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cinema.R;
import com.example.cinema.model.Category;

import java.util.List;

public class AdminSelectCategoryAdapter extends ArrayAdapter<Category> {

    // Context of the adapter
    private final Context context;

    // Constructor
    public AdminSelectCategoryAdapter(@NonNull Context context, @LayoutRes int resource,
                                      @NonNull List<Category> list) {
        super(context, resource, list);
        this.context = context;
    }
    //Method
    //Method getView()
    //Provides the view for the selected item in the dropdown.

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // Inflate the view for the selected item
            convertView = View.inflate(context, R.layout.item_choose_option, null);
            // Get the TextView for displaying the selected category name
            TextView tvSelected = convertView.findViewById(R.id.tv_selected);
            // Set the text of the TextView to the name of the selected category
            tvSelected.setText(this.getItem(position).getName());
        }
        return convertView;
    }

    //Method getDropDownView()
    //Provides the view for the items in the dropdown list.
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the view for each dropdown item
        View view = View.inflate(context, R.layout.item_drop_down_option, null);
        // Get the TextView for displaying the category name in the dropdown
        TextView tvName = view.findViewById(R.id.textview_name);
        // Set the text of the TextView to the name of the category
        tvName.setText(this.getItem(position).getName());
        return view;
    }
}
