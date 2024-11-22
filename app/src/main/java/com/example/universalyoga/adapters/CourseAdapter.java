package com.example.universalyoga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.R;
import com.example.universalyoga.models.Course;

import java.util.List;

/**
 * Adapter for displaying a list of courses in a RecyclerView.
 * This adapter handles the binding of course data to the UI components.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private OnCourseClickListener listener;

    /**
     * Interface for handling course click events.
     */
    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

     /**
     * Constructor for the CourseAdapter.
     *
     * @param courses The list of courses to display.
     * @param listener The listener for course click events.
     */
    public CourseAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courseList = courses;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link CourseViewHolder} of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new CourseViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    /**
     * Sets the list of courses and notifies the adapter to refresh the UI.
     *
     * @param courses The list of courses to display.
     */
    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * ViewHolder class for holding the views for each course item.
     */
    class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView courseNameTextView;
        private TextView typeOfClassTextView;
        private TextView dayOfWeekTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.course_name);
            typeOfClassTextView = itemView.findViewById(R.id.type_of_class);
            dayOfWeekTextView = itemView.findViewById(R.id.day_of_week);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseClick(courseList.get(position));
                }
            });
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the {@link CourseViewHolder#itemView} to reflect the item at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourse = courseList.get(position);
        holder.courseNameTextView.setText(currentCourse.getCourseName());
        holder.typeOfClassTextView.setText("Type of course: " + currentCourse.getTypeOfClass());
        holder.dayOfWeekTextView.setText("Day of week: " + currentCourse.getDayOfWeek());
    }
}