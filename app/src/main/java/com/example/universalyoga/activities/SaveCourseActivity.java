package com.example.universalyoga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import java.util.Locale;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;

public class SaveCourseActivity extends AppCompatActivity {
    private EditText courseNameEdit, descriptionEdit,
            durationEdit, capacityEdit, pricePerClassEdit, skillLevelEdit;
    private RadioGroup typeOfClassGroup;
    private Spinner dayOfWeekSpinner;
    private TimePicker timePicker;
    private Button saveButton;
    private YogaDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_course);

        // Initialize database
        database = YogaDatabase.getDatabase(this);

        // Initialize views
        initializeViews();
        setupTimePicker();

        // Set up save button click listener
        saveButton.setOnClickListener(v -> validateAndSaveCourse());
    }

    private void initializeViews() {
        courseNameEdit = findViewById(R.id.course_name);
        typeOfClassGroup = findViewById(R.id.type_of_class_group);
        descriptionEdit = findViewById(R.id.description);
        dayOfWeekSpinner = findViewById(R.id.day_of_week_spinner);
        timePicker = findViewById(R.id.time_picker);
        durationEdit = findViewById(R.id.duration);
        capacityEdit = findViewById(R.id.capacity);
        pricePerClassEdit = findViewById(R.id.price_per_class);
        skillLevelEdit = findViewById(R.id.skill_level);
        saveButton = findViewById(R.id.save_button);
    }

    private String getSelectedTypeOfClass() {
        int selectedId = typeOfClassGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.flow_yoga) {
            return "Flow Yoga";
        } else if (selectedId == R.id.aerial_yoga) {
            return "Aerial Yoga";
        } else if (selectedId == R.id.family_yoga) {
            return "Family Yoga";
        }
        return "";
    }

    private void setupTimePicker() {
        timePicker.setIs24HourView(true);  // Use 24-hour format
    }
    private String getTimeFromPicker() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        
        // Format time as HH:mm
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    private void validateAndSaveCourse() {
        // Get values from form
        String courseName = courseNameEdit.getText().toString().trim();
        String typeOfClass = getSelectedTypeOfClass();
        String description = descriptionEdit.getText().toString().trim();
        String dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
        String timeOfCourse = getTimeFromPicker();
        String durationStr = durationEdit.getText().toString().trim();
        String capacityStr = capacityEdit.getText().toString().trim();
        String priceStr = pricePerClassEdit.getText().toString().trim();
        String skillLevel = skillLevelEdit.getText().toString().trim();

        // Validate required fields
        if( courseName.isEmpty() || typeOfClass.isEmpty() || dayOfWeek.isEmpty() || timeOfCourse.isEmpty() || 
            durationStr.isEmpty() || capacityStr.isEmpty() || priceStr.isEmpty() || skillLevel.isEmpty() ) {
            Toast.makeText(this, "Please fill in all required fields", 
                         Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Parse numeric values
            int duration = Integer.parseInt(durationStr);
            int capacity = Integer.parseInt(capacityStr);
            double price = Double.parseDouble(priceStr);

            // Create course object
            Course course = new Course(
                    courseName,
                    typeOfClass,
                    description,
                    dayOfWeek,
                    timeOfCourse,
                    duration,
                    capacity,
                    price,
                    skillLevel
            );

            // Save course to database
            saveCourseToDatabase(course);

            

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for duration, capacity, and price", 
                         Toast.LENGTH_LONG).show();
        }
    }

    private void saveCourseToDatabase(Course course) {
        new Thread(() -> {
            try {
                long tempCourse = database.courseDAO().insertCourse(course);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Course saved successfully", 
                                    Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SaveCourseActivity.this, DetailsCourseActivity.class);
                    intent.putExtra("course_id", (int)tempCourse);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error saving course: " + e.getMessage(), 
                                    Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}