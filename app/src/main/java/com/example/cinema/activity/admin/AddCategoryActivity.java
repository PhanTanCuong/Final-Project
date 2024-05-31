package com.example.cinema.activity.admin;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.BaseActivity;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivityAddCategoryBinding;
import com.example.cinema.model.Category;
import com.example.cinema.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

//Assign: Phan Tấn Cường-20110356
public class AddCategoryActivity extends BaseActivity {

    // Declare a variable
    private ActivityAddCategoryBinding mActivityAddCategoryBinding;
    private boolean isUpdate;
    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddCategoryBinding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddCategoryBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mCategory = (Category) bundleReceived.get(ConstantKey.KEY_INTENT_CATEGORY_OBJECT);
        }

        initView();

        mActivityAddCategoryBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivityAddCategoryBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditCategory());
    }

    //Method
    // Method to initialize the view

    private void initView() {
        if (isUpdate) { // If in update mode
            mActivityAddCategoryBinding.tvTitle.setText(getString(R.string.edit_category_title));
            mActivityAddCategoryBinding.btnAddOrEdit.setText(getString(R.string.action_edit));
            mActivityAddCategoryBinding.edtName.setText(mCategory.getName());
            mActivityAddCategoryBinding.edtImage.setText(mCategory.getImage());
        } else {// If in add mode
            mActivityAddCategoryBinding.tvTitle.setText(getString(R.string.add_category_title));
            mActivityAddCategoryBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    // Method to add or edit a category
    private void addOrEditCategory() {
        String strName = mActivityAddCategoryBinding.edtName.getText().toString().trim();
        String strImage = mActivityAddCategoryBinding.edtImage.getText().toString().trim();
        // Check if the category name is empty
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_category_require), Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if the image URL is empty
        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_category_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Update category
        if (isUpdate) {//In update  mode
            showProgressDialog(true); // Show progress dialog
            Map<String, Object> map = new HashMap<>(); // Create a map to update data
            map.put("name", strName); // Add name to the map
            map.put("image", strImage); // Add image URL to the map

            // Update category data in the database
            MyApplication.get(this).getCategoryDatabaseReference()
                    .child(String.valueOf(mCategory.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Hide progress dialog
                        Toast.makeText(AddCategoryActivity.this,
                                getString(R.string.msg_edit_category_successfully), Toast.LENGTH_SHORT).show(); // Show success message
                        GlobalFunction.hideSoftKeyboard(AddCategoryActivity.this); // Hide the keyboard
                    });
            return;
        }

        // Add category
        // In create mode
        showProgressDialog(true); // Show progress dialog
        long categoryId = System.currentTimeMillis(); // Generate a category ID using the current time

        // Create a new Category object
        Category category = new Category(categoryId, strName, strImage);
        // Add the new category to the database
        MyApplication.get(this).getCategoryDatabaseReference().child(String.valueOf(categoryId)).setValue(category, (error, ref) -> {
            showProgressDialog(false); // Hide progress dialog
            mActivityAddCategoryBinding.edtName.setText(""); // Clear the name edit text
            mActivityAddCategoryBinding.edtImage.setText(""); // Clear the image URL edit text
            GlobalFunction.hideSoftKeyboard(this); // Hide the keyboard
            Toast.makeText(this, getString(R.string.msg_add_category_successfully), Toast.LENGTH_SHORT).show(); // Show success message
        });
    }
}