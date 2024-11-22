package com.example.universalyoga.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class for network-related operations.
 * It provides methods to check network availability.
 */
public class NetworkUtils {
    /**
     * Checks if the network is available.
     *
     * @param context The application context.
     * @return true if the network is available; false otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
        return false; // Network is not available
    }
}
