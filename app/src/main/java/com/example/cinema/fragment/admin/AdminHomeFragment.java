package com.example.cinema.fragment.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.admin.AddMovieActivity;
import com.example.cinema.adapter.admin.AdminMovieAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.FragmentAdminHomeBinding;
import com.example.cinema.model.Category;
import com.example.cinema.model.Movie;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminHomeFragment extends Fragment implements View.OnClickListener {

    private FragmentAdminHomeBinding mFragmentAdminHomeBinding; // Binding object for the fragment layout
    private List<Movie> mListMovies; // List to hold movie data
    private AdminMovieAdapter mAdminMovieAdapter; // Adapter for the RecyclerView

    private List<Category> mListCategory; // List to hold category data
    private Category mCategorySelected; // Currently selected category
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false); // Inflate the fragment layout

        initListener(); // Initialize listeners
        getListCategory(); // Fetch the list of categories
        return mFragmentAdminHomeBinding.getRoot(); // Return the root view of the fragment
    }

    //Method
    //initListener()
    private void initListener() {
        mFragmentAdminHomeBinding.btnAddMovie.setOnClickListener(v -> onClickAddMovie()); // Set click listener for the add movie button

        mFragmentAdminHomeBinding.imgSearch.setOnClickListener(view1 -> searchMovie()); // Set click listener for the search icon

        mFragmentAdminHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie(); // Perform search when the search action is triggered from the keyboard
                return true;
            }
            return false;
        });

        mFragmentAdminHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchMovie(); // Perform search when the search field is cleared
                }
            }
        });
    }

    private void getListCategory() {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        MyApplication.get(getActivity()).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListCategory != null) {
                    mListCategory.clear(); // Clear the existing list
                } else {
                    mListCategory = new ArrayList<>(); // Initialize the list if it's null
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class); // Get category object from the snapshot
                    if (category != null) {
                        mListCategory.add(0, category); // Add category to the list
                    }
                }
                mCategorySelected = new Category(0, getString(R.string.label_all), ""); // Add a default "All" category
                mListCategory.add(0, mCategorySelected);
                initLayoutCategory("0"); // Initialize the category layout with the default "All" category
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //initLayoutCategory()
    private void initLayoutCategory(String tag) {
        mFragmentAdminHomeBinding.layoutCategory.removeAllViews(); // Remove all views from the category layout
        if (mListCategory != null && !mListCategory.isEmpty()) {
            for (int i = 0; i < mListCategory.size(); i++) {
                Category category = mListCategory.get(i);

                FlowLayout.LayoutParams params =
                        new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                                FlowLayout.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(getActivity());
                params.setMargins(0, 10, 20, 10);
                textView.setLayoutParams(params);
                textView.setPadding(30, 10, 30, 10);
                textView.setTag(String.valueOf(category.getId()));
                textView.setText(category.getName());
                if (tag.equals(String.valueOf(category.getId()))) {
                    mCategorySelected = category; // Set the selected category
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    searchMovie(); // Perform search based on the selected category
                } else {
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                textView.setTextSize(((int) getResources().getDimension(R.dimen.text_size_xsmall) /
                        getResources().getDisplayMetrics().density));
                textView.setOnClickListener(this); // Set click listener for each category
                mFragmentAdminHomeBinding.layoutCategory.addView(textView); // Add the category view to the layout
            }
        }
    }

    //CRUD
    private void onClickAddMovie() {
        GlobalFunction.startActivity(getActivity(), AddMovieActivity.class); // Start the AddMovieActivity to add a new movie
    }

    private void onClickEditMovie(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie); // Pass the selected movie to the AddMovieActivity
        GlobalFunction.startActivity(getActivity(), AddMovieActivity.class, bundle);
    }

    private void deleteMovieItem(Movie movie) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return; // Return if the activity is null
                    }
                    MyApplication.get(getActivity()).getMovieDatabaseReference()
                            .child(String.valueOf(movie.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show()); // Delete the selected movie and show a confirmation message
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    public void searchMovie() {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        GlobalFunction.hideSoftKeyboard(getActivity()); // Hide the soft keyboard

        MyApplication.get(getActivity()).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListMovies != null) {
                    mListMovies.clear(); // Clear the existing list
                } else {
                    mListMovies = new ArrayList<>(); // Initialize the list if it's null
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class); // Get movie object from the snapshot
                    if (isMovieResult(movie)) {
                        mListMovies.add(0, movie); // Add movie to the list if it matches the search criteria
                    }
                }
                loadListMovie(); // Load the movie data into the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadListMovie() {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()); // Create a linear layout manager
        mFragmentAdminHomeBinding.rcvMovie.setLayoutManager(linearLayoutManager); // Set the layout manager for the RecyclerView
        mAdminMovieAdapter = new AdminMovieAdapter(getActivity(), mListMovies, new AdminMovieAdapter.IManagerMovieListener() {
            @Override
            public void editMovie(Movie movie) {
                onClickEditMovie(movie);// Set the edit movie action
            }

            @Override
            public void deleteMovie(Movie movie) {
                deleteMovieItem(movie);// Set the delete movie action
            }

            @Override
            public void clickItemMovie(Movie movie) {

            }
        });
        mFragmentAdminHomeBinding.rcvMovie.setAdapter(mAdminMovieAdapter);
    }

    //isMovieResult()
    private boolean isMovieResult(Movie movie) {
        if (movie == null) {
            return false; // Return false if the movie object is null
        }
        String key = mFragmentAdminHomeBinding.edtSearchName.getText().toString().trim(); // Get the search keyword
        long categoryId = 0;
        if (mCategorySelected != null) {
            categoryId = mCategorySelected.getId(); // Get the selected category ID
        }
        if (StringUtil.isEmpty(key)) {
            if (categoryId == 0) {
                return true; // Return true if no keyword and no specific category are selected
            } else return movie.getCategoryId() == categoryId; // Return true if the movie matches the selected category
        } else {
            boolean isMatch = GlobalFunction.getTextSearch(movie.getName()).toLowerCase().trim()
                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim());
            if (categoryId == 0) {
                return isMatch; // Return true if the movie matches the keyword
            } else return isMatch && movie.getCategoryId() == categoryId; // Return true if the movie matches both the keyword and the selected category
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdminMovieAdapter != null) {
            mAdminMovieAdapter.release(); // Release resources held by the adapter
        }
    }

    @Override
    public void onClick(View v) {
        initLayoutCategory(v.getTag().toString()); // Release resources held by the adapter
    }
}
