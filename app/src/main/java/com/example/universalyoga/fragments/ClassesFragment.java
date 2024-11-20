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
import androidx.appcompat.widget.SearchView;
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
    private SearchView classSearchView;

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
        
        classSearchView = view.findViewById(R.id.class_search_view);
        classSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchClasses(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchClasses(newText);
                return false;
            }
        });

        return view;
    }

    private void searchClasses(String query) {
        classViewModel.searchClasses(query).observe(getViewLifecycleOwner(), classes -> {
            classAdapter.setClasses(classes);
        });
    }

    private void navigateToDetails(Class yogaClass) {
        Intent intent = new Intent(getActivity(), DetailsClassActivity.class);
        intent.putExtra("class_id", yogaClass.getClassId());
        startActivity(intent);
    }
}