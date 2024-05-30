package com.example.cinema.constant;

public class ConstantKey {

    // Email domain format for admin users
    public static final String ADMIN_EMAIL_FORMAT = "@admin.com";

    // Currency unit for general purposes (e.g., food items)
    public static final String UNIT_CURRENCY = " 000 VNĐ";

    // Currency unit specifically for movie tickets
    public static final String UNIT_CURRENCY_MOVIE = " 000 VNĐ / 1 vé";

    // Payment method identifiers
    public static final int PAYMENT_CASH = 1; // Identifier for cash payment
    public static final int PAYMENT_PAYPAL = 2; // Identifier for PayPal payment

    // Payment method titles
    public static final String PAYMENT_CASH_TITLE = "Tiền mặt"; // Title for cash payment
    public static final String PAYMENT_PAYPAL_TITLE = "PayPal"; // Title for PayPal payment

    // Intent keys for passing objects between activities or fragments
    public static final String KEY_INTENT_FOOD_OBJECT = "food_object"; // Key for passing Food object
    public static final String KEY_INTENT_CATEGORY_OBJECT = "category_object"; // Key for passing Category object
    public static final String KEY_INTENT_MOVIE_OBJECT = "movie_object"; // Key for passing Movie object
}
