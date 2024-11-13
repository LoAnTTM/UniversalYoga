package com.example.universalyoga.activities;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.viewmodels.ClassViewModel;

import java.util.Calendar;
import java.util.Locale;

public class SaveClassActivity extends AppCompatActivity {
    private EditText datePicker, teacherEdit, commentsEdit;
    private RadioGroup typeOfClassGroup;
    private Button saveButton;
    private ClassViewModel classViewModel;
    private int courseId;
    private Class currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_class);

        // Initialize ViewModel
        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        // Initialize views
        initializeViews();
        setupDatePicker();

        Intent intent = getIntent();
        if (intent.hasExtra("class_id")) {
            int classId = intent.getIntExtra("class_id", -1);
            if (classId != -1) {
                classViewModel.getClass(classId).observe(this, yogaClass -> {
                    if (yogaClass != null) {
                        currentClass = yogaClass;
                        courseId = yogaClass.getCourseId();
                        datePicker.setText(yogaClass.getDate());
                        teacherEdit.setText(yogaClass.getTeacherName());
                        commentsEdit.setText(yogaClass.getComments());
                        if ("Online".equals(yogaClass.getTypeOfClass())) {
                            typeOfClassGroup.check(R.id.online_class);
                        } else if ("Offline".equals(yogaClass.getTypeOfClass())) {
                            typeOfClassGroup.check(R.id.offline_class);
                        }
                    }
                });
            }
        } else if (intent.hasExtra("course_id")) {
            courseId = intent.getIntExtra("course_id", -1);
            Log.d(TAG, "Course ID tai Class: " + courseId);
            if (courseId == -1) {
                Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Invalid parameters", Toast.LENGTH_SHORT).show();
        }

        // Set up save button click listener
        saveButton.setOnClickListener(v -> validateAndSaveClass());
    }

    private void initializeViews() {
        datePicker = findViewById(R.id.date_picker);
        typeOfClassGroup = findViewById(R.id.type_of_class_group);
        teacherEdit = findViewById(R.id.teacher_edit);
        commentsEdit = findViewById(R.id.comments_edit);
        saveButton = findViewById(R.id.save_button);
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); 
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                SaveClassActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.US, "%02d-%02d-%04d",
                        selectedDay, selectedMonth + 1, selectedYear);
                    datePicker.setText(formattedDate);
                },
                year, month, day);
            datePickerDialog.show();
        });
    }

    private String getSelectedTypeOfClass() {
        int selectedId = typeOfClassGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.online_class) {
            return "Online";
        } else if (selectedId == R.id.offline_class) {
            return "Offline";
        }
        return "";
    }

    private void validateAndSaveClass() {
        String date = datePicker.getText().toString().trim();
        String typeOfClass = getSelectedTypeOfClass(); 
        String teacher = teacherEdit.getText().toString().trim();
        String comments = commentsEdit.getText().toString().trim();

        if (date.isEmpty() || typeOfClass.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentClass == null) {
            Log.d(TAG, "Current class: " + currentClass);
            // Save new class
            Class newClass = new Class(courseId, date, typeOfClass, teacher, comments);
            classViewModel.saveClass(newClass);
            Toast.makeText(this, "Class saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing class
            currentClass.setDate(date);
            currentClass.setTypeOfClass(typeOfClass);
            currentClass.setTeacherName(teacher);
            currentClass.setComments(comments);
            classViewModel.saveClass(currentClass);
            Toast.makeText(this, "Class updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}