package view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Schedule;
import service.ScheduleService;
import model.Course;
import model.User;
import repository.ScheduleRepository;
import service.CourseService;
import service.UserService;
import utils.Utils;

public class ScheduleView {

    private final ScheduleService scheduleSrv = new ScheduleService();
    private final ScheduleRepository scheduleRepo = new ScheduleRepository();

    private final CourseService courseSrv = new CourseService();
    private final UserService userSrv = new UserService();

    public ScheduleView() {
    }

//----------------------------------------------------
    public void viewUserSchedule() {

        String userID = Utils.getValue("Enter User ID to view schedule: ");

        List<Schedule> schedule;

        String choice = Utils.getValue("Do you want to view a specific course schedule or the whole schedule? (Enter 'course' or 'whole'): ").trim().toLowerCase();

        if (choice == null || choice.isEmpty()) {
            System.out.println("Invalid choice. Please enter 'course' or 'whole'.");
        } else {
            switch (choice) {
                case "course": {
                    String courseID = Utils.getValue("Enter Course ID to view schedule: ");

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
        String userID = Utils.getValue("Enter User ID to edit schedule: ");
        String courseID = Utils.getValue("Enter Course ID to edit schedule: ");

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
        int choice = Integer.parseInt(Utils.getValue("Enter your choice (1-4): "));

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
        String userID = Utils.getValue("Enter User ID to edit schedule: ");
        String courseID = Utils.getValue("Enter Course ID to edit schedule: ");

        List<Schedule> schedules = scheduleRepo.readFileWithUserCourseID(userID, courseID);
        scheduleSrv.viewIncompleteWorkoutsBeforePresent(schedules);

        boolean continueCompleting = true; // Flag to control the loop

        while (continueCompleting) {
            String workoutID = Utils.getValue("Enter Workout ID to mark as completed: ");
            int order = Integer.parseInt(Utils.getValue("Enter the order of the workout: "));

            String dateStr = Utils.getValue("Enter the date of the workout (dd/MM/yyyy): ");
            LocalDate workoutDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Mark the workout as completed with the new date parameter
            scheduleSrv.markWorkoutAsCompleted(schedules, workoutID, order, workoutDate);

            // Ask the user if they want to continue
            String response = Utils.getValue("Would you like to complete another workout? (y/n): ").trim().toLowerCase();
            continueCompleting = response.equals("y");
        }

        System.out.println("Workout completion process ended.");
    }

//----------------------------------------------------
    public void viewUpcomingWorkouts() {
        String userID = Utils.getValue("Enter User ID: ");

        List<Schedule> schedule = scheduleRepo.readFileWithUserID(userID);

        if (schedule.isEmpty()) {
            System.out.println("No workouts found for this user.");
            return;
        }

        scheduleSrv.viewUpcomingWorkouts(schedule);
    }

//----------------------------------------------------
    public void viewUsersProgress() {
        // Retrieve all schedules from the repository
        List<Schedule> schedules = scheduleRepo.readFile();

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
            System.out.println();
        }
    }
}
