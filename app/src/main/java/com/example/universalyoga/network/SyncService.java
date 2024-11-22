package com.example.universalyoga.network;

import android.content.Context;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Service for synchronizing data between the local database and Firebase.
 * It handles data uploads and deletions for courses and classes.
 */
public class SyncService {
    private static SyncService instance;
    private final Context context;
    private final YogaDatabase roomDatabase;
    private final DatabaseReference firebaseDatabase;
    private final NetworkUtils networkUtils;

    /**
     * Private constructor for SyncService.
     *
     * @param context The application context.
     */
    private SyncService(Context context) {
        this.context = context;
        this.roomDatabase = YogaDatabase.getDatabase(context);
        this.firebaseDatabase = FirebaseDatabase.getInstance("https://universalyoga-80d91-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        this.networkUtils = new NetworkUtils();
    }

    /**
     * Gets the singleton instance of SyncService.
     *
     * @param context The application context.
     * @return The singleton instance of SyncService.
     */
    public static synchronized SyncService getInstance(Context context) {
        if (instance == null) {
            instance = new SyncService(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Clears existing data in Firebase and synchronizes local data with Firebase.
     */
    public void clearAndSyncFirebaseData() {
        firebaseDatabase.child("courses").removeValue()
                .addOnSuccessListener(aVoid -> syncCoursesToFirebase());
        firebaseDatabase.child("classes").removeValue()
                .addOnSuccessListener(aVoid -> syncClassesToFirebase());

    }

    /**
     * Synchronizes all courses from the local database to Firebase.
     */
    private void syncCoursesToFirebase() {
        roomDatabase.courseDAO().getAllCourses().observeForever(courses -> {
            if (courses != null && NetworkUtils.isNetworkAvailable(context)) {
                for (Course course : courses) {
                    firebaseDatabase.child("courses").child(String.valueOf(course.getCourseId())).setValue(course);
                }
            }
        });
    }

    /**
     * Synchronizes all classes from the local database to Firebase.
     */
    private void syncClassesToFirebase() {
        roomDatabase.classDAO().getAllClasses().observeForever(classes -> {
            if (classes != null && NetworkUtils.isNetworkAvailable(context)) {
                for (Class yogaClass : classes) {
                    firebaseDatabase.child("classes").child(String.valueOf(yogaClass.getClassId())).setValue(yogaClass);
                }
            }
        });
    }

    /**
     * Deletes a specific course from Firebase.
     *
     * @param courseId The ID of the course to delete.
     */
    public void deleteCourseFromFirebase(int courseId) {
        firebaseDatabase.child("courses").child(String.valueOf(courseId)).removeValue();
    }

    /**
     * Deletes a specific class from Firebase.
     *
     * @param classId The ID of the class to delete.
     */
    public void deleteClassFromFirebase(int classId) {
        firebaseDatabase.child("classes").child(String.valueOf(classId)).removeValue();
    }

    /**
     * Deletes all courses from Firebase.
     */
    public void deleteAllCoursesFromFirebase() {
        firebaseDatabase.child("courses").removeValue();
    }

    /**
     * Deletes all classes from Firebase.
     */
    public void deleteAllClassesFromFirebase() {
        firebaseDatabase.child("classes").removeValue();
    }
}
