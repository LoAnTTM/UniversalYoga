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
import com.example.universalyoga.MainActivity;
import com.example.universalyoga.R;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;

public class DetailsClassActivity extends AppCompatActivity {
    private ClassViewModel classViewModel;
    private int classId;
    private Class currentClass;

    private TextView courseNameTextView, classDateTextView, classTypeTextView, classTeacherTextView, classCommentsTextView;
    private Button editButton, deleteButton;
    private ImageView homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_class);

        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        classId = getIntent().getIntExtra("class_id", -1);
        if (classId == -1) {
            Toast.makeText(this, "Error: Class not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadClassDetails(classId);

        editButton.setOnClickListener(v -> editClass(currentClass));
        deleteButton.setOnClickListener(v -> deleteClass(currentClass));

        homeIcon.setOnClickListener(v -> {
            Intent homeIntent = new Intent(DetailsClassActivity.this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }

    private void initializeViews() {
        homeIcon = findViewById(R.id.home_icon);
        courseNameTextView = findViewById(R.id.course_name);
        classDateTextView = findViewById(R.id.class_date);
        classTypeTextView = findViewById(R.id.class_type);
        classTeacherTextView = findViewById(R.id.class_teacher);
        classCommentsTextView = findViewById(R.id.class_comments);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
    }

    private void loadClassDetails(int classId) {
        classViewModel.getClass(classId).observe(this, yogaClass -> {
            if (yogaClass != null) {
                currentClass = yogaClass;
                displayClassDetails(yogaClass);
            }
        });
    }

    private void displayClassDetails(Class yogaClass) {
        courseNameTextView.setText(String.format("Course name: " + yogaClass.getCourseName()));
        classDateTextView.setText(String.format("Date: " + yogaClass.getCourseDay() + ", " + yogaClass.getDate()));
        classTypeTextView.setText(String.format("Class type: " + yogaClass.getTypeOfClass()));
        classTeacherTextView.setText(String.format("Teacher: " + yogaClass.getTeacherName()));
        classCommentsTextView.setText(String.format("Comments: %s",
                yogaClass.getComments().isEmpty() ? "No Comments" : yogaClass.getComments()));
    }

    private void editClass(Class yogaClass) {
        Intent intent = new Intent(this, SaveClassActivity.class);
        intent.putExtra("class_id", yogaClass.getClassId());
        startActivity(intent);
    }

    private void deleteClass(Class yogaClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this class?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            classViewModel.deleteClass(yogaClass);
            Toast.makeText(this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}