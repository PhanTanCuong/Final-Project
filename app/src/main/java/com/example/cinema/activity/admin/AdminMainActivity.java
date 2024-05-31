package com.example.cinema.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinema.R;
import com.example.cinema.activity.BaseActivity;
import com.example.cinema.adapter.admin.AdminViewPagerAdapter;
import com.example.cinema.databinding.ActivityAdminMainBinding;
import com.example.cinema.event.ResultQrCodeEvent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
//Assign: Phan Tấn Cường-20110356
@SuppressLint("NonConstantResourceId")  //Suppresses lint warnings related to non-constant resource IDs.
public class AdminMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminMainBinding activityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(activityAdminMainBinding.getRoot());

        // Set up the view pager with an adapter and disable user input
        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        activityAdminMainBinding.viewpager2.setAdapter(adminViewPagerAdapter);
        activityAdminMainBinding.viewpager2.setUserInputEnabled(false);

        // Register a page change callback for the view pager

        activityAdminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Updates the selected item in the bottom navigation and the title text based on the current page of the ViewPager2.
                switch (position) {
                    case 0:
                        activityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_category).setChecked(true);
                        activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_category));
                        break;

                    case 1:
                        activityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_food_drink).setChecked(true);
                        activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_food_drink));
                        break;

                    case 2:
                        activityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_movie).setChecked(true);
                        activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_movie));
                        break;

                    case 3:
                        activityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_booking).setChecked(true);
                        activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_booking));
                        break;

                    case 4:
                        activityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_manage).setChecked(true);
                        activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_manage));
                        break;
                }
            }
        });

        // Set up item selected listener for the bottom navigation
        activityAdminMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            // Update the view pager current item and title based on the selected item
            switch (id) {
                case R.id.nav_admin_category:
                    activityAdminMainBinding.viewpager2.setCurrentItem(0);
                    activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_category));
                    break;

                case R.id.nav_admin_food_drink:
                    activityAdminMainBinding.viewpager2.setCurrentItem(1);
                    activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_food_drink));
                    break;

                case R.id.nav_admin_movie:
                    activityAdminMainBinding.viewpager2.setCurrentItem(2);
                    activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_movie));
                    break;

                case R.id.nav_admin_booking:
                    activityAdminMainBinding.viewpager2.setCurrentItem(3);
                    activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_booking));
                    break;

                case R.id.nav_admin_manage:
                    activityAdminMainBinding.viewpager2.setCurrentItem(4);
                    activityAdminMainBinding.tvTitle.setText(getString(R.string.nav_admin_manage));
                    break;

            }
            return true;
        });
    }

    //Methods
    // showDialogLogout() method
    // Displays a confirmation dialog to log out.
    // Uses MaterialDialog to build the dialog.
    // On positive action, it dismisses the dialog and finishes all activities using finishAffinity().
    // On negative action, it simply dismisses the dialog.
    private void showDialogLogout() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_confirm_login_another_device))
                .positiveText(getString(R.string.action_ok))
                .negativeText(getString(R.string.action_cancel))
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    finishAffinity();// Close all activities and exit the app
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .cancelable(true)
                .show();
    }

    //onActivityResult() Method
    //Handles results from other activities, specifically QR code scanning.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);//Uses IntentIntegrator.parseActivityResult to parse the result.
        if (intentResult != null && intentResult.getContents() != null) {
            EventBus.getDefault().post(new ResultQrCodeEvent(intentResult.getContents()));//Posts a ResultQrCodeEvent to EventBus if a QR code is scanned.
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //onBackPressed() method
    //Overrides the back button press to show the logout confirmation dialog instead of the default back behavior.
    @Override
    public void onBackPressed() {
        showDialogLogout();
    }
}