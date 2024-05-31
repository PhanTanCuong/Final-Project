package com.example.cinema.activity.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.adapter.admin.AdminRevenueAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.databinding.ActivityAdminRevenueBinding;
import com.example.cinema.model.BookingHistory;
import com.example.cinema.model.Revenue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminRevenueActivity extends AppCompatActivity {

    // Declare a variable
    private ActivityAdminRevenueBinding mActivityAdminRevenueBinding;
    private List<Revenue> mListRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminRevenueBinding = ActivityAdminRevenueBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminRevenueBinding.getRoot());

        initListener();
        getListRevenue();
    }

    //Methods
    //initListener() method
    //Sets up a click listener for the back button to call onBackPressed.
    private void initListener() {
        mActivityAdminRevenueBinding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    //getListRevenue()



    private void getListRevenue() {
        //Adds a ValueEventListener to the booking database reference.
        MyApplication.get(this).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//Fetches booking history data from the snapshot and populates a list of BookingHistory.
                List<BookingHistory> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingHistory bookingHistory = dataSnapshot.getValue(BookingHistory.class);
                    if (bookingHistory != null) {
                        list.add(bookingHistory);
                    }
                }
                //    Calls handleDataHistories to process the fetched booking histories.
                handleDataHistories(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //handleDataHistories() Method
    //







    private void handleDataHistories(List<BookingHistory> list) {
        //    Checks if the list of booking histories is null or empty.
        if (list == null || list.isEmpty()) {
            return;
        }
        //    Initializes or clears the revenue list (mListRevenue).
        if (mListRevenue != null) {
            mListRevenue.clear();
        } else {
            mListRevenue = new ArrayList<>();
        }
        //    Iterates through the booking histories to populate the revenue list.
        for (BookingHistory history : list) {
            long movieId = history.getMovieId();
            if (checkRevenueExist(movieId)) {
                getRevenueFromMovieId(movieId).getHistories().add(history);
            } else {
                Revenue revenue = new Revenue();
                revenue.setMovieId(history.getMovieId());
                revenue.setMovieName(history.getName());
                revenue.getHistories().add(history);
                mListRevenue.add(revenue);
            }
        }
        //    Sets up the RecyclerView with a LinearLayoutManager and an AdminRevenueAdapter.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminRevenueBinding.rcvData.setLayoutManager(linearLayoutManager);

        List<Revenue> listFinal = new ArrayList<>(mListRevenue);
        listFinal.sort((statistical1, statistical2)     //    Sorts the revenue list by total price in descending order.
                -> statistical2.getTotalPrice() - statistical1.getTotalPrice());
        AdminRevenueAdapter adminRevenueAdapter = new AdminRevenueAdapter(listFinal);
        mActivityAdminRevenueBinding.rcvData.setAdapter(adminRevenueAdapter);

        // Calculate total
        String strTotalValue = getTotalValues() + ConstantKey.UNIT_CURRENCY;
        mActivityAdminRevenueBinding.tvTotalValue.setText(strTotalValue);//updates the UI with the total value.
    }

    // checkRevenueExist() Method
    //Checks if a revenue entry exists for a given movie ID in the revenue list.
    private boolean checkRevenueExist(long movieId) {
        if (mListRevenue == null || mListRevenue.isEmpty()) {
            return false;
        }
        boolean result = false;
        for (Revenue revenue : mListRevenue) {
            if (movieId == revenue.getMovieId()) {
                result = true;
                break;
            }
        }
        return result;
    }

    // getRevenueFromMovieId() Method
    // Returns the revenue entry for a given movie ID from the revenue list.
    private Revenue getRevenueFromMovieId(long movieId) {
        Revenue result = null;
        for (Revenue revenue : mListRevenue) {
            if (movieId == revenue.getMovieId()) {
                result = revenue;
                break;
            }
        }
        return result;
    }

    //    getTotalValues() Method:
    //    Calculates and returns the total revenue from the revenue list.
    private int getTotalValues() {
        if (mListRevenue == null || mListRevenue.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (Revenue revenue : mListRevenue) {
            total += revenue.getTotalPrice();
        }
        return total;
    }
}