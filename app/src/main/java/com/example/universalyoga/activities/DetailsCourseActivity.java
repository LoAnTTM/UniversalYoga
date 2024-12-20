package com.example.universalyoga.activities;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universalyoga.MainActivity;
import com.example.universalyoga.R;
import com.example.universalyoga.adapters.ClassAdapter;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;
import com.example.universalyoga.viewmodels.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity to display the details of a specific course.
 * It allows users to view course information, edit or delete the course,
 * and manage associated classes.
 */
public class DetailsCourseActivity extends AppCompatActivity {
    private TextView courseNameText, typeOfClassText, descriptionText, dayOfWeekText,
            timeOfCourseText, durationText, capacityText, priceText, skillLevelText;
    private Button editButton, deleteButton;
    private CourseViewModel courseViewModel;
    private ClassViewModel classViewModel;
    private Course currentCourse;

    private RecyclerView classesRecyclerView;
    private ClassAdapter classAdapter;
    private FloatingActionButton addClassButton;
    private ImageView homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        initializeViews();

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        classesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(new ClassAdapter.OnClassClickListener() {
            @Override
            public void onClassClick(Class yogaClass) {
                Intent intent = new Intent(DetailsCourseActivity.this, SaveClassActivity.class);
                intent.putExtra("class_id", yogaClass.getClassId());
                intent.putExtra("course_name", currentCourse.getCourseName());
                intent.putExtra("course_day", currentCourse.getDayOfWeek());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Class yogaClass) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsCourseActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this class?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    classViewModel.deleteClass(yogaClass);
                    Toast.makeText(DetailsCourseActivity.this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                    loadClassesForCourse(currentCourse.getCourseId());
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        }, true);
        classesRecyclerView.setAdapter(classAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra("course_id")) {
            int courseId = intent.getIntExtra("course_id", -1);
            if (courseId != -1) {
                loadCourseDetails(courseId);
            } else {
                Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        editButton.setOnClickListener(v -> editCourse(currentCourse));
        deleteButton.setOnClickListener(v -> deleteCourse(currentCourse));
        addClassButton.setOnClickListener(v -> startAddClassActivity(currentCourse));

        homeIcon.setOnClickListener(v -> {
            Intent homeIntent = new Intent(DetailsCourseActivity.this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }

    /**
     * Initializes the UI components.
     */
    private void initializeViews() {
        homeIcon = findViewById(R.id.home_icon);
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

        editButton = findViewById(R.id.edit_course_button);
        deleteButton = findViewById(R.id.delete_course_button);
        addClassButton = findViewById(R.id.add_class_button);
    }

    /**
     * Loads the details of the course with the specified ID.
     *
     * @param courseId The ID of the course to load.
     */
    private void loadCourseDetails(int courseId) {
        courseViewModel.getCourse(courseId).observe(this, course -> {
            if (course != null) {
                currentCourse = course;
                displayCourseDetails(course);
                loadClassesForCourse(courseId);
            }
        });
    }

    /**
     * Displays the details of the specified course in the UI.
     *
     * @param course The course to display.
     */
    private void displayCourseDetails(Course course) {
        courseNameText.setText(String.format("Course Name: " + course.getCourseName()));
        typeOfClassText.setText(String.format("Type of Course: " + course.getTypeOfClass()));
        descriptionText.setText(String.format("Description: " + (course.getDescription().isEmpty() ? "No Description" : course.getDescription())));
        dayOfWeekText.setText(String.format("Day of Week: " + course.getDayOfWeek()));
        timeOfCourseText.setText(String.format("Time of Course: " + course.getTimeOfCourse()));
        durationText.setText(String.format("Duration: " + course.getDuration()));
        capacityText.setText(String.format("Capacity: " + course.getCapacity()));
        priceText.setText(String.format("Price: £" + course.getPricePerClass()));
        skillLevelText.setText(String.format("Skill Level: " + course.getSkillLevel()));
    }

    /**
     * Starts the activity to edit the specified course.
     *
     * @param course The course to edit.
     */
    private void editCourse(Course course) {
        Intent intent = new Intent(this, SaveCourseActivity.class);
        intent.putExtra("course_id", course.getCourseId());
        startActivity(intent);
    }

    /**
     * Deletes the specified course after user confirmation.
     *
     * @param course The course to delete.
     */
    private void deleteCourse(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this course?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            courseViewModel.deleteCourse(course);
            Toast.makeText(this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Class --------------------------------------------------------------------------
    /**
     * Loads the classes associated with the specified course ID.
     *
     * @param courseId The ID of the course.
     */
    private void loadClassesForCourse(int courseId) {
        classViewModel.getClassesByCourseId(courseId).observe(this, classes -> {
            if (classes != null) {
                classAdapter.setClasses(classes);
            }
        });
    }

    /**
     * Starts the activity to add a new class for the specified course.
     *
     * @param course The course for which to add a class.
     */
    private void startAddClassActivity(Course course) {
        Intent intent = new Intent(this, SaveClassActivity.class);
        intent.putExtra("course_id", course.getCourseId());
        intent.putExtra("course_name", course.getCourseName());
        intent.putExtra("course_day", course.getDayOfWeek());
        startActivity(intent);
    }

}