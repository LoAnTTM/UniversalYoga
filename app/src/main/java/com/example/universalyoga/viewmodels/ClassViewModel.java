package com.example.universalyoga.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.app.Application;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.network.SyncService;

import java.util.List;

public class ClassViewModel extends AndroidViewModel {
    private final YogaDatabase database;
    private final SyncService syncService;

    public ClassViewModel(Application application) {
        super(application);
        database = YogaDatabase.getDatabase(application);
        syncService = SyncService.getInstance(application);
    }

    public LiveData<List<Class>> getAllClasses() {
        return database.classDAO().getAllClasses();
    }

    public LiveData<Class> getClass(int classId) {
        return database.classDAO().getClassById(classId);
    }

    public LiveData<List<Class>> getClassesByCourseId(int courseId) {
        return database.classDAO().getAllClassesByCourseId(courseId);
    }

    public void saveClass(Class yogaClass) {
        new Thread(() -> {
            if (yogaClass.getClassId() == 0) {
                database.classDAO().insertClass(yogaClass);
            } else {
                database.classDAO().updateClass(yogaClass);
            }
        }).start();
    }

    public void deleteClass(Class yogaClass) {
        new Thread(() -> {
            database.classDAO().deleteClass(yogaClass);
            syncService.deleteClassFromFirebase(yogaClass.getClassId());
        }).start();
    }

    public void deleteAllClasses() {
        new Thread(() -> {
            database.classDAO().deleteAllClasses();
            syncService.deleteAllClassesFromFirebase();
        }).start();
    }

    public LiveData<List<Class>> searchClasses(String query) {
        return database.classDAO().searchClasses("%" + query + "%");
    }
}