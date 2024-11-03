package view;

import model.Schedule;
import service.ScheduleService;

import java.util.List;
import java.util.Scanner;
import repository.ScheduleRepository;

public class ScheduleView {

    private final ScheduleService scheduleSrv = new ScheduleService();
    private final ScheduleRepository scheduleRepo = new ScheduleRepository();
    private final Scanner scanner = new Scanner(System.in);

    public ScheduleView() {
    }

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

    public void viewEditUserSchedule() {
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

        // Display the updated schedule
        // Display the first week's schedule
        scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

        // Display whole schedule
        scheduleSrv.displayWholeScheduleForCourse(schedule);
    }

    public void viewCompleteWorkouts() {
        System.out.print("Enter User ID to edit schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to edit schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        List<Schedule> schedule = scheduleRepo.readFileWithUserCourseID(userID, courseID);

        // Display the first week's schedule
        scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

        // Display whole schedule
        scheduleSrv.displayWholeScheduleForCourse(schedule);

    }

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
}
