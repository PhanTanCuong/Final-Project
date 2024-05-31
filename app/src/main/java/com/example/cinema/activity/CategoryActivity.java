package com.example.cinema.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.adapter.MovieAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivityCategoryBinding;
import com.example.cinema.model.Category;
import com.example.cinema.model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//Nguyen Quang Vinh
//create CategoryActivity class that extends from AppCompatActivity
public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding mActivityCategoryBinding;
    private List<Movie> mListMovies;
    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCategoryBinding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(mActivityCategoryBinding.getRoot());

        getDataIntent();
        initListener();
        getListMovies();
    }


    //Nguyen QUang Vinh
    //This method receives category data from the intent and updates the title (tvTitle) with the category name.
    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mCategory = (Category) bundleReceived.get(ConstantKey.KEY_INTENT_CATEGORY_OBJECT);
            mActivityCategoryBinding.tvTitle.setText(mCategory.getName());
        }
    }


    //This method sets up the listener for the back button (imgBack)
    private void initListener() {
        mActivityCategoryBinding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    //This method gets a list of movies from Firebase Database. When data changes, the movie list will be updated
    private void getListMovies() {
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
                    if (movie != null && mCategory.getId() == movie.getCategoryId()) {
                        mListMovies.add(0, movie);
                    }
                }
                displayListMovies();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    //Navigate to the movie details page when the user selects a movie from the list.
    private void displayListMovies() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mActivityCategoryBinding.rcvData.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(mListMovies,
                movie -> GlobalFunction.goToMovieDetail(this, movie));
        mActivityCategoryBinding.rcvData.setAdapter(movieAdapter);
    }
}