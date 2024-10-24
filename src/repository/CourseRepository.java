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
    //private final ArrayList<Course> courseList = new ArrayList<>();
    
    public CourseRepository() {
        readFile();
    }
//    
//    public ArrayList<Course> getCourseList() {
//        return courseList;
//    }
     
@Override
public ArrayList<Course> readFile() {
    ArrayList<Course> courseList = new ArrayList<>(); // Initialize the list

    try (BufferedReader input = new BufferedReader(new FileReader(path + coursePath))) {
        String line;
  
        while ((line = input.readLine()) != null) {
            String[] tokString = line.split("\\|");
            if (tokString.length == 4) {
                try {
                    Course course = new Course(
                            tokString[0].trim(),      
                            tokString[1].trim(),         
                            tokString[2].trim(),        
                            Double.parseDouble(tokString[3].trim())
                    );
                    courseList.add(course);  
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price for course: " + tokString[0]);
                }
            } else {
                System.err.println("Malformed line: " + line); 
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
    }
    return courseList; 
}
    @Override
    public void writeFile(ArrayList<Course> courses){
          try {
            BufferedWriter output = new BufferedWriter(new FileWriter(path + coursePath));

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
