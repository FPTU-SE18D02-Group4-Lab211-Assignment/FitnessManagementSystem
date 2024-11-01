package view;

import model.Schedule;
import service.ScheduleService;

import java.util.List;
import java.util.Scanner;

public class ScheduleView {

    private final ScheduleService scheduleService = new ScheduleService();
    private final Scanner scanner = new Scanner(System.in);

    public ScheduleView() {
    }

    public void viewUserSchedule() {
        System.out.print("Enter User ID to view schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to view schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        List<Schedule> userSchedules = scheduleService.getSchedulesForUser(userID, courseID); // Fetch schedules
        if (userSchedules.isEmpty()) {
            System.out.println("No schedules found for this user.");
        } else {
            System.out.println("User Schedules:");
            userSchedules.forEach(schedule -> System.out.println(schedule));
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

        boolean removed = scheduleService.deleteWorkoutSchedule(userID, courseID, workoutID, order);
        if (removed) {
            System.out.println("Workout schedule removed successfully.");
        } else {
            System.out.println("No matching workout schedule found.");
        }
    }

    public void generatePersonalizedSchedule() {
        System.out.print("Enter User ID to generate schedule: ");
        String userID = scanner.nextLine(); // Get User ID from input
        System.out.print("Enter Course ID to generate schedule: ");
        String courseID = scanner.nextLine(); // Get Course ID from input

        System.out.print("Enter number of sessions per week: ");
        int sessionsPerWeek = Integer.parseInt(scanner.nextLine()); // Convert input to integer

        List<Schedule> personalizedSchedule = scheduleService.generatePersonalizedSchedule(userID, courseID, sessionsPerWeek);
        if (personalizedSchedule.isEmpty()) {
            System.out.println("Failed to generate a personalized schedule.");
        } else {
            System.out.println("Personalized schedule generated successfully:");
            personalizedSchedule.forEach(schedule -> System.out.println(schedule));
        }
    }
}
