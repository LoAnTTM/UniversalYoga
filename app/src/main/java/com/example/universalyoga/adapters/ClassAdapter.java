package com.example.universalyoga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public interface OnClassClickListener {
        void onClassClick(Class yogaClass);
    }

    public ClassAdapter(OnClassClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class currentClass = classes.get(position);
        holder.dateText.setText("Date: " + currentClass.getDate());
        holder.classTypeText.setText("Class type: " + currentClass.getTypeOfClass());
        holder.teacherText.setText("Teacher name: " + currentClass.getTeacherName());
        holder.commentText.setText("Comment: " + (currentClass.getComments().isEmpty() ? "No Comments" : currentClass.getComments()));
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

        public ClassViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.class_date);
            classTypeText = itemView.findViewById(R.id.type_of_class);
            teacherText = itemView.findViewById(R.id.teacher_name);
            commentText = itemView.findViewById(R.id.comment);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClassClick(classes.get(position));
                }
            });
        }
    }
}