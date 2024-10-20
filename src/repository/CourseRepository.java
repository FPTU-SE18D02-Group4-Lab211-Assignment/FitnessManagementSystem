package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Course;

public final class CourseRepository implements ICourseRepository {
    private final ArrayList<Course> courseList = new ArrayList<>();
    
    public CourseRepository() {
        readFile();
    }
    
    public ArrayList<Course> getCourseList() {
        return courseList;
    }
     
    @Override
    public ArrayList<Course> readFile() {
        return courseList;
    }
    
    @Override
    public void writeFile(ArrayList<Course> courses){
        
    }    
}
