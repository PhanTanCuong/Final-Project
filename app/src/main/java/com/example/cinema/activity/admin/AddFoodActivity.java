package com.example.cinema.activity.admin;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.BaseActivity;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivityAddFoodBinding;
import com.example.cinema.model.Food;
import com.example.cinema.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends BaseActivity {

    // Declare a variable
    private ActivityAddFoodBinding mActivityAddFoodBinding;
    private boolean isUpdate;
    private Food mFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddFoodBinding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddFoodBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mFood = (Food) bundleReceived.get(ConstantKey.KEY_INTENT_FOOD_OBJECT);
        }

        initView();

        mActivityAddFoodBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivityAddFoodBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditFood());
    }

    //Methods
    // Method to initialize the view-initView
    private void initView() {
        if (isUpdate) {//In update mode
            mActivityAddFoodBinding.tvTitle.setText(getString(R.string.edit_food_title));
            mActivityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_edit));
            mActivityAddFoodBinding.edtName.setText(mFood.getName());
            mActivityAddFoodBinding.edtPrice.setText(String.valueOf(mFood.getPrice()));
            mActivityAddFoodBinding.edtQuantity.setText(String.valueOf(mFood.getQuantity()));
        } else {//In add mode
            mActivityAddFoodBinding.tvTitle.setText(getString(R.string.add_food_title));
            mActivityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    // Method to add or edit a food item
    private void addOrEditFood() {
        String strName = mActivityAddFoodBinding.edtName.getText().toString().trim(); // Get food name from edit text
        String strPrice = mActivityAddFoodBinding.edtPrice.getText().toString().trim(); // Get food price from edit text
        String strQuantity = mActivityAddFoodBinding.edtQuantity.getText().toString().trim(); // Get food quantity from edit text

        // Check if the food name is empty
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_food_require), Toast.LENGTH_SHORT).show(); // Show error message
            return;
        }

        // Check if the food price is empty
        if (StringUtil.isEmpty(strPrice)) {
            Toast.makeText(this, getString(R.string.msg_price_food_require), Toast.LENGTH_SHORT).show(); // Show error message
            return;
        }

        // Check if the food quantity is empty
        if (StringUtil.isEmpty(strQuantity)) {
            Toast.makeText(this, getString(R.string.msg_quantity_food_require), Toast.LENGTH_SHORT).show(); // Show error message
            return;
        }

        // If in update mode
        if (isUpdate) {
            showProgressDialog(true); // Show progress dialog
            Map<String, Object> map = new HashMap<>(); // Create a map to update data
            map.put("name", strName); // Add name to the map
            map.put("price", Integer.parseInt(strPrice)); // Add price to the map
            map.put("quantity", Integer.parseInt(strQuantity)); // Add quantity to the map

            // Update food data in the database
            MyApplication.get(this).getFoodDatabaseReference()
                    .child(String.valueOf(mFood.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Hide progress dialog
                        Toast.makeText(AddFoodActivity.this, getString(R.string.msg_edit_food_successfully), Toast.LENGTH_SHORT).show(); // Show success message
                        GlobalFunction.hideSoftKeyboard(AddFoodActivity.this); // Hide the keyboard
                    });
            return;
        }

        // If in add mode
        showProgressDialog(true); // Show progress dialog
        long foodId = System.currentTimeMillis(); // Generate a food ID using the current time

        // Create a new Food object
        Food food = new Food(foodId, strName, Integer.parseInt(strPrice), Integer.parseInt(strQuantity));
        // Add the new food to the database
        MyApplication.get(this).getFoodDatabaseReference().child(String.valueOf(foodId)).setValue(food, (error, ref) -> {
            showProgressDialog(false); // Hide progress dialog
            mActivityAddFoodBinding.edtName.setText(""); // Clear the name edit text
            mActivityAddFoodBinding.edtPrice.setText(""); // Clear the price edit text
            mActivityAddFoodBinding.edtQuantity.setText(""); // Clear the quantity edit text
            GlobalFunction.hideSoftKeyboard(this); // Hide the keyboard
            Toast.makeText(this, getString(R.string.msg_add_food_successfully), Toast.LENGTH_SHORT).show(); // Show success message
        });
    }
}