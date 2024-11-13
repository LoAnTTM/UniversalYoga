package com.example.universalyoga.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Locale;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.universalyoga.R;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.viewmodels.CourseViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;

public class SaveCourseActivity extends AppCompatActivity {
    private EditText courseNameEdit, descriptionEdit, pricePerClassEdit;
    private RadioGroup typeOfClassGroup;
    private ChipGroup dayOfWeekChipGroup;
    private TimePicker timePicker;
    private NumberPicker durationPicker;
    private Slider capacitySlider;
    private TextView capacityValueText;
    private Spinner skillLevelSpinner;
    private Button saveButton;
    private YogaDatabase database;
    private CourseViewModel courseViewModel;
    private Course currentCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_course);

        database = YogaDatabase.getDatabase(this);

        // Initialize views
        initializeViews();
        setupChipGroup();
        setupDurationPicker();
        setupCapacitySlider();

        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Get task data from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("course_id")) {
            int courseId = intent.getIntExtra("course_id", -1);
            Log.d(TAG, "Received course ID: " + courseId);
            if (courseId != -1) {
                courseViewModel.getCourse(courseId).observe(this, course -> {
                    if (course != null) {
                        currentCourse = course;
                        courseNameEdit.setText(course.getCourseName());
                        typeOfClassGroup.check(setRadioButtonId(course.getTypeOfClass()));
                        descriptionEdit.setText(course.getDescription());
                        dayOfWeekChipGroup.check(setDayOfWeek(course.getDayOfWeek()));

                        // Parse time and set to TimePicker
                        String[] timeParts = course.getTimeOfCourse().split(":");
                        int hour = Integer.parseInt(timeParts[0].trim());
                        int minute = Integer.parseInt(timeParts[1].trim().split(" ")[0]);
                        timePicker.setHour(hour);
                        timePicker.setMinute(minute);

                        durationPicker.setValue(course.getDuration());
                        capacitySlider.setValue(course.getCapacity());
                        capacityValueText.setText(String.valueOf(course.getCapacity()));
                        pricePerClassEdit.setText(String.valueOf(course.getPricePerClass()));
                        skillLevelSpinner.setSelection(setSkillLevel(course.getSkillLevel()));
                    }else {
                        Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Set up save button click listener
        saveButton.setOnClickListener(v -> validateAndSaveCourse());
    }

    private void initializeViews() {
        courseNameEdit = findViewById(R.id.course_name);
        typeOfClassGroup = findViewById(R.id.type_of_class_group);
        descriptionEdit = findViewById(R.id.description);
        dayOfWeekChipGroup = findViewById(R.id.day_of_week_chip_group);
        timePicker = findViewById(R.id.time_picker);
        durationPicker = findViewById(R.id.duration_picker);
        capacitySlider = findViewById(R.id.capacity_slider);
        capacityValueText = findViewById(R.id.capacity_value_text);
        pricePerClassEdit = findViewById(R.id.price_per_class);
        skillLevelSpinner = findViewById(R.id.skill_level_spinner);
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

    private void setupChipGroup() {
        dayOfWeekChipGroup.setSingleSelection(true);
    }

    private String getSelectedDayOfWeek() {
        int selectedChipId = dayOfWeekChipGroup.getCheckedChipId();
        if (selectedChipId != View.NO_ID) {
            Chip selectedChip = findViewById(selectedChipId);
            return selectedChip != null ? selectedChip.getText().toString() : "";
        }
        return "";
    }

    private String getTimeFromPicker() {
        // timePicker.setIs24HourView(true);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        
        // Convert 24 hour time to 12 hour format with AM/PM
        String amPm = hour >= 12 ? "PM" : "AM";
        int hour12 = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
        
        // Format time as hh:mm AM/PM
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minute, amPm);
    }

    private void setupDurationPicker() {
        durationPicker.setMinValue(30);  
        durationPicker.setMaxValue(180); // (3 hours)
        durationPicker.setValue(60);
        
        // Set step size to 15 minutes
        durationPicker.setFormatter(value -> String.format("%d min", value));
    }

    private void setupCapacitySlider() {
        capacitySlider.addOnChangeListener((slider, value, fromUser) -> {
            if (capacityValueText != null) {
                capacityValueText.setText(String.format(Locale.getDefault(), 
                    "Selected: %d people", (int) value));
            }
        });
    }

    private void validateAndSaveCourse() {
        String courseName = courseNameEdit.getText().toString().trim();
        String typeOfClass = getSelectedTypeOfClass();
        String description = descriptionEdit.getText().toString().trim();
        String dayOfWeek = getSelectedDayOfWeek();
        String timeOfCourse = getTimeFromPicker();
        int duration = durationPicker.getValue();
        int capacity = (int) capacitySlider.getValue();
        String priceStr = pricePerClassEdit.getText().toString().trim();
        String skillLevel = skillLevelSpinner.getSelectedItem().toString();
        
        double price = Double.parseDouble(priceStr);

        // Validate required fields
        if (courseName.isEmpty() || typeOfClass.isEmpty() || dayOfWeek.isEmpty() || 
            timeOfCourse.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", 
                Toast.LENGTH_LONG).show();
            return;
        }

        if (currentCourse == null) {
            //Save new course
            Course newCourse = new Course(
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
            new Thread(() -> {
                try {
                    long tempCourse = database.courseDAO().insertCourse(newCourse);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SaveCourseActivity.this, DetailsCourseActivity.class);
                        intent.putExtra("course_id", (int) tempCourse);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error saving class: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        } else {
            // Update existing course
            currentCourse.setCourseName(courseName);
            currentCourse.setTypeOfClass(typeOfClass);
            currentCourse.setDescription(description);
            currentCourse.setDayOfWeek(dayOfWeek);
            currentCourse.setTimeOfCourse(timeOfCourse);
            currentCourse.setDuration(duration);
            currentCourse.setCapacity(capacity);
            currentCourse.setPricePerClass(price);
            currentCourse.setSkillLevel(skillLevel);
            courseViewModel.saveCourse(currentCourse);
            Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SaveCourseActivity.this, DetailsCourseActivity.class);
            intent.putExtra("course_id", currentCourse.getCourseId());
            startActivity(intent);
            finish();
        }
    }

    private int setRadioButtonId(String typeOfClass) {
        switch (typeOfClass) {
            case "Flow Yoga":
                return R.id.flow_yoga;
            case "Aerial Yoga":
                return R.id.aerial_yoga;
            case "Family Yoga":
                return R.id.family_yoga;
            default:
                return -1;
        }
    }

    private int setDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Monday":
                return R.id.monday_chip;
            case "Tuesday":
                return R.id.tuesday_chip;
            case "Wednesday":
                return R.id.wednesday_chip;
            case "Thursday":
                return R.id.thursday_chip;
            case "Friday":
                return R.id.friday_chip;
            case "Saturday":
                return R.id.saturday_chip;
            case "Sunday":
                return R.id.sunday_chip;
            default:
                return -1;
        }
    }

    private int setSkillLevel(String skillLevel) {
        String[] skillLevels = getResources().getStringArray(R.array.skill_levels);
        for (int i = 0; i < skillLevels.length; i++) {
            if (skillLevels[i].equals(skillLevel)) {
                return i;
            }
        }
        return 0;
    }
}