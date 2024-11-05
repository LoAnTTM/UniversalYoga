package com.example.universalyoga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;

public class SaveClassActivity extends AppCompatActivity {
    private EditText dateEdit, typeOfClassEdit, teacherEdit, commentsEdit;
    private Button saveButton;
    private YogaDatabase database;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_class);

        courseId = getIntent().getIntExtra("course_id", -1);

        if (courseId == -1) {
            Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        database = YogaDatabase.getDatabase(this);
        initializeViews();
        saveButton.setOnClickListener(v -> validateAndSaveClass());
    }

    private void initializeViews() {
        dateEdit = findViewById(R.id.date_edit);
        typeOfClassEdit = findViewById(R.id.type_of_class_edit);
        teacherEdit = findViewById(R.id.teacher_edit);
        commentsEdit = findViewById(R.id.comments_edit);
        saveButton = findViewById(R.id.save_button);

        // Add date picker for dateEdit
        dateEdit.setOnClickListener(v -> showDatePicker());
    }

    private void validateAndSaveClass() {
        String dates = dateEdit.getText().toString().trim();
        String typeOfClass = typeOfClassEdit.getText().toString().trim();
        String teacher = teacherEdit.getText().toString().trim();
        String comments = commentsEdit.getText().toString().trim();

        if (dates.isEmpty() || typeOfClass.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Class newClass = new Class(courseId, dates, typeOfClass, teacher, comments);

        new Thread(() -> {
            try {
                database.classDAO().insertClass(newClass);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Class saved successfully", 
                                    Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error saving class: " + e.getMessage(), 
                                    Toast.LENGTH_LONG).show();
                });
            }
        }).start();

        Intent intent = new Intent(SaveClassActivity.this, DetailsClassActivity.class);
        startActivity(intent);
    }

    private void showDatePicker() {
        // Implement date picker dialog
    }

}