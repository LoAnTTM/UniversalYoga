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
    public Class(int courseId, String date, String typeOfClass, String teacherName, String comments) {
        this.courseId = courseId;
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

    // Setters
    public void setClassId(int id) {
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
}
