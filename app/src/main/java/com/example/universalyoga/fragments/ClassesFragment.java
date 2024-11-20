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
import com.example.universalyoga.activities.DetailsClassActivity;
import com.example.universalyoga.adapters.ClassAdapter;
import com.example.universalyoga.models.Class;
import com.example.universalyoga.viewmodels.ClassViewModel;

public class ClassesFragment extends Fragment {
    private ClassViewModel classViewModel;
    private RecyclerView recyclerView;
    private ClassAdapter classAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        classAdapter = new ClassAdapter(new ClassAdapter.OnClassClickListener() {
            @Override
            public void onClassClick(Class yogaClass) {
                navigateToDetails(yogaClass);
            }

            @Override
            public void onDeleteClick(Class yogaClass) {
                // No action needed as delete button is hidden
            }
        }, false);

        recyclerView.setAdapter(classAdapter);

        classViewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        classViewModel.getAllClasses().observe(getViewLifecycleOwner(), classes -> {
            classAdapter.setClasses(classes);
        });

        return view;
    }

    private void navigateToDetails(Class yogaClass) {
        Intent intent = new Intent(getActivity(), DetailsClassActivity.class);
        intent.putExtra("class_id", yogaClass.getClassId());
        startActivity(intent);
    }
    private void confirmAndDeleteClass(Class yogaClass) {
        // Show confirmation dialog before deleting a class
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this class?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    classViewModel.deleteClass(yogaClass);
                    Toast.makeText(getContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}