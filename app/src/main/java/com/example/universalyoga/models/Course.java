package com.example.universalyoga.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;

    @ColumnInfo(name = "course_name")
    @NonNull
    private String courseName;

    @ColumnInfo(name = "type_of_class")
    @NonNull
    private String typeOfClass;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "day_of_week")
    @NonNull
    private String dayOfWeek;

    @ColumnInfo(name = "time_of_course")
    @NonNull
    private String timeOfCourse;

    @ColumnInfo(name = "duration")
    @NonNull
    private int duration;

    @ColumnInfo(name = "capacity")
    @NonNull
    private int capacity;

    @ColumnInfo(name = "price_per_class")
    @NonNull
    private double pricePerClass;

    @ColumnInfo(name = "skill_level")
    @NonNull
    private String skillLevel;


    // Constructor
    public Course(@NonNull String courseName, @NonNull String typeOfClass, String description,
                  @NonNull String dayOfWeek, @NonNull String timeOfCourse, @NonNull int duration,
                  @NonNull int capacity, @NonNull double pricePerClass, @NonNull String skillLevel) {
        this.courseName = courseName;
        this.typeOfClass = typeOfClass;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.duration = duration;
        this.capacity = capacity;
        this.pricePerClass = pricePerClass;
        this.skillLevel = skillLevel;
    }

    // Getters
    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
                  }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public int getDuration() {
        return duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerClass() {
        return pricePerClass;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    // Setters
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }    

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }    

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPricePerClass(double pricePerClass) {
        this.pricePerClass = pricePerClass;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}
