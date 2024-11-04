package view;

import model.Course;
import service.CourseService;
import utils.Validation;

public class CourseView {

    private final CourseService courseService = new CourseService();

    public void displayDeleteCourse() {
        System.out.println("===== Delete Course =====");

        String courseId = Validation.getValue("Enter the Course ID to delete: ");

        Course courseToDelete = courseService.findById(courseId);
        System.out.println(courseToDelete);

        if (courseToDelete != null) {
            String confirmation = Validation.getValue("Are you sure you want to delete this course? (yes/no): ");
            if (confirmation.equalsIgnoreCase("yes")) {
                courseService.delete(courseToDelete);
            } else {
                System.out.println("Deletion canceled.");
            }
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }
}
