package com.example.cinema.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.adapter.MovieAdapter;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivitySearchBinding;
import com.example.cinema.model.Category;
import com.example.cinema.model.Movie;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySearchBinding mActivitySearchBinding;
    private List<Category> mListCategory;
    private Category mCategorySelected;
    private List<Movie> mListMovies;

    //Nguyen Quang Vinh
    //Set up the interface, initialize listeners and get a list of movie genres
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchBinding.getRoot());

        initListener();
        getListCategory();
    }

    //Set up click and input events for views such as the "Back", "Delete" and "Search" buttons.
    private void initListener() {
        mActivitySearchBinding.imageBack.setOnClickListener(v -> {
            GlobalFunction.hideSoftKeyboard(SearchActivity.this);
            onBackPressed();
        });
        mActivitySearchBinding.imageDelete.setOnClickListener(v -> mActivitySearchBinding.edtKeyword.setText(""));
        mActivitySearchBinding.edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie();
                return true;
            }
            return false;
        });
        mActivitySearchBinding.edtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mActivitySearchBinding.imageDelete.setVisibility(View.VISIBLE);
                } else {
                    mActivitySearchBinding.imageDelete.setVisibility(View.GONE);
                    searchMovie();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //Get the list of movie genres from Firebase Realtime Database and display it on the interface
    private void getListCategory() {
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.VISIBLE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.VISIBLE);

                if (mListCategory != null) {
                    mListCategory.clear();
                } else {
                    mListCategory = new ArrayList<>();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        mListCategory.add(0, category);
                    }
                }
                mCategorySelected = new Category(0, getString(R.string.label_all), "");
                mListCategory.add(0, mCategorySelected);
                initLayoutCategory("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.GONE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.GONE);
            }
        });
    }

    //Display the category list on the interface by adding TextViews to the FlowLayout.
    private void initLayoutCategory(String tag) {
        mActivitySearchBinding.layoutCategory.removeAllViews();
        if (mListCategory != null && !mListCategory.isEmpty()) {
            for (int i = 0; i < mListCategory.size(); i++) {
                Category category = mListCategory.get(i);

                FlowLayout.LayoutParams params =
                        new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                                FlowLayout.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(SearchActivity.this);
                params.setMargins(0, 10, 20, 10);
                textView.setLayoutParams(params);
                textView.setPadding(30, 10, 30, 10);
                textView.setTag(String.valueOf(category.getId()));
                textView.setText(category.getName());
                if (tag.equals(String.valueOf(category.getId()))) {
                    mCategorySelected = category;
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    searchMovie();
                } else {
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                textView.setTextSize(((int) getResources().getDimension(R.dimen.text_size_small) /
                        getResources().getDisplayMetrics().density));
                textView.setOnClickListener(SearchActivity.this);
                mActivitySearchBinding.layoutCategory.addView(textView);
            }
        }
    }

    //create method searchMovie
    private void searchMovie() {
        MyApplication.get(this).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListMovies != null) {
                    mListMovies.clear();
                } else {
                    mListMovies = new ArrayList<>();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (isMovieResult(movie)) {
                        mListMovies.add(0, movie);
                    }
                }
                displayListMoviesResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //Display movie list to RecyclerView using GridLayoutManager and MovieAdapter
    private void displayListMoviesResult() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mActivitySearchBinding.rcvData.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(mListMovies,
                movie -> GlobalFunction.goToMovieDetail(this, movie));
        mActivitySearchBinding.rcvData.setAdapter(movieAdapter);
    }

    //Check if the movie matches the selected keyword and genre
    private boolean isMovieResult(Movie movie) {
        if (movie == null) {
            return false;
        }
        String key = mActivitySearchBinding.edtKeyword.getText().toString().trim();
        long categoryId = mCategorySelected.getId();
        if (StringUtil.isEmpty(key)) {
            if (categoryId == 0) {
                return true;
            } else return movie.getCategoryId() == categoryId;
        } else {
            boolean isMatch = GlobalFunction.getTextSearch(movie.getName()).toLowerCase().trim()
                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim());
            if (categoryId == 0) {
                return isMatch;
            } else return isMatch && movie.getCategoryId() == categoryId;
        }
    }

    @Override
    public void onClick(View v) {
        initLayoutCategory(v.getTag().toString());
    }
}