package service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import model.Course;
import repository.CourseRepository;
import utils.Validation;

public class CourseService implements ICourseService {

    private ArrayList<Course> courseList;
    private CourseRepository courseRepository = new CourseRepository();

    public CourseService() {
        courseList = courseRepository.readFile();
        if (courseList == null) {
            courseList = new ArrayList<>();
        }
    }

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
        ArrayList<Course> courseList = courseRepository.readFile();
        if (courseList.isEmpty()) {
            System.out.println("No courses to display.");
        } else {

            for (Course course : courseList) {
                System.out.printf("| %-6s | %-32s | %-300s | %-8s \n",
                        course.getCourseID(), course.getCourseName(), course.getCourseDescription(),
                        course.getCoursePrice());
            }
        }
    }

    @Override
    public void add(Course c) {
        String courseID = Validation.checkString("Enter course format (CS-YYYY): ", "Course ID must be in the format CS-YYYY, where YYYY are digits.", "CS-\\d{4}");
        String courseName = Validation.checkString("Enter course name: ", "Name cannot be empty.", "^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$");
        String courseDescription = Validation.getValue("Enter course description: ");
        double coursePrice = Validation.checkDouble("Enter course price", "Price must be in double");
        Course newCourse = new Course(courseID, courseName, courseDescription, coursePrice);
        courseList.add(newCourse);
        courseRepository.writeFile(courseList);
        System.out.println("Course added successfully!");

    }

    @Override
    public void update(Course c) {
        String courseID = Validation.getValue("Enter the course ID to update: ");
        Course courseToUpdate = null;

        for (Course course : courseList) {
            if (course.getCourseID().equals(courseID)) {
                courseToUpdate = course;
                break;
            }
        }

        if (courseToUpdate == null) {
            System.out.println("No course found with ID: " + courseID);
            return;
        }

        while (true) {
            Set<String> printedFields = new HashSet<>();
            java.lang.reflect.Field[] field1 = courseToUpdate.getClass().getSuperclass().getDeclaredFields();
            java.lang.reflect.Field[] field2 = courseToUpdate.getClass().getDeclaredFields();

            for (int i = 0; i < field1.length; i++) {
                field1[i].setAccessible(true);
                try {
                    if (!Modifier.isStatic(field1[i].getModifiers()) && !printedFields.contains(field1[i].getName())) {
                        Object value = field1[i].get(courseToUpdate);
                        System.out.println((i + 1) + ": " + field1[i].getName() + " = " + value);
                        printedFields.add(field1[i].getName());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("Error accessing field: " + field1[i].getName());
                }
            }

            // In ra các trường của class hiện tại
            for (int j = 0; j < field2.length; j++) {
                field2[j].setAccessible(true);
                try {
                    if (!Modifier.isStatic(field2[j].getModifiers()) && !printedFields.contains(field2[j].getName())) {
                        Object value = field2[j].get(courseToUpdate);
                        System.out.println((field1.length + j + 1) + ": " + field2[j].getName() + " = " + value);
                        printedFields.add(field2[j].getName());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("Error accessing field: " + field2[j].getName());
                }
            }

            System.out.print("Choose a field to update (1-" + (field1.length + field2.length) + ") or enter 0 to finish: ");
            int choice = Integer.parseInt(Validation.getValue(""));

            if (choice == 0) {
                System.out.println("Update completed.");
                break;
            } else if (choice < 1 || choice > (field1.length + field2.length)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            Field selectedField;
            if (choice <= field1.length) {
                selectedField = field1[choice - 1];
            } else {
                selectedField = field2[choice - field1.length - 1];
            }

            String newValue = Validation.getValue("Enter new value for " + selectedField.getName() + ": ");
            try {
                if (selectedField.getType() == String.class) {
                    selectedField.set(courseToUpdate, newValue);
                } else if (selectedField.getType() == int.class) {
                    selectedField.set(courseToUpdate, Integer.parseInt(newValue));
                } else if (selectedField.getType() == double.class) {
                    selectedField.set(courseToUpdate, Double.parseDouble(newValue));
                } else if (selectedField.getType() == boolean.class) {
                    selectedField.set(courseToUpdate, Boolean.parseBoolean(newValue));
                }
                System.out.println(selectedField.getName() + " updated to " + newValue);
            } catch (IllegalAccessException ex) {
                System.out.println("Error updating field: " + selectedField.getName());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input for " + selectedField.getName() + ". Please enter a valid value.");
            }
        }
        courseRepository.writeFile(courseList);
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
