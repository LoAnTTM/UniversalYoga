package com.example.universalyoga.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.universalyoga.R;
import com.example.universalyoga.models.Course;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;
import com.example.universalyoga.viewmodels.CourseViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SummaryFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private ClassViewModel classViewModel;
    private TextView courseSummaryText;
    private TextView classSummaryText;
    private TextView revenueSummaryText;
    private TextView teacherSummaryText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        courseSummaryText = view.findViewById(R.id.course_summary);
        classSummaryText = view.findViewById(R.id.class_summary);
        revenueSummaryText = view.findViewById(R.id.revenue_summary);
        teacherSummaryText = view.findViewById(R.id.teacher_summary);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        loadSummary();

        return view;
    }

    private void loadSummary() {
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            int courseCount = courses != null ? courses.size() : 0;
            courseSummaryText.setText("Courses: " + courseCount);

            if (courses != null) {
                calculateAdditionalSummary(courses);
            }
        });

        classViewModel.getAllClasses().observe(getViewLifecycleOwner(), classes -> {
            int classCount = classes != null ? classes.size() : 0;
            classSummaryText.setText("Classes: " + classCount);
        });
    }

    private static class SummaryData {
        int totalClasses = 0;
        double totalRevenue = 0;
        Set<String> uniqueTeachers = new HashSet<>();
    }

    private void calculateAdditionalSummary(List<Course> courses) {
        SummaryData summaryData = new SummaryData();

        for (Course course : courses) {
            int courseId = course.getCourseId();
            double coursePrice = course.getPricePerClass();
            classViewModel.getClassesByCourseId(courseId).observe(getViewLifecycleOwner(), classes -> {
                if (classes != null) {
                    summaryData.totalClasses += classes.size();
                    summaryData.totalRevenue += classes.size() * coursePrice;
                    for (Class yogaClass : classes) {
                        summaryData.uniqueTeachers.add(yogaClass.getTeacherName());
                    }
                    updateSummaryViews(summaryData);
                }
            });
        }
    }

    private void updateSummaryViews(SummaryData summaryData) {
        revenueSummaryText.setText(String.format(Locale.getDefault(), "Total Revenue: $%.2f", summaryData.totalRevenue));
        teacherSummaryText.setText(String.format(Locale.getDefault(), "Total Teachers: %d", summaryData.uniqueTeachers.size()));
    }
}