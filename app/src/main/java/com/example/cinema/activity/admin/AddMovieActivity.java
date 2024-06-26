package com.example.cinema.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.BaseActivity;
import com.example.cinema.adapter.admin.AdminSelectCategoryAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivityAddMovieBinding;
import com.example.cinema.model.Category;
import com.example.cinema.model.Movie;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Assign: Phan Tấn Cường-20110356
public class AddMovieActivity extends BaseActivity {

    //Declare vars
    private ActivityAddMovieBinding mActivityAddMovieBinding;
    private boolean isUpdate; // Flag to check if we are updating an existing movie or adding a new one
    private Movie mMovie;
    private List<Category> mListCategory;
    private Category mCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddMovieBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mMovie = (Movie) bundleReceived.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        }

        initView();
        getListCategory();

        mActivityAddMovieBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivityAddMovieBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditMovie());
        mActivityAddMovieBinding.tvDate.setOnClickListener(v -> {
            if (isUpdate) {
                GlobalFunction.showDatePicker(AddMovieActivity.this, mMovie.getDate(), date -> mActivityAddMovieBinding.tvDate.setText(date));
            } else {
                GlobalFunction.showDatePicker(AddMovieActivity.this, "", date -> mActivityAddMovieBinding.tvDate.setText(date));
            }
        });
    }

    //Methods
    // Method to initialize the view-initView
    // Method to initialize the view based on whether it's update or add mode
    private void initView() {
        if (isUpdate) { //Update mode
            mActivityAddMovieBinding.tvTitle.setText(getString(R.string.edit_movie_title)); // Set title for edit mode
            mActivityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_edit)); // Set button text to "Edit"

            // Set movie details in the fields
            mActivityAddMovieBinding.edtName.setText(mMovie.getName());
            mActivityAddMovieBinding.edtDescription.setText(mMovie.getDescription());
            mActivityAddMovieBinding.edtPrice.setText(String.valueOf(mMovie.getPrice()));
            mActivityAddMovieBinding.tvDate.setText(mMovie.getDate());
            mActivityAddMovieBinding.edtImage.setText(mMovie.getImage());
            mActivityAddMovieBinding.edtImageBanner.setText(mMovie.getImageBanner());
            mActivityAddMovieBinding.edtVideo.setText(mMovie.getUrl());
        } else { //Add mode
            mActivityAddMovieBinding.tvTitle.setText(getString(R.string.add_movie_title)); // Set title for add mode
            mActivityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_add)); // Set button text to "Add"
        }
    }

    //getListCategory() Method
    //to get the list of categories from the database
    private void getListCategory() {
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListCategory != null) {
                    mListCategory.clear();// Clear the list if it already exists
                } else {
                    mListCategory = new ArrayList<>();// Initialize the list if it's null
                }

                // Add each category from the snapshot to the list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    mListCategory.add(0, category);
                }
                initSpinnerCategory(); // Initialize the category spinner
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {} // Handle the error
        });
    }

    //getPositionCategoryUpdate() Method
    private int getPositionCategoryUpdate(Movie movie) {
        if (mListCategory == null || mListCategory.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < mListCategory.size(); i++) {
            if (movie.getCategoryId() == mListCategory.get(i).getId()) {//if the category ID matches
                return i; // Return the pos
            }
        }
        return 0;
    }


    //initSpinnerCategory() Method
//    Initialize the category spinner with the list of categories.
//    Set the selected category based on the current movie data if updating.
    private void initSpinnerCategory() {
        AdminSelectCategoryAdapter selectCategoryAdapter = new AdminSelectCategoryAdapter(this,
                R.layout.item_choose_option, mListCategory);
        mActivityAddMovieBinding.spnCategory.setAdapter(selectCategoryAdapter);  // Set the adapter to the spinner
        mActivityAddMovieBinding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategorySelected = selectCategoryAdapter.getItem(position); // Set the selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });
        if (isUpdate) {
            mActivityAddMovieBinding.spnCategory.setSelection(getPositionCategoryUpdate(mMovie)); // Set the selection if in update mode
        }
    }

    //addOrEditMovie() Method
    private void addOrEditMovie() {
        // Get input values from the fields
        String strName = mActivityAddMovieBinding.edtName.getText().toString().trim();
        String strDescription = mActivityAddMovieBinding.edtDescription.getText().toString().trim();
        String strPrice = mActivityAddMovieBinding.edtPrice.getText().toString().trim();
        String strDate = mActivityAddMovieBinding.tvDate.getText().toString().trim();
        String strImage = mActivityAddMovieBinding.edtImage.getText().toString().trim();
        String strImageBanner = mActivityAddMovieBinding.edtImageBanner.getText().toString().trim();
        String strVideo = mActivityAddMovieBinding.edtVideo.getText().toString().trim();

        // Validate inputs
        if (mCategorySelected == null || mCategorySelected.getId() <= 0) {
            Toast.makeText(this, getString(R.string.msg_category_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strDescription)) {
            Toast.makeText(this, getString(R.string.msg_description_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strPrice)) {
            Toast.makeText(this, getString(R.string.msg_price_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strDate)) {
            Toast.makeText(this, getString(R.string.msg_date_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImageBanner)) {
            Toast.makeText(this, getString(R.string.msg_image_banner_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strVideo)) {
            Toast.makeText(this, getString(R.string.msg_video_movie_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Update movie if in update mode
        if (isUpdate) {
            showProgressDialog(true); // Show progress dialog
            Map<String, Object> map = new HashMap<>();
            map.put("name", strName);
            map.put("description", strDescription);
            map.put("price", Integer.parseInt(strPrice));
            if (!strDate.equals(mMovie.getDate())) {
                map.put("date", strDate);
                map.put("rooms", GlobalFunction.getListRooms()); // Update rooms if the date has changed
            }
            map.put("image", strImage);
            map.put("imageBanner", strImageBanner);
            map.put("url", strVideo);
            map.put("categoryId", mCategorySelected.getId());
            map.put("categoryName", mCategorySelected.getName());

            // Update movie data in the database
            MyApplication.get(this).getMovieDatabaseReference()
                    .child(String.valueOf(mMovie.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Hide progress dialog
                        Toast.makeText(AddMovieActivity.this, getString(R.string.msg_edit_movie_successfully), Toast.LENGTH_SHORT).show(); // Show success message
                        GlobalFunction.hideSoftKeyboard(this); // Hide the keyboard
                    });
            return;
        }

        // Add new movie if in add mode
        showProgressDialog(true); // Show progress dialog
        long movieId = System.currentTimeMillis(); // Generate a new movie ID based on the current time
        Movie movie = new Movie(movieId, strName, strDescription, Integer.parseInt(strPrice), strDate, strImage, strImageBanner, strVideo, GlobalFunction.getListRooms(), mCategorySelected.getId(), mCategorySelected.getName(), 0);

        // Add new movie to the database
        MyApplication.get(this).getMovieDatabaseReference().child(String.valueOf(movieId)).setValue(movie, (error, ref) -> {
            showProgressDialog(false); // Hide progress dialog
            mActivityAddMovieBinding.spnCategory.setSelection(0); // Reset category selection
            mActivityAddMovieBinding.edtName.setText(""); // Clear name field
            mActivityAddMovieBinding.edtDescription.setText(""); // Clear description field
            mActivityAddMovieBinding.edtPrice.setText(""); // Clear price field
            mActivityAddMovieBinding.tvDate.setText(""); // Clear date field
            mActivityAddMovieBinding.edtImage.setText(""); // Clear image field
            mActivityAddMovieBinding.edtImageBanner.setText(""); // Clear image banner field
            mActivityAddMovieBinding.edtVideo.setText(""); // Clear video URL field
            GlobalFunction.hideSoftKeyboard(this); // Hide the keyboard
            Toast.makeText(this, getString(R.string.msg_add_movie_successfully), Toast.LENGTH_SHORT).show(); // Show success message
        });
    }
}