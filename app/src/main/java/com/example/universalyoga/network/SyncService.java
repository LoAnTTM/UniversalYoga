package com.example.universalyoga.network;

import android.content.Context;

import com.example.universalyoga.database.YogaDatabase;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SyncService {
    private static SyncService instance;
    private final Context context;
    private final YogaDatabase roomDatabase;
    private final DatabaseReference firebaseDatabase;
    private final NetworkUtils networkUtils;

    private SyncService(Context context) {
        this.context = context;
        this.roomDatabase = YogaDatabase.getDatabase(context);
        this.firebaseDatabase = FirebaseDatabase.getInstance("https://universalyoga-80d91-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        this.networkUtils = new NetworkUtils();
    }

    public static synchronized SyncService getInstance(Context context) {
        if (instance == null) {
            instance = new SyncService(context.getApplicationContext());
        }
        return instance;
    }

    public void clearAndSyncFirebaseData() {
        firebaseDatabase.child("courses").removeValue()
                .addOnSuccessListener(aVoid -> syncCoursesToFirebase());
        firebaseDatabase.child("classes").removeValue()
                .addOnSuccessListener(aVoid -> syncClassesToFirebase());

    }

    private void syncCoursesToFirebase() {
        roomDatabase.courseDAO().getAllCourses().observeForever(courses -> {
            if (courses != null && NetworkUtils.isNetworkAvailable(context)) {
                for (Course course : courses) {
                    firebaseDatabase.child("courses").child(String.valueOf(course.getCourseId())).setValue(course);
                }
            }
        });
    }

    private void syncClassesToFirebase() {
        roomDatabase.classDAO().getAllClasses().observeForever(classes -> {
            if (classes != null && NetworkUtils.isNetworkAvailable(context)) {
                for (Class yogaClass : classes) {
                    firebaseDatabase.child("classes").child(String.valueOf(yogaClass.getClassId())).setValue(yogaClass);
                }
            }
        });
    }

    public void deleteCourseFromFirebase(int courseId) {
        firebaseDatabase.child("courses").child(String.valueOf(courseId)).removeValue();
    }

    public void deleteClassFromFirebase(int classId) {
        firebaseDatabase.child("classes").child(String.valueOf(classId)).removeValue();
    }

    public void deleteAllCoursesFromFirebase() {
        firebaseDatabase.child("courses").removeValue();
    }

    public void deleteAllClassesFromFirebase() {
        firebaseDatabase.child("classes").removeValue();
    }
}
