package com.example.universalyoga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.R;
import com.example.universalyoga.models.Class;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<Class> classes = new ArrayList<>();
    private OnClassClickListener listener;
    private boolean showDeleteButton;

    public interface OnClassClickListener {
        void onClassClick(Class yogaClass);
        void onDeleteClick(Class yogaClass);
    }

    public ClassAdapter(OnClassClickListener listener, boolean showDeleteButton) {
        this.listener = listener;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView dateText;
        private TextView classTypeText;
        private TextView teacherText;
        private TextView commentText;
        private TextView courseName;
        private ImageButton deleteButton;

        public ClassViewHolder(View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            dateText = itemView.findViewById(R.id.class_date);
            classTypeText = itemView.findViewById(R.id.type_of_class);
            teacherText = itemView.findViewById(R.id.teacher_name);
            commentText = itemView.findViewById(R.id.comment);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class currentClass = classes.get(position);
        holder.courseName.setText(currentClass.getCourseName());
        holder.dateText.setText("Date: " + currentClass.getCourseDay() + ", " + currentClass.getDate());
        holder.classTypeText.setText("Class type: " + currentClass.getTypeOfClass());
        holder.teacherText.setText("Teacher name: " + currentClass.getTeacherName());
        holder.commentText.setText("Comment: " + (currentClass.getComments().isEmpty() ? "No Comments" : currentClass.getComments()));

        if (showDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(currentClass);
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClassClick(classes.get(position));
            }
        });
    }
}
