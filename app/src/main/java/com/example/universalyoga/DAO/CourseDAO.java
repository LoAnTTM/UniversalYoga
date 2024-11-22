package com.example.universalyoga.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.universalyoga.models.Course;
import java.util.List;

/**
 * Data Access Object (DAO) for accessing course data in the database.
 * This interface defines methods for performing CRUD operations on the Course entity.
 */
@Dao
public interface CourseDAO {
    /**
     * Inserts a new course into the database.
     *
     * @param course The course object to insert.
     * @return The row ID of the newly inserted course.
     */
    @Insert
    long insertCourse(Course course);

    /**
     * Updates an existing course in the database.
     *
     * @param course The course object with updated data.
     */
    @Update
    void updateCourse(Course course);

    /**
     * Deletes a specific course from the database.
     *
     * @param course The course object to delete.
     */
    @Delete
    void deleteCourse(Course course);

    /**
     * Deletes all courses from the database.
     */
    @Query("DELETE FROM courses")
    void deleteAllCourses();

    /**
     * Retrieves all courses from the database.
     *
     * @return LiveData containing a list of all courses.
     */
    @Query("SELECT * FROM Courses")
    LiveData<List<Course>> getAllCourses();

    /**
     * Retrieves a specific course by its ID.
     *
     * @param courseId The ID of the course to retrieve.
     * @return LiveData containing the course object.
     */
    @Query("SELECT * FROM Courses WHERE courseId = :courseId")
    LiveData<Course> getCourseById(int courseId);
}
