package com.example.cinema.listener;

import android.os.SystemClock;
import android.view.View;

public abstract class IOnSingleClickListener implements View.OnClickListener {

    // Minimum interval between two clicks to consider them as separate clicks
    private static final long MIN_CLICK_INTERVAL = 600;

    // Variable to store the time of the last click
    private long mLastClickTime;

    // Abstract method to be implemented by subclasses to handle single clicks
    public abstract void onSingleClick(View v);

    // Override the onClick method from View.OnClickListener interface
    @Override
    public void onClick(View v) {
        // Get the current time when the click occurred
        long currentClickTime = SystemClock.uptimeMillis();

        // Calculate the elapsed time since the last click
        long elapsedTime = currentClickTime - mLastClickTime;

        // Update the last click time to the current time
        mLastClickTime = currentClickTime;

        // Check if the elapsed time is less than or equal to the minimum click interval
        // If it's within this interval, ignore the click (considered as a rapid multiple click)
        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;

        // If the click is considered a single click, call the onSingleClick method to handle it
        onSingleClick(v);
    }
}
