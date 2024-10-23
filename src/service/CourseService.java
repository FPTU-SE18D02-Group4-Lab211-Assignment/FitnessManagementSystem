package service;

import java.util.ArrayList;
import model.Course;

public class CourseService implements ICourseService {

    private final ArrayList<Course> courseList = new ArrayList<>();
    
    @Override
    public Course findById(String id) {
          for (Course course : courseList) {
            if (course.getCourseID().equals(id)) {
                return course;
            }
        }
        return null;
    }

    @Override
    public void display() {

    }

    @Override
    public void add(Course c) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Course c) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
