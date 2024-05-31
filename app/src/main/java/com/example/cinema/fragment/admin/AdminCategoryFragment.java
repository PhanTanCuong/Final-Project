package com.example.cinema.fragment.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.admin.AddCategoryActivity;
import com.example.cinema.adapter.admin.AdminCategoryAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.FragmentAdminCategoryBinding;
import com.example.cinema.model.Category;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminCategoryFragment extends Fragment {

    private FragmentAdminCategoryBinding mFragmentAdminCategoryBinding; // Binding object for the fragment layout
    private List<Category> mListCategory; // List to hold category data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminCategoryBinding = FragmentAdminCategoryBinding.inflate(inflater, container, false); // Inflate the fragment layout

        initListener(); // Initialize listeners
        getListCategory(""); // Fetch the list of categories with an empty keyword
        return mFragmentAdminCategoryBinding.getRoot(); // Return the root view of the fragment
    }
    //Methods
    //initListener() Method

    private void initListener() {
        mFragmentAdminCategoryBinding.btnAddCategory.setOnClickListener(v -> onClickAddCategory()); // Set click listener for the add category button

        mFragmentAdminCategoryBinding.imgSearch.setOnClickListener(view1 -> searchCategory()); // Set click listener for the search icon

        mFragmentAdminCategoryBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCategory(); // Perform search when the search action is triggered from the keyboard
                return true;
            }
            return false;
        });

        mFragmentAdminCategoryBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
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
                    getListCategory(""); // Fetch the list without any keyword filtering if input is empty
                }
            }
        });
    }

    //searchCategory() Method
    private void searchCategory() {
        String strKey = mFragmentAdminCategoryBinding.edtSearchName.getText().toString().trim(); // Get the keyword from the input field
        getListCategory(strKey); // Fetch the list based on the keyword
        GlobalFunction.hideSoftKeyboard(getActivity()); // Hide the soft keyboard
    }

    //CRUD
    //onClickAddCategory() method
    private void onClickAddCategory() {
        GlobalFunction.startActivity(getActivity(), AddCategoryActivity.class); // Start the AddCategoryActivity to add a new category
    }

    //onClickEditCategory() Method
    private void onClickEditCategory(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_CATEGORY_OBJECT, category);
        GlobalFunction.startActivity(getActivity(), AddCategoryActivity.class, bundle);// Pass the selected category to the AddCategoryActivity
    }

    private void deleteCategoryItem(Category category) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return; // Return if the activity is null
                    }
                    MyApplication.get(getActivity()).getCategoryDatabaseReference()
                            .child(String.valueOf(category.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_category_successfully), Toast.LENGTH_SHORT).show()); // Delete the selected category and show a confirmation message
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }
    //getListCategory()

    public void getListCategory(String key) {
        if (getActivity() == null) {
            return;
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
                        if (StringUtil.isEmpty(key)) {
                            mListCategory.add(0, category); // Add category to the list if no keyword is specified
                        } else {
                            if (GlobalFunction.getTextSearch(category.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                                mListCategory.add(0, category); // Add category to the list if it matches the keyword
                            }
                        }
                    }
                }
                loadListData(); // Load the category data into the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {// Handle error
            }
        });
    }

    private void loadListData() {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()); // Create a linear layout manager
        mFragmentAdminCategoryBinding.rcvCategory.setLayoutManager(linearLayoutManager); // Set the layout manager for the RecyclerView;

        AdminCategoryAdapter adminCategoryAdapter = new AdminCategoryAdapter(mListCategory,
                new AdminCategoryAdapter.IManagerCategoryListener() {
                    @Override
                    public void editCategory(Category category) {
                        onClickEditCategory(category); // Set the edit category action
                    }

                    @Override
                    public void deleteCategory(Category category) {
                        deleteCategoryItem(category);// Set the delete category action
                    }
                });
        mFragmentAdminCategoryBinding.rcvCategory.setAdapter(adminCategoryAdapter); // Set the adapter for the RecyclerView
    }
}
