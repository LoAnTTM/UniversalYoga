package com.example.universalyoga.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Classes")
public class Class {
    @PrimaryKey(autoGenerate = true)
    private int classId;

    @ColumnInfo(name = "course_id")
    @NonNull
    private int courseId;

    @ColumnInfo(name = "course_name")
    private String courseName;

    @ColumnInfo(name = "course_day")
    private String courseDay;

    @ColumnInfo(name = "date")
    @NonNull
    private String date;

    @ColumnInfo(name = "type_of_class")
    @NonNull
    private String typeOfClass;

    @ColumnInfo(name = "teacher_name")
    @NonNull
    private String teacherName;

    @ColumnInfo(name = "comments")
    private String comments;

    // Constructor
    public Class(int courseId, String courseName, String courseDay, String date, String typeOfClass, String teacherName, String comments) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDay = courseDay;
        this.date = date;
        this.typeOfClass = typeOfClass;
        this.teacherName = teacherName;
        this.comments = comments;
    }

    // Getters
    public int getClassId() {
        return classId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getDate() {
        return date;
    }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getComments() {
        return comments;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDay() {
        return courseDay;
    }

    // Setters
    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDay(String courseDay) {
        this.courseDay = courseDay;
    }
}
