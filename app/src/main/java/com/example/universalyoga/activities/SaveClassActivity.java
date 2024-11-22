package com.example.universalyoga.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.universalyoga.R;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;

import java.util.Calendar;
import java.util.Locale;

public class SaveClassActivity extends AppCompatActivity {
    private TextView courseNameText, courseDayText;
    private EditText datePicker, teacherEdit, commentsEdit;
    private RadioGroup typeOfClassGroup;
    private Button saveButton, cancelButton;
    private ClassViewModel classViewModel;
    private int courseId;
    private Class currentClass;
    private String courseName;
    private String courseDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_class);

        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

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
                        courseName = intent.getStringExtra("course_name");
                        courseDay = intent.getStringExtra("course_day");
                        if (courseName == null && courseDay == null) {
                            courseNameText.setText(String.format("Course Name: %s", yogaClass.getCourseName()));
                            courseDayText.setText(String.format("Day of week: %s", yogaClass.getCourseDay()));   
                        } else {
                            courseNameText.setText(String.format("Course Name: %s", courseName));
                            courseDayText.setText(String.format("Day of week: %s", courseDay));                   
                        }
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
            courseName = intent.getStringExtra("course_name");
            courseDay = intent.getStringExtra("course_day");
            courseNameText.setText(String.format("Course Name: %s", courseName));
            courseDayText.setText(String.format("Day of week: %s", courseDay));
            if (courseId == -1) {
                Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Invalid parameters", Toast.LENGTH_SHORT).show();
        }

        saveButton.setOnClickListener(v -> validateAndSaveClass());
        cancelButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Do you really want to leave without saving this class?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void initializeViews() {
        datePicker = findViewById(R.id.date_picker);
        typeOfClassGroup = findViewById(R.id.type_of_class_group);
        teacherEdit = findViewById(R.id.teacher_edit);
        commentsEdit = findViewById(R.id.comments_edit);
        courseNameText = findViewById(R.id.course_name_text);
        courseDayText = findViewById(R.id.course_day_text);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
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
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        int selectedDayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);
                        int targetDayOfWeek = getDayOfWeek(courseDay);
                        if (selectedDayOfWeek == targetDayOfWeek) {
                            String formattedDate = String.format(Locale.US, "%02d-%02d-%04d",
                                    selectedDay, selectedMonth + 1, selectedYear);
                            datePicker.setText(formattedDate);
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("Invalid Date")
                                    .setMessage("Please select a " + courseDay)
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        datePicker.performClick();
                                    }).show();
                        }
                    },
                    year, month, day);
            // Disable all dates before today
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private int getDayOfWeek(String dayName) {
        switch (dayName.toLowerCase(Locale.US)) {
            case "sunday":
                return Calendar.SUNDAY;
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            default:
                throw new IllegalArgumentException("Invalid day: " + dayName);
        }
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
            Class newClass = new Class(courseId, courseName, courseDay, date, typeOfClass, teacher, comments);
            classViewModel.saveClass(newClass);
            Toast.makeText(this, "Class saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            currentClass.setCourseName(courseName);
            currentClass.setCourseDay(courseDay);
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