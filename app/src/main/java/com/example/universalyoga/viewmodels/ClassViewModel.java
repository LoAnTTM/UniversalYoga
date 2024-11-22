package com.example.universalyoga.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.app.Application;
import android.widget.Toast;
import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.network.SyncService;
import java.util.List;

/**
 * ViewModel for managing class data in the application.
 * It provides methods to interact with the database and synchronize data with Firebase.
 */
public class ClassViewModel extends AndroidViewModel {
    private final YogaDatabase database;
    private final SyncService syncService;

    /**
     * Constructor for ClassViewModel.
     *
     * @param application The application context.
     */
    public ClassViewModel(Application application) {
        super(application);
        database = YogaDatabase.getDatabase(application);
        syncService = SyncService.getInstance(application);
    }

    /**
     * Retrieves all classes from the database.
     *
     * @return LiveData containing a list of all classes.
     */
    public LiveData<List<Class>> getAllClasses() {
        return database.classDAO().getAllClasses();
    }

    /**
     * Retrieves a specific class by its ID.
     *
     * @param classId The ID of the class to retrieve.
     * @return LiveData containing the class object.
     */
    public LiveData<Class> getClass(int classId) {
        return database.classDAO().getClassById(classId);
    }

    /**
     * Retrieves all classes associated with a specific course ID.
     *
     * @param courseId The ID of the course.
     * @return LiveData containing a list of classes for the specified course.
     */
    public LiveData<List<Class>> getClassesByCourseId(int courseId) {
        return database.classDAO().getAllClassesByCourseId(courseId);
    }

    /**
     * Saves a class to the database. If the class ID is 0, it inserts a new class;
     * otherwise, it updates the existing class.
     *
     * @param yogaClass The class object to save.
     */
    public void saveClass(Class yogaClass) {
        new Thread(() -> {
            if (yogaClass.getClassId() == 0) {
                database.classDAO().insertClass(yogaClass);
            } else {
                database.classDAO().updateClass(yogaClass);
            }
        }).start();
    }

    /**
     * Deletes a specific class from the database and Firebase.
     *
     * @param yogaClass The class object to delete.
     */
    public void deleteClass(Class yogaClass) {
        new Thread(() -> {
            database.classDAO().deleteClass(yogaClass);
            syncService.deleteClassFromFirebase(yogaClass.getClassId());
            Toast.makeText(getApplication(), "Class deleted successfully from local database and Firebase.", Toast.LENGTH_SHORT).show();
        }).start();
    }

    /**
     * Deletes all classes from the database and Firebase.
     */
    public void deleteAllClasses() {
        new Thread(() -> {
            database.classDAO().deleteAllClasses();
            syncService.deleteAllClassesFromFirebase();
        }).start();
    }

    /**
     * Searches for classes based on a query string.
     *
     * @param query The search query.
     * @return LiveData containing a list of classes that match the query.
     */
    public LiveData<List<Class>> searchClasses(String query) {
        return database.classDAO().searchClasses("%" + query + "%");
    }
}