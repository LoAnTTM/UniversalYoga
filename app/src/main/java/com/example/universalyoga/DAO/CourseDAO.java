package com.example.universalyoga.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.universalyoga.models.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert
    long insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("SELECT * FROM Courses")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM Courses WHERE courseId = :courseId")
    LiveData<Course> getCourseById(int courseId);
}
