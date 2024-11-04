package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import model.Schedule;
import service.ScheduleService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Course;
import model.User;
import repository.CourseRepository;
import repository.ScheduleRepository;
import repository.UserRepository;
import service.CourseService;
import service.UserService;
import utils.Utils;

public class ScheduleView {

    private final ScheduleService scheduleSrv = new ScheduleService();
    private final ScheduleRepository scheduleRepo = new ScheduleRepository();
    private final CourseRepository courseRepo = new CourseRepository();
    private final UserRepository userRepo = new UserRepository();
    private final CourseService courseSrv = new CourseService();
    private final UserService userSrv = new UserService();
    private final Scanner scanner = new Scanner(System.in);

    public ScheduleView() {
    }
//----------------------------------------------------

    public void viewUserSchedule() {

        System.out.print("Enter User ID to view schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input

        List<Schedule> schedule;

        // Ask the user whether they want to view the whole schedule or a specific course schedule
        System.out.print("Do you want to view a specific course schedule or the whole schedule? (Enter 'course' or 'whole'): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (null == choice) {
            System.out.println("Invalid choice. Please enter 'course' or 'whole'.");
        } else {
            switch (choice) {
                case "course": {
                    System.out.print("Enter Course ID to view schedule: ");
                    String courseID = scanner.nextLine(); // Get Course ID from input

                    schedule = scheduleRepo.readFileWithUserCourseID(userID, courseID);

                    // Display the first week's schedule
                    scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

                    // Display whole schedule
                    scheduleSrv.displayWholeScheduleForCourse(schedule);
                    break;
                }
                case "whole": {
                    schedule = scheduleRepo.readFileWithUserID(userID);

                    // Display the first week's schedule
                    scheduleSrv.displayWeeklyScheduleForUser(schedule, 1);

                    // Display whole schedule
                    scheduleSrv.displayWholeScheduleForUser(schedule);

                    break;
                }
                default:
                    System.out.println("Invalid choice. Please enter 'course' or 'whole'.");
                    break;
            }
        }
    }
//----------------------------------------------------

    public void viewEditUserSchedule() throws IOException {
        System.out.print("Enter User ID to edit schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to edit schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        List<Schedule> schedule = scheduleRepo.readFileWithUserCourseID(userID, courseID);

        // Display the first week's schedule
        scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

        // Display whole schedule
        scheduleSrv.displayWholeScheduleForCourse(schedule);

        // Menu for moving workouts
        System.out.println("Choose an option to move workouts:");
        System.out.println("1. Move a workout from a specific date");
        System.out.println("2. Move all workouts in a week");
        System.out.println("3. Move workouts from a set of weeks");
        System.out.println("4. Move all remaining workouts after today");
        System.out.print("Enter your choice (1-4): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Perform the chosen operation
        switch (choice) {
            case 1:
                scheduleSrv.moveWorkoutFromDate(schedule); // Move a specific workout
                break;
            case 2:
                scheduleSrv.moveAllWorkoutsInWeek(schedule); // Move all workouts in a week
                break;
            case 3:
                scheduleSrv.moveWorkoutsFromWeeks(schedule); // Move workouts from a set of weeks
                break;
            case 4:
                scheduleSrv.moveRemainingWorkouts(schedule); // Move all remaining workouts
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }

        schedule = scheduleRepo.readFileWithUserCourseID(userID, courseID);

        // Display the first week's schedule
        scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

        // Display whole schedule
        scheduleSrv.displayWholeScheduleForCourse(schedule);
    }
//----------------------------------------------------

    public void viewToCompleteWorkouts() {
        System.out.print("Enter User ID to edit schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to edit schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        List<Schedule> schedules = scheduleRepo.readFileWithUserCourseID(userID, courseID);
        scheduleSrv.viewIncompleteWorkoutsBeforePresent(schedules);

        boolean continueCompleting = true; // Flag to control the loop

        while (continueCompleting) {
            String workoutID = Utils.getValue("Enter Workout ID to mark as completed: "); // Get Workout ID from input
            // Prompt the user for the order of the workout to mark it as completed
            int order = Integer.parseInt(Utils.getValue("Enter the order of the workout: ")); // Get order from input

            // Mark the workout as completed
            scheduleSrv.markWorkoutAsCompleted(schedules, workoutID, order);

            // Save changes to the file using the replaceFile method
            for (Schedule schedule : schedules) {
                if (schedule.getWorkoutID().equals(workoutID) && schedule.getOrder() == order) {
                    scheduleRepo.replaceFile(schedule); // Save the updated schedule
                    break; // Exit loop after saving the relevant schedule
                }
            }

            // Ask the user if they want to continue
            System.out.print("Would you like to complete another workout? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (!response.equals("y")) {
                continueCompleting = false; // Exit the loop if the user doesn't want to continue
            }
        }

        System.out.println("Workout completion process ended.");
    }

//----------------------------------------------------
    public void viewUpcomingWorkouts() {
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine(); // Get User ID from input

        List<Schedule> schedule = scheduleRepo.readFileWithUserID(userID);

        if (schedule.isEmpty()) {
            System.out.println("No workouts found for this user.");
            return;
        }

        // Call the service method to view upcoming workouts
        scheduleSrv.viewUpcomingWorkouts(schedule);
    }

//----------------------------------------------------
    public void viewUsersProgress() {
        // Retrieve all schedules from the repository
        List<Schedule> schedules = scheduleRepo.readFile();

        // Get the Coach ID from user input
        String coachID = Utils.getValue("Enter Coach ID: ");

        // Create a map to group users by their courses and schedules
        Map<String, Map<String, List<Schedule>>> courseUserSchedulesMap = new HashMap<>();

        // Populate the map with user schedules
        for (Schedule schedule : schedules) {
            String courseID = schedule.getCourseID();
            String userID = schedule.getUserID();

            // Retrieve course and user objects using their respective repositories
            Course course = courseSrv.findById(courseID);
            User user = userSrv.findById(userID);

            // Check if the user and course were found and the course is created by the coach
            if (user != null && course != null && course.getCoachID().getId().equals(coachID)) {
                // Add schedule to the corresponding user and course
                courseUserSchedulesMap
                        .computeIfAbsent(courseID, k -> new HashMap<>())
                        .computeIfAbsent(userID, k -> new ArrayList<>())
                        .add(schedule);
            }
        }

        // Check if any progress was collected
        if (courseUserSchedulesMap.isEmpty()) {
            System.out.println("No user progress found for Coach ID: " + coachID);
            return; // Exit early if no progress was found
        }

        // Calculate and print progress for each user in each course
        for (Map.Entry<String, Map<String, List<Schedule>>> courseEntry : courseUserSchedulesMap.entrySet()) {
            String courseID = courseEntry.getKey();
            Map<String, List<Schedule>> userSchedulesMap = courseEntry.getValue();

            Course course = courseSrv.findById(courseID);
            if (course != null) {
                System.out.println("Course: " + course.getCourseName() + " (ID: " + courseID + ")");
            } else {
                System.out.println("Course ID: " + courseID + " not found.");
            }

            for (Map.Entry<String, List<Schedule>> userEntry : userSchedulesMap.entrySet()) {
                String userID = userEntry.getKey();
                List<Schedule> userSchedules = userEntry.getValue();

                // Calculate progress for the user based on their specific schedules
                double progress = scheduleSrv.calculateProgress(userSchedules);
                User user = userSrv.findById(userID); // Retrieve user object for displaying user details

                // Print user progress
                if (user != null) {
                    String userProgress = String.format("User: %s (ID: %s) - Progress: %.2f%%, Total Sessions: %d",
                            user.getName(), user.getId(), progress, userSchedules.size());
                    System.out.println("  " + userProgress);
                } else {
                    System.out.println("  User ID: " + userID + " not found.");
                }
            }
            System.out.println(); // Print a newline for better readability
        }
    }

}
