package com.example.cinema.prefs;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.cinema.model.User;
import com.example.cinema.util.StringUtil;
import com.google.gson.Gson;

public class DataStoreManager {
    // Key for storing user information in SharedPreferences
    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";

    private static DataStoreManager instance; // Singleton instance
    private MySharedPreferences sharedPreferences; // Instance of SharedPreferences wrapper

    // Initialize the DataStoreManager with context
    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    // Retrieve the singleton instance of DataStoreManager
    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    // Save user information to SharedPreferences
    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon(); // Convert User object to JSON string
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    // Retrieve user information from SharedPreferences
    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            return new Gson().fromJson(jsonUser, User.class); // Convert JSON string to User object
        }
        return new User(); // Return a default User object if no user information found
    }
}
