package com.example.universalyoga.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.app.Application;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.network.SyncService;

public class CourseViewModel extends AndroidViewModel {
    private final YogaDatabase database;
    private final SyncService syncService;

    public CourseViewModel(Application application) {
        super(application);
        database = YogaDatabase.getDatabase(application);
        syncService = SyncService.getInstance(application);
    }

    public LiveData<Course> getCourse(int courseId) {
        return database.courseDAO().getCourseById(courseId);
    }

    public void saveCourse(Course course) {
        new Thread(() -> {
            if (course.getCourseId() == 0) {
                database.courseDAO().insertCourse(course);
            } else {
                database.courseDAO().updateCourse(course);
            }
        }).start();
    }

    public void deleteCourse(Course course) {
        new Thread(() -> {
            database.courseDAO().deleteCourse(course);
            syncService.deleteCourseFromFirebase(course.getCourseId());
        }).start();
    }
}
