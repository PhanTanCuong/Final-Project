package com.example.cinema.constant;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class PayPalConfig {

    // PayPal Client ID for the development environment
    // This ID is used to authenticate the app with PayPal's services in the development environment.
    public static final String PAYPAL_CLIENT_ID_DEV = "AYhvByuHhNBY3T28bd5F5fuZky0GG5pPaNlXk_SsO5kBgXuEaHj3I0E5bWZwGbFMsB5tjTyteOnlMHrx";

    // PayPal environment for development
    // ENVIRONMENT_NO_NETWORK is used for testing without actual network calls.
    public static final String PAYPAL_ENVIRONMENT_DEV = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // Default currency for PayPal transactions
    // This specifies the currency that will be used for transactions (e.g., USD).
    public static final String PAYPAL_CURRENCY = "USD";

    // Default content text for PayPal payments
    // This text is used as a description or memo for the payment.
    public static final String PAYPAl_CONTENT_TEXT = "Your Payment";
}
