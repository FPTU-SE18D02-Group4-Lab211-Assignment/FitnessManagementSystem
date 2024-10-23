package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        String line;
        try {
            BufferedReader input = new BufferedReader(new FileReader(path + coursePath));
            ArrayList<Course> courseList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
            
            while ((line = input.readLine()) != null) {
                String[] tokString = line.split(",");
                LocalDate dayOfBirth =  LocalDate.parse(tokString[2], formatter);
                Course course = new Course(
                        tokString[0], 
                        tokString[1],
                        tokString[2],        
                        Double.parseDouble(tokString[3])
                );
                courseList.add(course);
            }
            input.close();
            return courseList;
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void writeFile(ArrayList<Course> courses){
          try {
            BufferedWriter output = new BufferedWriter(new FileWriter(path + coursePath));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 

            for (Course course : courses) {
                output.write(
                        course.getCourseID() + "," +
                        course.getCourseName() + "," +
                        course.getCourseDescription() + "," +
                        course.getCoursePrice()
                );
                output.newLine();
            }
            output.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }    
}
