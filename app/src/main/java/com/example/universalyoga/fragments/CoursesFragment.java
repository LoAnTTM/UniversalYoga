package com.example.universalyoga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.universalyoga.viewmodels.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {
    private CourseViewModel courseViewModel;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private FloatingActionButton addButton;
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

        addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SaveCourseActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void onCourseClick(Course course) {
        if (course != null) {
//            Log.d(TAG, "Opening course details for ID: " + course.getCourseId());
            Intent intent = new Intent(getActivity(), DetailsCourseActivity.class);
            intent.putExtra("course_id", course.getCourseId());
            startActivity(intent);
        }
    }
}
