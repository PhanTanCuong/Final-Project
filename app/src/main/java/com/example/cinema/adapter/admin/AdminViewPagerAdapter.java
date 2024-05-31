package com.example.cinema.adapter.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cinema.fragment.admin.AdminBookingFragment;
import com.example.cinema.fragment.admin.AdminCategoryFragment;
import com.example.cinema.fragment.admin.AdminFoodFragment;
import com.example.cinema.fragment.admin.AdminHomeFragment;
import com.example.cinema.fragment.admin.AdminManageFragment;
//Assign: Phan Tấn Cường-20110356
public class AdminViewPagerAdapter extends FragmentStateAdapter {

    //Constructor
    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //Methods
    //Method Fragment()
    //Creates and returns a fragment based on the given position.

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                // Return AdminFoodFragment for position 1
                return new AdminFoodFragment();
            case 2:
                // Return AdminHomeFragment for position 2
                return new AdminHomeFragment();
            case 3:
                // Return AdminBookingFragment for position 3
                return new AdminBookingFragment();
            case 4:
                // Return AdminManageFragment for position 4
                return new AdminManageFragment();
            default:
                // Return AdminCategoryFragment for position 0 or any other position
                return new AdminCategoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
