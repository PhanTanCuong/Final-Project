package com.example.cinema.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinema.activity.ChangePasswordActivity;
import com.example.cinema.activity.SignInActivity;
import com.example.cinema.activity.admin.AdminRevenueActivity;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.FragmentAdminManageBinding;
import com.example.cinema.prefs.DataStoreManager;
import com.google.firebase.auth.FirebaseAuth;

//Assign: Phan Tấn Cường-20110356

public class AdminManageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        FragmentAdminManageBinding fragmentAdminManageBinding = FragmentAdminManageBinding.inflate(inflater, container, false);

        // Set the email address of the current user in the TextView
        fragmentAdminManageBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        // Set click listeners for different layout components
        fragmentAdminManageBinding.layoutReport.setOnClickListener(v -> onClickReport());
        fragmentAdminManageBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAdminManageBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());

        // Return the root view of the fragment
        return fragmentAdminManageBinding.getRoot();
    }
    //Methods

    // Method to handle click on the report layout
    private void onClickReport() {
        // Start the AdminRevenueActivity to view the revenue reports
        GlobalFunction.startActivity(getActivity(), AdminRevenueActivity.class);
    }

    // Method to handle click on the change password layout
    private void onClickChangePassword() {
        // Start the ChangePasswordActivity to change the user's password
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    // Method to handle click on the sign-out layout
    private void onClickSignOut() {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        FirebaseAuth.getInstance().signOut(); // Sign out the user from Firebase Authentication
        DataStoreManager.setUser(null); // Clear the user data from DataStoreManager
        GlobalFunction.startActivity(getActivity(), SignInActivity.class); // Start the SignInActivity to sign in again
        getActivity().finishAffinity(); // Finish all activities and clear the activity stack
    }
}
