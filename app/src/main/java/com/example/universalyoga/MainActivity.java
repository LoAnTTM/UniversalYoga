package com.example.universalyoga;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.universalyoga.fragments.ClassesFragment;
import com.example.universalyoga.fragments.CoursesFragment;
import com.example.universalyoga.fragments.SummaryFragment;
import com.example.universalyoga.network.NetworkReceiver;
import com.example.universalyoga.network.SyncService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity that serves as the entry point for the application.
 * It manages the bottom navigation and displays the appropriate fragments.
 */
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NetworkReceiver networkReceiver;
    private SyncService syncService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sync service
        syncService = SyncService.getInstance(this);

        // Register network receiver to listen for connectivity changes
        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_courses) {
                selectedFragment = new CoursesFragment();
            } else if (itemId == R.id.nav_classes) {
                selectedFragment = new ClassesFragment();
            } else if (itemId == R.id.nav_summary) {
                selectedFragment = new SummaryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_courses);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the network receiver to prevent memory leaks
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }
}
