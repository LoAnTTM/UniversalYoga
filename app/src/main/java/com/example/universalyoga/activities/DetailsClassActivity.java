package com.example.universalyoga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;

public class DetailsClassActivity extends AppCompatActivity {

    private YogaDatabase database;
    private int classId;
    private Class yogaClass;

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

        editButton.setOnClickListener(v -> editClass());
        deleteButton.setOnClickListener(v -> deleteClass());
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
        new Thread(() -> {
            try {
                yogaClass = database.classDAO().getClassById(classId).getValue();
                runOnUiThread(() -> {
                    classDateTextView.setText(yogaClass.getDate());
                    classTypeTextView.setText(yogaClass.getTypeOfClass());
                    classTeacherTextView.setText(yogaClass.getTeacherName());
                    classCommentsTextView.setText(yogaClass.getComments());
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading class: " + e.getMessage(), 
                                    Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void editClass() {
        Intent intent = new Intent(this, SaveClassActivity.class);
        intent.putExtra("class_id", classId);
        startActivity(intent);
    }

    private void deleteClass() {
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