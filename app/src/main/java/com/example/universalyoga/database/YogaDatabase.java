package com.example.universalyoga.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.universalyoga.DAO.ClassDAO;
import com.example.universalyoga.DAO.CourseDAO;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;

/**
 * The Room database for the Yoga application.
 * This class serves as the main access point for the underlying SQLite database.
 */
@Database(entities = {Course.class, Class.class}, version = 1)
public abstract class YogaDatabase extends RoomDatabase {

    /**
     * Gets the DAO for accessing course data.
     *
     * @return The CourseDAO instance for accessing course data.
     */
    public abstract CourseDAO courseDAO();

    /**
     * Gets the DAO for accessing class data.
     *
     * @return The ClassDAO instance for accessing class data.
     */
    public abstract ClassDAO classDAO();

    private static YogaDatabase instance;

    /**
     * Gets the singleton instance of the YogaDatabase.
     * If the instance does not exist, it creates a new instance.
     *
     * @param context The application context.
     * @return The singleton instance of YogaDatabase.
     */
    public static YogaDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            YogaDatabase.class, "yoga_database")
                    .build();
        }
        return instance;
    }
}
