package com.example.universalyoga.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
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

/**
 * Activity for saving or updating a course.
 * This activity allows users to input course details and save them to the database.
 */
public class SaveCourseActivity extends AppCompatActivity {
    private EditText courseNameEdit, descriptionEdit, pricePerClassEdit;
    private RadioGroup typeOfClassGroup;
    private ChipGroup dayOfWeekChipGroup;
    private TimePicker timePicker;
    private NumberPicker durationPicker;
    private Slider capacitySlider;
    private TextView capacityValueText;
    private Spinner skillLevelSpinner;
    private Button saveButton, cancelButton;
    private YogaDatabase database;
    private CourseViewModel courseViewModel;
    private Course currentCourse;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_course);

        database = YogaDatabase.getDatabase(this);

        initializeViews();
        setupChipGroup();
        setupDurationPicker();
        setupCapacitySlider();

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra("course_id")) {
            int courseId = intent.getIntExtra("course_id", -1);
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
                    } else {
                        Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        saveButton.setOnClickListener(v -> validateAndSaveCourse());
        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Do you really want to leave without saving this course?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    /**
     * Initializes the views in the activity.
     */
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
        cancelButton = findViewById(R.id.cancel_button);
    }

    /**
     * Gets the selected type of class from the RadioGroup.
     *
     * @return The selected type of class.
     */
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

    /**
     * Sets up the ChipGroup for selecting the day of the week.
     */
    private void setupChipGroup() {
        dayOfWeekChipGroup.setSingleSelection(true);
    }

    /**
     * Gets the selected day of the week from the ChipGroup.
     *
     * @return The selected day of the week.
     */
    private String getSelectedDayOfWeek() {
        int selectedChipId = dayOfWeekChipGroup.getCheckedChipId();
        if (selectedChipId != View.NO_ID) {
            Chip selectedChip = findViewById(selectedChipId);
            return selectedChip != null ? selectedChip.getText().toString() : "";
        }
        return "";
    }

    /**
     * Gets the time from the TimePicker.
     *
     * @return The selected time as a string.
     */
    private String getTimeFromPicker() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String amPm = hour >= 12 ? "PM" : "AM";
        int hour12 = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minute, amPm);
    }

    /**
     * Sets up the NumberPicker for selecting the duration of the course.
     */
    private void setupDurationPicker() {
        durationPicker.setMinValue(30);
        durationPicker.setMaxValue(180);
        durationPicker.setValue(30);
        durationPicker.setFormatter(value -> String.format("%d min", value));
    }

    /**
     * Sets up the Slider for selecting the capacity of the course.
     */
    private void setupCapacitySlider() {
        capacitySlider.addOnChangeListener((slider, value, fromUser) -> {
            if (capacityValueText != null) {
                capacityValueText.setText(String.format(Locale.getDefault(),
                        "Selected: %d people", (int) value));
            }
        });
    }

    /**
     * Validates the input fields and saves the course to the database.
     */
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
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        priceStr = decimalFormat.format(price);
        price = Double.parseDouble(priceStr);

        if (courseName.isEmpty() || typeOfClass.isEmpty() || dayOfWeek.isEmpty() ||
                timeOfCourse.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (currentCourse == null) {
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

    /**
     * Sets the RadioButton ID based on the type of class.
     *
     * @param typeOfClass The type of class.
     * @return The RadioButton ID.
     */
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

    /**
     * Sets the Chip ID based on the day of the week.
     *
     * @param dayOfWeek The day of the week.
     * @return The Chip ID.
     */
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

    /**
     * Sets the Spinner selection based on the skill level.
     *
     * @param skillLevel The skill level.
     * @return The Spinner selection index.
     */
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