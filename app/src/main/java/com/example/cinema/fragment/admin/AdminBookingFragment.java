package com.example.cinema.fragment.admin;

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
import com.example.cinema.adapter.BookingHistoryAdapter;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.FragmentAdminBookingBinding;
import com.example.cinema.event.ResultQrCodeEvent;
import com.example.cinema.listener.IOnSingleClickListener;
import com.example.cinema.model.BookingHistory;
import com.example.cinema.util.DateTimeUtils;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
//Assign: Phan Tấn Cường-20110356
public class AdminBookingFragment extends Fragment {

    private FragmentAdminBookingBinding mFragmentAdminBookingBinding; // Binding object for the fragment layout
    private List<BookingHistory> mListBookingHistory; // List to hold booking history data
    private BookingHistoryAdapter mBookingHistoryAdapter; // Adapter for the booking history RecyclerView
    private boolean mIsUsedChecked; // Flag to check if the "Used" checkbox is checked
    private String mKeyWord = ""; // Keyword for search functionality

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminBookingBinding = FragmentAdminBookingBinding.inflate(inflater, container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        getListBookingHistory(); // Fetch the list of booking history

        return mFragmentAdminBookingBinding.getRoot(); // Return the root view of the fragment
    }

    private void initListener() {
        mFragmentAdminBookingBinding.imgSearch.setOnClickListener(view1 -> searchBooking()); // Set click listener for the search icon
        mFragmentAdminBookingBinding.chbBookingUsed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mIsUsedChecked = isChecked; // Update the used checkbox flag
            getListBookingHistory(); // Fetch the updated list based on the checkbox status
        });

        mFragmentAdminBookingBinding.edtSearchId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBooking(); // Perform search when the search action is triggered from the keyboard
                return true;
            }
            return false;
        });

        mFragmentAdminBookingBinding.edtSearchId.addTextChangedListener(new TextWatcher() {
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
                    mKeyWord = ""; // Reset keyword if the input is empty
                    getListBookingHistory(); // Fetch the list without any keyword filtering
                }
            }
        });
        mFragmentAdminBookingBinding.imgScanQr.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clickOpenScanQRCode(); // Open the QR code scanner
            }
        });
    }

    //searchBooking() Method
    private void searchBooking() {
        mKeyWord = mFragmentAdminBookingBinding.edtSearchId.getText().toString().trim(); // Get the keyword from the input field
        getListBookingHistory(); // Fetch the list based on the keyword
        GlobalFunction.hideSoftKeyboard(getActivity()); // Hide the soft keyboard
    }

    //getListBookingHistory() Method
    public void getListBookingHistory() {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListBookingHistory != null) {
                    mListBookingHistory.clear(); // Clear the existing list
                } else {
                    mListBookingHistory = new ArrayList<>(); // Initialize the list if it's null
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingHistory bookingHistory = dataSnapshot.getValue(BookingHistory.class); // Get booking history object from the snapshot
                    if (bookingHistory != null) {
                        boolean isExpire = DateTimeUtils.convertDateToTimeStamp(bookingHistory.getDate()) < DateTimeUtils.getLongCurrentTimeStamp(); // Check if the booking is expired
                        if (mIsUsedChecked) {
                            if (isExpire || bookingHistory.isUsed()) { // Check if the booking is expired or used
                                if (StringUtil.isEmpty(mKeyWord)) {
                                    mListBookingHistory.add(0, bookingHistory); // Add booking to the list if no keyword is specified
                                } else {
                                    if (String.valueOf(bookingHistory.getId()).contains(mKeyWord)) {
                                        mListBookingHistory.add(0, bookingHistory); // Add booking to the list if it matches the keyword
                                    }
                                }
                            }
                        } else {
                            if (!isExpire && !bookingHistory.isUsed()) { // Check if the booking is not expired and not used
                                if (StringUtil.isEmpty(mKeyWord)) {
                                    mListBookingHistory.add(0, bookingHistory); // Add booking to the list if no keyword is specified
                                } else {
                                    if (String.valueOf(bookingHistory.getId()).contains(mKeyWord)) {
                                        mListBookingHistory.add(0, bookingHistory); // Add booking to the list if it matches the keyword
                                    }
                                }
                            }
                        }
                    }
                }
                displayListBookingHistory(); // Display the booking history list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //displayListBookingHistory() Method
    private void displayListBookingHistory() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()); // Create a linear layout manager
        mFragmentAdminBookingBinding.rcvBookingHistory.setLayoutManager(linearLayoutManager); // Set the layout manager for the RecyclerView

        mBookingHistoryAdapter = new BookingHistoryAdapter(getActivity(), true,
                mListBookingHistory, null, this::updateStatusBooking); // Initialize the adapter
        mFragmentAdminBookingBinding.rcvBookingHistory.setAdapter(mBookingHistoryAdapter); // Set the adapter for the RecyclerView
    }

    //updateStatusBooking() Method
    private void updateStatusBooking(String id) {
        if (getActivity() == null) {
            return; // Return if the activity is null
        }
        MyApplication.get(getActivity()).getBookingDatabaseReference().child(id).child("used").setValue(true,
                (error, ref) -> Toast.makeText(getActivity(), getString(R.string.msg_confirm_booking_success), Toast.LENGTH_SHORT).show());
                // Update the booking status to used and show a confirmation message
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBookingHistoryAdapter != null) mBookingHistoryAdapter.release();// Release resources used by the adapter
    }

    private void clickOpenScanQRCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Quét mã order vé xem phim"); // Set the prompt for the QR code scanner
        intentIntegrator.setCameraId(0); // Use the default camera
        intentIntegrator.setOrientationLocked(true); // Lock the orientation
        intentIntegrator.initiateScan(); // Start the scan
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResultQrCodeEvent event) {
        if (event != null) {
            mFragmentAdminBookingBinding.edtSearchId.setText(event.getResult()); // Set the search input field with the QR code result
            mKeyWord = event.getResult(); // Update the keyword with the QR code result
            getListBookingHistory(); // Fetch the booking history based on the QR code result
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this); // Unregister from EventBus if registered
        }
    }
}
