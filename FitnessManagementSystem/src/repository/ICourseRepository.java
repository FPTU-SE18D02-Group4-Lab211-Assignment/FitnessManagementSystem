package repository;

import java.util.ArrayList;
import model.Course;

public interface ICourseRepository extends Repository<Course, ArrayList<Course>> {
    final String coursePath = "\\data\\course.csv";
    
    @Override
    public ArrayList<Course> readFile();
    
    @Override
    public void writeFile(ArrayList<Course> courses);
}
