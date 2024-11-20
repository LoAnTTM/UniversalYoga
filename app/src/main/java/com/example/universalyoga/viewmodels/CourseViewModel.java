package com.example.universalyoga.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.network.NetworkUtils;
import com.example.universalyoga.network.SyncService;

import java.util.List;

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

    //  public void deleteCourse(Course course) {
    //     new Thread(() -> {
    //         database.courseDAO().deleteCourse(course);
    //         syncService.deleteCourseFromFirebase(course.getCourseId());
    //     }).start();
    //  }

     public void deleteCourse(Course course) {
        new Thread(() -> {
            // Fetch all classes associated with the course
            List<Class> classes = database.classDAO().getAllClassesByCourseIdSync(course.getCourseId());
    
            // Delete each class from the Room database
            for (Class yogaClass : classes) {
                database.classDAO().deleteClass(yogaClass);
                if (NetworkUtils.isNetworkAvailable(getApplication())) {
                    syncService.deleteClassFromFirebase(yogaClass.getClassId());
                }
            }
    
            // Delete the course from the Room database
            database.courseDAO().deleteCourse(course);
    
            // Delete the course from Firebase
            if (NetworkUtils.isNetworkAvailable(getApplication())) {
                syncService.deleteCourseFromFirebase(course.getCourseId());
            }
        }).start();
    }

    public LiveData<List<Course>> getAllCourses() {
        return database.courseDAO().getAllCourses();
    }

    public void deleteAllCourses() {
        new Thread(() -> {
            database.courseDAO().deleteAllCourses();
            syncService.deleteAllCoursesFromFirebase();
        }).start();
    }
}
