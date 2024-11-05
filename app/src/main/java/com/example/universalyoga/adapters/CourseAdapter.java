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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private OnCourseClickListener listener;

    // Định nghĩa interface OnCourseClickListener để xử lý các hành động khác nhau trên mỗi item
    public interface OnCourseClickListener {
        void onCourseClick(Course course);
        void onDeleteClick(Course course);
        void onCourseChecked(Course course, boolean isChecked);
    }

    // Constructor
    public CourseAdapter(List<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    // Để cập nhật danh sách các khóa học
    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourse = courseList.get(position);
        holder.bind(currentCourse);
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView courseNameTextView;
        private TextView typeOfClassTextView;
        private CheckBox courseCompletedCheckBox;
        private ImageButton deleteButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các view trong item layout
            courseNameTextView = itemView.findViewById(R.id.course_name);
            typeOfClassTextView = itemView.findViewById(R.id.type_of_class);
            courseCompletedCheckBox = itemView.findViewById(R.id.checkbox_course_completed);
            deleteButton = itemView.findViewById(R.id.button_delete_course);

            // Xử lý sự kiện nhấn vào item để xem chi tiết khóa học
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseClick(courseList.get(position));
                }
            });

            // Xử lý sự kiện nhấn vào nút xóa
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(courseList.get(position));
                }
            });

            // Xử lý sự kiện thay đổi trạng thái của CheckBox (hoàn thành khóa học hay chưa)
            courseCompletedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseChecked(courseList.get(position), isChecked);
                }
            });
        }

        // Phương thức để gán dữ liệu vào các view
        public void bind(Course course) {
            courseNameTextView.setText(course.getCourseName());
            typeOfClassTextView.setText(course.getTypeOfClass());
            courseCompletedCheckBox.setChecked(course.isCompleted()); // Giả sử có trường `isCompleted` để đánh dấu trạng thái khóa học
        }
    }
}
            