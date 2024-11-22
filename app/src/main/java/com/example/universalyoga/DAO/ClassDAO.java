package com.example.universalyoga.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.universalyoga.models.Class;

/**
 * Data Access Object (DAO) for accessing class data in the database.
 * This interface defines methods for performing CRUD operations on the Class entity.
 */
@Dao
public interface ClassDAO {
    /**
     * Inserts a new class into the database.
     *
     * @param yogaClass The class object to insert.
     * @return The row ID of the newly inserted class.
     */
    @Insert
    long insertClass(Class yogaClass);

    /**
     * Updates an existing class in the database.
     *
     * @param yogaClass The class object with updated data.
     */
    @Update
    void updateClass(Class yogaClass);

    /**
     * Deletes a specific class from the database.
     *
     * @param yogaClass The class object to delete.
     */
    @Delete
    void deleteClass(Class yogaClass);

    /**
     * Deletes all classes from the database.
     */
    @Query("DELETE FROM classes")
    void deleteAllClasses();

    /**
     * Retrieves all classes from the database.
     *
     * @return LiveData containing a list of all classes.
     */
    @Query("SELECT * FROM classes")
    LiveData<List<Class>> getAllClasses();

    /**
     * Retrieves all classes associated with a specific course ID.
     *
     * @param courseId The ID of the course.
     * @return LiveData containing a list of classes for the specified course.
     */
    @Query("SELECT * FROM classes WHERE course_id = :courseId")
    LiveData<List<Class>> getAllClassesByCourseId(int courseId);

    /**
     * Retrieves a specific class by its ID.
     *
     * @param classId The ID of the class to retrieve.
     * @return LiveData containing the class object.
     */
    @Query("SELECT * FROM classes WHERE classId = :classId")
    LiveData<Class> getClassById(int classId);

    /**
     * Retrieves all classes associated with a specific course ID synchronously.
     *
     * @param courseId The ID of the course.
     * @return A list of classes for the specified course.
     */
    @Query("SELECT * FROM classes WHERE course_id = :courseId")
    List<Class> getAllClassesByCourseIdSync(int courseId);

    /**
     * Searches for classes based on a query string.
     *
     * @param query The search query.
     * @return LiveData containing a list of classes that match the query.
     */
    @Query("SELECT * FROM classes WHERE teacher_name LIKE :query OR type_of_class LIKE :query OR comments LIKE :query OR course_name LIKE :query OR course_day LIKE :query")
    LiveData<List<Class>> searchClasses(String query);
}
