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

/**
 * Adapter for displaying a list of yoga classes in a RecyclerView.
 * This adapter handles the binding of class data to the UI components.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<Class> classes = new ArrayList<>();
    private OnClassClickListener listener;
    private boolean showDeleteButton;

    /**
     * Interface for handling class click events.
     */
    public interface OnClassClickListener {
        void onClassClick(Class yogaClass);

        void onDeleteClick(Class yogaClass);
    }

    /**
     * Constructor for the ClassAdapter.
     *
     * @param listener The listener for class click events.
     * @param showDeleteButton Flag to indicate if the delete button should be shown.
     */
    public ClassAdapter(OnClassClickListener listener, boolean showDeleteButton) {
        this.listener = listener;
        this.showDeleteButton = showDeleteButton;
    }

    /**
     * Called when RecyclerView needs a new {@link ClassViewHolder} of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ClassViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(itemView);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return classes.size();
    }

    /**
     * Sets the list of classes and notifies the adapter to refresh the UI.
     *
     * @param classes The list of classes to display.
     */
    public void setClasses(List<Class> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for holding the views for each class item.
     */
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

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the {@link ClassViewHolder#itemView} to reflect the item at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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
