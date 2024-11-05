package view;

import model.Course;
import service.CourseService;
import utils.Validation;

public class CourseView {

    private final CourseService courseSrv = new CourseService();

    public void displayDeleteCourse() {
        System.out.println("===== Delete Course =====");

        Course courseToDelete = Validation.validateAndFindCourse(courseSrv);
        String courseId = courseToDelete.getCourseID();

        System.out.println(courseToDelete);

        String confirmation = Validation.getValue("Are you sure you want to delete this course? (yes/no): ");
        if (confirmation.equalsIgnoreCase("yes")) {
            courseSrv.delete(courseToDelete);
        } else {
            System.out.println("Deletion canceled.");
        }

    }
}
