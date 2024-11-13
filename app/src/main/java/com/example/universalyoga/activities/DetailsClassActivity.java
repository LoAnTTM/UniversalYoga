package com.example.universalyoga.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.universalyoga.R;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;

public class DetailsClassActivity extends AppCompatActivity {
    private static final String TAG = "DetailsClassActivity";
    private ClassViewModel classViewModel;
    private int classId;
    private Class currentClass;

    private TextView classDateTextView, classTypeTextView, classTeacherTextView, classCommentsTextView;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_class);

        // Initialize ViewModel
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
        classViewModel.getClass(classId).observe(this, yogaClass -> {
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
        Log.d(TAG, "Opening course ID: " + yogaClass.getCourseId());
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