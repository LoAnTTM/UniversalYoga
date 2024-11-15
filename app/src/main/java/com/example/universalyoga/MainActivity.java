package com.example.universalyoga;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.activities.DetailsCourseActivity;
import com.example.universalyoga.activities.SaveCourseActivity;
import com.example.universalyoga.adapters.CourseAdapter;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.network.NetworkReceiver;
import com.example.universalyoga.network.SyncService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private YogaDatabase database;
    private FloatingActionButton addButton;
    private List<Course> courses = new ArrayList<>();

    private NetworkReceiver networkReceiver;
    private SyncService syncService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        database = YogaDatabase.getDatabase(this);

        // Initialize sync service
        syncService = SyncService.getInstance(this);

        // Register network receiver
        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        syncService.syncData();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(courses, this::onCourseClick);
        recyclerView.setAdapter(courseAdapter);

        // Initialize Add Course Button
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SaveCourseActivity.class);
            startActivity(intent);
        });

        // Load courses
        loadCourses();
    }

    private void loadCourses() {
//        database.courseDAO().getAllCourses().observe(this, new Observer<List<Course>>() {
//            @Override
//            public void onChanged(List<Course> courses) {
//                if(courses != null){
//                    courseAdapter.setCourses(courses);
//                }
//            }
//        });

        database.courseDAO().getAllCourses().observe(this, courses -> {
            if (courses != null) {
                courseAdapter.setCourses(courses);
            }
        });
    }

    public void onCourseClick(Course course) {
        if (course != null) {
            Log.d(TAG, "Opening course details for ID: " + course.getCourseId());
            Intent intent = new Intent(this, DetailsCourseActivity.class);
            intent.putExtra("course_id", course.getCourseId());
            startActivity(intent);
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