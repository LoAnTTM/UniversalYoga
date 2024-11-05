package com.example.universalyoga.activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.R;
import com.example.universalyoga.adapters.ClassAdapter;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;

public class DetailsCourseActivity extends AppCompatActivity {
    private static final String TAG = "DetailsCourseActivity";

    private TextView courseNameText, typeOfClassText, descriptionText, dayOfWeekText,
            timeOfCourseText, durationText, capacityText, priceText, skillLevelText;
    private YogaDatabase database;
    private Course currentCourse;
    private int courseId;

    private RecyclerView classesRecyclerView;
    private ClassAdapter classAdapter;
    private Button addClassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        // Initialize database and executor
        database = YogaDatabase.getDatabase(this);

        // Initialize views
        initializeViews();

        // Get course ID from intent
        Intent intent = getIntent();
        if (intent.hasExtra("course_id")) {
            int courseId = intent.getIntExtra("course_id", -1);
            Log.d(TAG, "Received course ID: " + courseId);
            if (courseId != -1) {
                loadCourseDetails(courseId);
            }else {
                Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void initializeViews() {
        courseNameText = findViewById(R.id.course_name_text);
        typeOfClassText = findViewById(R.id.type_of_class_text);
        descriptionText = findViewById(R.id.description_text);
        dayOfWeekText = findViewById(R.id.day_of_week_text);
        timeOfCourseText = findViewById(R.id.time_of_course_text);
        durationText = findViewById(R.id.duration_text);
        capacityText = findViewById(R.id.capacity_text);
        priceText = findViewById(R.id.price_text);
        skillLevelText = findViewById(R.id.skill_level_text);


        classesRecyclerView = findViewById(R.id.classes_recycler_view);
        classesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(this::onClassClick);
        classesRecyclerView.setAdapter(classAdapter);

        addClassButton = findViewById(R.id.add_class_button);
        addClassButton.setOnClickListener(v -> startAddClassActivity());
    }

    private void loadCourseDetails(int courseId) {
        LiveData<Course> courseData = database.courseDAO().getCoursesById(courseId);
        courseData.observe(this, course -> {
            if (course != null) {
                currentCourse = course;
                displayCourseDetails(course);
                loadClassesForCourse(courseId);
            }
        });
    }
    

    private void displayCourseDetails(Course course) {
        courseNameText.setText(String.format("Course Name: "+ course.getCourseName()));
        typeOfClassText.setText(String.format("Type of Class: "+ course.getTypeOfClass()));
        descriptionText.setText(String.format("Description: "+ (course.getDescription().isEmpty() ? "No Description" : course.getDescription())));
        dayOfWeekText.setText(String.format("Day of Week: "+ course.getDayOfWeek()));
        timeOfCourseText.setText(String.format("Time of Course: "+ course.getTimeOfCourse()));
        durationText.setText(String.format("Duration: "+ course.getDuration()));
        capacityText.setText(String.format("Capacity: "+ course.getCapacity()));
        priceText.setText(String.format("Price: £"+ course.getPricePerClass()));
        skillLevelText.setText(String.format("Skill Level: "+ course.getSkillLevel()));
    }

    // Add these methods
    private void loadClassesForCourse(int courseId) {
        database.classDAO().getAllClasses(courseId).observe(this, classes -> {
            if (classes != null) {
                classAdapter.setClasses(classes);
            }
        });
    }

    private void startAddClassActivity() {
        Intent intent = new Intent(this, SaveClassActivity.class);
        intent.putExtra("course_id", courseId);
        intent.putExtra("date_of_week", currentCourse.getDayOfWeek());
        startActivity(intent);
    }

    private void onClassClick(Class yogaClass) {
        Intent intent = new Intent(this, DetailsClassActivity.class);
        intent.putExtra("class_id", yogaClass.getClassId());
        startActivity(intent);
    }

}