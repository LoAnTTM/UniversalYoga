package com.example.universalyoga.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * BroadcastReceiver to listen for network connectivity changes.
 * It notifies the user about the network status and triggers data synchronization.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Device is online. Synchronizing data...", Toast.LENGTH_SHORT).show();
            SyncService.getInstance(context).clearAndSyncFirebaseData();
        } else {
            Toast.makeText(context, "Device is offline.", Toast.LENGTH_SHORT).show();
        }
    }
}
