package view;

import java.util.ArrayList;
import java.util.List;
import model.Coach;
import model.Course;
import model.Workout;
import service.CoachService;
import service.CourseService;
import service.WorkoutService;
import utils.Validation;

public class CourseView {

    private final CourseService courseSrv = new CourseService();
    private final CoachService coachSrv = new CoachService();
    private final WorkoutService workoutService = new WorkoutService(); // Assuming there's a WorkoutService for handling workout data

    public void displayAddCourse() throws Exception {
        System.out.println("===== Add Course =====");

        // Validate and retrieve the coach for the course
        Coach coach = Validation.validateAndFindCoach(coachSrv);
        if (coach == null) {
            System.err.println("Coach not found.");
            return;
        }

        // Generate a new course ID
        String courseID = courseSrv.generateCourseID(); // Assuming generateCourseID() is a method in CourseService
        System.out.println("New Course ID: " + courseID);

        // Gather input for the new course
        String courseName = Validation.checkString("Enter course name: ", "Name cannot be empty.", "^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$");
        String courseDescription = Validation.getValue("Enter course description: ");
        int courseDuration = Validation.checkInt("Enter course duration (number of workouts): ", "Duration must be a positive integer.");
        double coursePrice = Validation.checkDouble("Enter course price: ", "Price must be a valid number.");

        // Get workout IDs
        String workoutInput = Validation.getValue("Enter workout IDs separated by commas (e.g., WOR-0001,WOR-0002,...): ");
        String[] workoutIDs = workoutInput.split(",");

        // Ensure the workout IDs match the course duration
        if (workoutIDs.length != courseDuration) {
            System.err.println("Error: The number of workout IDs must match the course duration.");
            return;
        }

        // Validate workout IDs and create a list of workout IDs for the course
        List<String> listOfWorkout = new ArrayList<>();
        for (String workoutID : workoutIDs) {
            workoutID = workoutID.trim();
            Workout workout = workoutService.findById(workoutID);
            if (workout != null) {
                listOfWorkout.add(workout.getId());
            } else {
                System.err.println("Warning: Workout ID " + workoutID + " not found.");
            }
        }

        // Create and add the new course
        Course newCourse = new Course(courseID, courseName, courseDescription, courseDuration, coursePrice, coach, listOfWorkout);
        courseSrv.add(newCourse); // Assuming add method in CourseService handles adding to the repository and writing to file

        System.out.println("Course added successfully!");
    }

    public void displayDeleteCourse() {
        System.out.println("===== Delete Course =====");

        Course courseToDelete = Validation.validateAndFindCourse(courseSrv);

        System.out.println(courseToDelete);

        String confirmation = Validation.getValue("Are you sure you want to delete this course? (yes/no): ");
        if (confirmation.equalsIgnoreCase("yes")) {
            courseSrv.delete(courseToDelete);
        } else {
            System.out.println("Deletion canceled.");
        }
    }

}
