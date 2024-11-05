package com.example.universalyoga.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import com.example.universalyoga.models.Class;

@Dao
public interface ClassDAO {
    @Insert
    void insertClass(Class yogaClass);

    @Update
    void updateClass(Class yogaClass);

    @Delete
    void deleteClass(Class yogaClass);

    @Query("SELECT * FROM classes WHERE course_id = :courseId")
    LiveData<List<Class>> getAllClasses(int courseId);

    @Query("SELECT * FROM classes WHERE classId = :classId")
    LiveData<Class> getClassById(int classId);

    //Get all classes for a specific teacher
    @Query("SELECT * FROM classes WHERE teacher_name = :teacherName")
    LiveData<List<Class>> getAllClassesByTeacher(String teacherName);

    //Get all classes for a specific type of class
    @Query("SELECT * FROM classes WHERE type_of_class = :typeOfClass")
    LiveData<List<Class>> getAllClassesByType(String typeOfClass);

    //Get all classes for a specific date
    @Query("SELECT * FROM classes WHERE date = :date")
    LiveData<List<Class>> getAllClassesByDate(String date);
}
