package com.example.universalyoga;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.universalyoga.fragments.ClassesFragment;
import com.example.universalyoga.fragments.CoursesFragment;
import com.example.universalyoga.network.NetworkReceiver;
import com.example.universalyoga.network.SyncService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        // Register network receiver
        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_courses) {
                    selectedFragment = new CoursesFragment();
                } else if (itemId == R.id.nav_classes) {
                    selectedFragment = new ClassesFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_courses);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receiver
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }
}
