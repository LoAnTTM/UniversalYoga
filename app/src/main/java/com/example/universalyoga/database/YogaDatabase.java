package com.example.universalyoga.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.universalyoga.DAO.ClassDAO;
import com.example.universalyoga.DAO.CourseDAO;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;

@Database(entities = {Course.class, Class.class}, version = 1)
public abstract class YogaDatabase extends RoomDatabase {

    public abstract CourseDAO courseDAO();
    public abstract ClassDAO classDAO();

    private static YogaDatabase instance;

    public static YogaDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            YogaDatabase.class, "yoga_database")
                    .build();
        }
        return instance;
    }
}
