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

    public void editUserSchedule() {
        System.out.print("Enter User ID to edit schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to edit schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        System.out.print("Enter Workout ID to remove from schedule: ");
        String workoutID = scanner.nextLine();
        System.out.print("Enter order of the workout to remove: ");
        int order = Integer.parseInt(scanner.nextLine()); // Convert input to integer

        boolean removed = scheduleSrv.deleteWorkoutSchedule(userID, courseID, workoutID, order);
        if (removed) {
            System.out.println("Workout schedule removed successfully.");
        } else {
            System.out.println("No matching workout schedule found.");
        }
    }
}
