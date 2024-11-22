package com.example.universalyoga.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.network.NetworkUtils;
import com.example.universalyoga.network.SyncService;
import java.util.List;

/**
 * ViewModel for managing course data in the application.
 * It provides methods to interact with the database and synchronize data with Firebase.
 */
public class CourseViewModel extends AndroidViewModel {
    private final YogaDatabase database;
    private final SyncService syncService;

    /**
     * Constructor for CourseViewModel.
     *
     * @param application The application context.
     */
    public CourseViewModel(Application application) {
        super(application);
        database = YogaDatabase.getDatabase(application);
        syncService = SyncService.getInstance(application);
    }

    /**
     * Retrieves a specific course by its ID.
     *
     * @param courseId The ID of the course to retrieve.
     * @return LiveData containing the course object.
     */
    public LiveData<Course> getCourse(int courseId) {
        return database.courseDAO().getCourseById(courseId);
    }

    /**
     * Saves a course to the database. If the course ID is 0, it inserts a new course;
     * otherwise, it updates the existing course.
     *
     * @param course The course object to save.
     */
    public void saveCourse(Course course) {
        new Thread(() -> {
            if (course.getCourseId() == 0) {
                database.courseDAO().insertCourse(course);
            } else {
                database.courseDAO().updateCourse(course);
            }
        }).start();
    }

    /**
     * Deletes a specific course from the database and Firebase.
     * This method also deletes all classes associated with the course.
     *
     * @param course The course object to delete.
     */
    public void deleteCourse(Course course) {
        new Thread(() -> {
            // Fetch all classes associated with the course
            List<Class> classes = database.classDAO().getAllClassesByCourseIdSync(course.getCourseId());

            for (Class yogaClass : classes) {
                database.classDAO().deleteClass(yogaClass);
                syncService.deleteClassFromFirebase(yogaClass.getClassId());
                Toast.makeText(getApplication(), "Class deleted successfully from local database and Firebase.", Toast.LENGTH_SHORT).show();
            }
            database.courseDAO().deleteCourse(course);
            syncService.deleteCourseFromFirebase(course.getCourseId());
            Toast.makeText(getApplication(), "Course deleted successfully from local database and Firebase.", Toast.LENGTH_SHORT).show();
        }).start();
    }

     /**
     * Retrieves all courses from the database.
     *
     * @return LiveData containing a list of all courses.
     */
    public LiveData<List<Course>> getAllCourses() {
        return database.courseDAO().getAllCourses();
    }

    /**
     * Deletes all courses from the database and Firebase.
     */
    public void deleteAllCourses() {
        new Thread(() -> {
            database.courseDAO().deleteAllCourses();
            syncService.deleteAllCoursesFromFirebase();
        }).start();
    }
}
