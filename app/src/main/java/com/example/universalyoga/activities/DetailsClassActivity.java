package com.example.universalyoga.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;

public class DetailsClassActivity extends AppCompatActivity {

    private YogaDatabase database;
    private int classId;
    private Class currentClass;

    private TextView classDateTextView, classTypeTextView, classTeacherTextView, classCommentsTextView;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_class);

        database = YogaDatabase.getDatabase(this);

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
    }

    private void initializeViews() {
        classDateTextView = findViewById(R.id.class_date);
        classTypeTextView = findViewById(R.id.class_type);
        classTeacherTextView = findViewById(R.id.class_teacher);
        classCommentsTextView = findViewById(R.id.class_comments);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
    }

    private void loadClassDetails(int classId) {
        LiveData<Class> classData = database.classDAO().getClassById(classId);
        classData.observe(this, yogaClass -> {
            if (yogaClass != null) {
                currentClass = yogaClass;
                displayClassDetails(yogaClass);
            }
        });
    }

    private void displayClassDetails(Class yogaClass) {
        classDateTextView.setText(String.format("Date: %s", yogaClass.getDate()));
        classTypeTextView.setText(String.format("Type: %s", yogaClass.getTypeOfClass()));
        classTeacherTextView.setText(String.format("Teacher: %s", yogaClass.getTeacherName()));
        classCommentsTextView.setText(String.format("Comments: %s", 
            yogaClass.getComments().isEmpty() ? "No Comments" : yogaClass.getComments()));
    }

    private void editClass(Class yogaClass) {
        Intent intent = new Intent(this, SaveClassActivity.class);
        Log.d(TAG, "Opening course details for ID: " + yogaClass.getCourseId());
        intent.putExtra("class_id", yogaClass.getClassId());
        startActivity(intent);
    }

    private void deleteClass(Class yogaClass) {
        new Thread(() -> {
            try {
                database.classDAO().deleteClass(yogaClass);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Class deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error deleting class: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}