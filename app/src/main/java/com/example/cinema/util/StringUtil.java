package com.example.cinema.util;

public class StringUtil {

    /**
     * Check if a given CharSequence is a valid email address.
     *
     * @param target The CharSequence to check.
     * @return True if the CharSequence is a valid email address, false otherwise.
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Check if a given string is empty.
     *
     * @param input The string to check.
     * @return True if the string is null, empty, or contains only whitespace, false otherwise.
     */
    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    /**
     * Convert a number to a string with leading zero if it's less than 10.
     *
     * @param number The number to convert.
     * @return A string representation of the number with leading zero if necessary.
     */
    public static String getDoubleNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else return "" + number;
    }
}

