package service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Course;
import model.Schedule;
import repository.CourseRepository;
import utils.Validation;

public class CourseService implements ICourseService {

    private final CourseRepository courseRepo = new CourseRepository();

//----------------------------------------------------
    // Helper method to filter available courses

    public List<Course> getAvailableCourses(List<Schedule> userSchedules) {
        Set<String> registeredCourseIDs = new HashSet<>();
        for (Schedule schedule : userSchedules) {
            registeredCourseIDs.add(schedule.getCourseID());
        }

        // Debug: Print registered course IDs
        System.out.println("Registered Course IDs: " + registeredCourseIDs);

        List<Course> availableCourses = new ArrayList<>();
        for (Course course : courseRepo.getCourseList()) {
            if (!registeredCourseIDs.contains(course.getCourseID())) {
                availableCourses.add(course);
            }
        }

        return availableCourses;
    }
//----------------------------------------------------

    public String generateCourseID() {
        int maxId = 0;
        for (Course course : courseRepo.getCourseList()) {
            String[] parts = course.getCourseID().split("-");
            if (parts.length == 2 && parts[0].equals("COU")) {
                try {
                    int idNum = Integer.parseInt(parts[1]);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid course ID format: " + course.getCourseID());
                }
            }
        }
        return "COU-" + String.format("%04d", maxId + 1);
    }
//----------------------------------------------------

    @Override
    public Course findById(String id) {
        for (Course course : courseRepo.getCourseList()) {
            if (course.getCourseID().equals(id)) {
                return course;
            }
        }
        return null;
    }
//----------------------------------------------------

    @Override
    public void display() {
        ArrayList<Course> courseList = courseRepo.readFile();
        if (courseList.isEmpty()) {
            System.out.println("No courses to display.");
        } else {
            System.out.printf("| %-6s | %-20s | %-100s | %-8s | %-5s | %-8s | %-30s |\n",
                    "ID", "Name", "Description", "Price", "Duration", "Coach ID", "Workouts");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            for (Course course : courseList) {

                System.out.printf("| %-6s | %-20s | %-100s | %-8.2f | %-5d | %-8s | %-30s |\n",
                        course.getCourseID(), course.getCourseName(), course.getCourseDescription(),
                        course.getCoursePrice(), course.getCourseDuration(),
                        course.getCoachID(), course.getListOfWorkout());
            }
        }
    }
//----------------------------------------------------

    @Override
    public void add(Course c) {
        if (findById(c.getCourseID()) == null) {
            courseRepo.getCourseList().add(c);
            System.out.println("Course added successfully.");
            save();
        } else {
            System.out.println("Course with ID " + c.getCourseID() + " already exists.");
        }
    }
//----------------------------------------------------

    @Override
    public void delete(Course course) {
        if (course == null) {
            System.err.println("Error: Course not found.");
            return;
        }

        if (courseRepo.getCourseList().remove(course)) {
            System.out.println("Course with ID " + course.getCourseID() + " has been successfully deleted.");
            save();
        } else {
            System.err.println("Error: Failed to delete course with ID " + course.getCourseID());
        }
    }

//----------------------------------------------------
    @Override
    public void update(Course c) {
        CourseService courseSrv = new CourseService();

        Course courseToUpdate = Validation.validateAndFindCourse(courseSrv);
        String courseID = courseToUpdate.getCourseID();

        for (Course course : courseRepo.getCourseList()) {
            if (course.getCourseID().equals(courseID)) {
                courseToUpdate = course;
                break;
            }
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
                    selectedField.set(courseToUpdate, Integer.valueOf(newValue));
                } else if (selectedField.getType() == double.class) {
                    selectedField.set(courseToUpdate, Double.valueOf(newValue));
                } else if (selectedField.getType() == boolean.class) {
                    selectedField.set(courseToUpdate, Boolean.valueOf(newValue));
                }
                System.out.println(selectedField.getName() + " updated to " + newValue);
            } catch (IllegalAccessException ex) {
                System.out.println("Error updating field: " + selectedField.getName());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input for " + selectedField.getName() + ". Please enter a valid value.");
            }
        }
        save();
    }
//----------------------------------------------------

    @Override
    public void save() {
        courseRepo.writeFile(courseRepo.getCourseList());
        System.out.println("Course data has been successfully saved.");
    }

}
