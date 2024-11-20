package com.example.universalyoga.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.R;
import com.example.universalyoga.activities.DetailsCourseActivity;
import com.example.universalyoga.activities.SaveCourseActivity;
import com.example.universalyoga.adapters.CourseAdapter;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.viewmodels.ClassViewModel;
import com.example.universalyoga.viewmodels.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {
    private CourseViewModel courseViewModel;
    private ClassViewModel classViewModel;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private FloatingActionButton addButton, deleteAllButton;
    private List<Course> courses = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseAdapter = new CourseAdapter(courses,this::onCourseClick);
        recyclerView.setAdapter(courseAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            courseAdapter.setCourses(courses);
        });

        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SaveCourseActivity.class);
            startActivity(intent);
        });

        deleteAllButton = view.findViewById(R.id.delete_all_button);
        deleteAllButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all courses and their related classes?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAllCoursesAndClasses())
                .setNegativeButton("No", null)
                .show();
        });

        return view;
    }

    public void onCourseClick(Course course) {
        if (course != null) {
            Intent intent = new Intent(getActivity(), DetailsCourseActivity.class);
            intent.putExtra("course_id", course.getCourseId());
            startActivity(intent);
        }
    }

    private void deleteAllCoursesAndClasses() {
        new Thread(() -> {
            classViewModel.deleteAllClasses();
            courseViewModel.deleteAllCourses();
    
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "All courses and related classes deleted successfully", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
