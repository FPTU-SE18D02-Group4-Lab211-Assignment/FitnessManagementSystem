package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Course;
import model.Schedule;
import repository.ScheduleRepository;

public class ScheduleService {

    private final ScheduleRepository scheduleRepository = new ScheduleRepository();
    private final CourseService courseService = new CourseService();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ScheduleService() {
    }

//----------------------------------------------------
    public List<Schedule> generatePersonalizedSchedule(String userID, String courseID, int daysPerWeek, int totalWeeks) {
        Course course = courseService.findById(courseID);
        List<String> workoutIDs = course.getListOfWorkout();

        List<Schedule> personalizedSchedule = new ArrayList<>();
        if (workoutIDs.isEmpty()) {
            System.out.println("No workouts available for this course!");
            return personalizedSchedule;
        }

        // Set the start date to the beginning of the next week
        LocalDate startDate = LocalDate.now().plusWeeks(1).with(DayOfWeek.MONDAY);
        int totalWorkouts = workoutIDs.size();
        int averageWorkoutsPerWeek = (int) Math.ceil((double) totalWorkouts / totalWeeks);
        int workoutsPerDay = (int) Math.ceil((double) averageWorkoutsPerWeek / daysPerWeek);

        int workoutNumber = 0;  // Tracks the index of workouts in workoutIDs

        // Calculate the number of days between scheduled days
        int daysBetweenDays = 7 / daysPerWeek;

        // Map to track how many workouts are scheduled for each day
        Map<LocalDate, Integer> workoutCountByDate = new HashMap<>();
        LocalDate lastWorkoutDay = startDate;  // Keep track of the last workout day used

        for (int week = 0; week < totalWeeks; week++) {
            for (int session = 0; session < daysPerWeek; session++) {
                // Calculate the session date based on the week and the interval
                LocalDate sessionDate = startDate.plusDays(week * 7 + session * daysBetweenDays);
                lastWorkoutDay = sessionDate;  // Update the last workout day
                int workoutIndex = workoutCountByDate.getOrDefault(sessionDate, 0);

                // Schedule workouts for the current day
                for (int i = 0; i < workoutsPerDay && workoutNumber < totalWorkouts; i++) {
                    Schedule schedule = new Schedule(
                            userID,
                            workoutIDs.get(workoutNumber++),
                            courseID,
                            workoutIndex + 1,
                            sessionDate.format(formatter),
                            false
                    );
                    personalizedSchedule.add(schedule);
                    workoutIndex++;
                }

                // Update the workout count for the date
                workoutCountByDate.put(sessionDate, workoutIndex);

                // Stop if all workouts have been scheduled
                if (workoutNumber >= totalWorkouts) {
                    break;
                }
            }

            // Stop if all workouts have been scheduled
            if (workoutNumber >= totalWorkouts) {
                break;
            }
        }

        // If there are any remaining workouts, add them to the last workout day
        while (workoutNumber < totalWorkouts) {
            Schedule schedule = new Schedule(
                    userID,
                    workoutIDs.get(workoutNumber++),
                    courseID,
                    workoutCountByDate.getOrDefault(lastWorkoutDay, 0) + 1,
                    lastWorkoutDay.format(formatter),
                    false
            );
            personalizedSchedule.add(schedule);
            workoutCountByDate.put(lastWorkoutDay, workoutCountByDate.getOrDefault(lastWorkoutDay, 0) + 1);
        }

        return personalizedSchedule;
    }

//----------------------------------------------------
    public void updateSessionsPerWeek(int newSessionsPerWeek, String userID, String courseID) {
        // Get the user's schedules for the specific course
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);

        // Filter out the incomplete schedules
        List<Schedule> incompleteSchedules = new ArrayList<>();
        for (Schedule schedule : userSchedules) {
            if (!schedule.isStatus()) {
                incompleteSchedules.add(schedule);
            }
        }

        // Check if there are any incomplete schedules
        if (incompleteSchedules.isEmpty()) {
            System.out.println("No incomplete workout sessions for this course.");
            return;
        }

        // Get course information
        Course course = courseService.findById(courseID);
        List<String> workoutIDs = course.getListOfWorkout();

        // Validate newSessionsPerWeek to not exceed available workouts
        if (newSessionsPerWeek > workoutIDs.size()) {
            System.out.println("New sessions per week exceeds available workouts. Adjusting to available workouts.");
            newSessionsPerWeek = workoutIDs.size();
        }

        // Start from today
        LocalDate startDate = LocalDate.now();
        int workoutIndex = 0;
        int daysBetweenSessions = 7 / newSessionsPerWeek;
//
//        // Remove old schedules for the user and course with status incomplete
//        scheduleRepository.removeSchedulesByUserAndCourse(userID, courseID);

        // Create new schedules for the user
        List<Schedule> updatedSchedules = new ArrayList<>();
        for (int week = 0; workoutIndex < workoutIDs.size(); week++) {
            for (int session = 0; session < newSessionsPerWeek && workoutIndex < workoutIDs.size(); session++) {
                // Calculate the date for the workout session
                LocalDate sessionDate = startDate.plusDays(week * 7 + session * daysBetweenSessions);

                // Create a new Schedule for the workout session
                Schedule updatedSchedule = new Schedule(
                        userID,
                        workoutIDs.get(workoutIndex), // Use workoutID from the list
                        courseID,
                        workoutIndex + 1, // Session number in the course
                        sessionDate.toString(), // Date as String
                        false // Initial status is not completed
                );

                updatedSchedules.add(updatedSchedule);
                workoutIndex++;
            }
        }

        // Write the new schedules into the repository
        for (Schedule updatedSchedule : updatedSchedules) {
            scheduleRepository.writeFile(updatedSchedule);
        }
    }

//----------------------------------------------------
    public void markWorkoutAsCompleted(String userID, String courseID, String workoutID, int order) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        boolean found = false;

        for (Schedule schedule : userSchedules) {
            if (schedule.getWorkoutID().equals(workoutID) && schedule.getOrder() == order && !schedule.isStatus()) {
                schedule.setStatus(true);
                scheduleRepository.writeFile(schedule); // Save updated schedule
                System.out.println("Workout marked as completed.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Workout not found with the specified order or is already completed.");
        }
    }

//----------------------------------------------------
    public double calculateProgress(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        long totalSessions = userSchedules.size();
        long completedSessions = userSchedules.stream().filter(Schedule::isStatus).count();

        if (totalSessions == 0) {
            return 0;
        }
        return (double) completedSessions / totalSessions * 100;
    }

//----------------------------------------------------
    public void viewUpcomingWorkouts(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        LocalDate today = LocalDate.now();

        userSchedules.stream()
                .filter(schedule -> !schedule.isStatus() && schedule.getDate().isAfter(today))
                .sorted((s1, s2) -> s1.getDate().compareTo(s2.getDate()))
                .forEach(schedule -> System.out.println(schedule));
    }

//----------------------------------------------------
    public List<Schedule> getIncompleteWorkouts(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        List<Schedule> incompleteWorkouts = new ArrayList<>();

        for (Schedule schedule : userSchedules) {
            if (!schedule.isStatus()) {
                incompleteWorkouts.add(schedule);
            }
        }

        return incompleteWorkouts;
    }
//----------------------------------------------------

    public void displayWeeklyScheduleForCourse(List<Schedule> schedule, int weekNumber) {
        // Find the earliest workout date
        LocalDate firstWorkoutDate = schedule.stream()
                .map(Schedule::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now()); // Default to today if no workouts exist

        // Calculate the start date based on weekNumber relative to the first workout date
        LocalDate weekStartDate = firstWorkoutDate.with(DayOfWeek.MONDAY).plusWeeks(weekNumber - 1);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        System.out.println("Schedule for Week " + weekNumber + " (" + weekStartDate.format(formatter) + " to " + weekEndDate.format(formatter) + ")");
        System.out.println("-------------------------------------------------------");
        System.out.println("|  Day        |  Date        |  Workouts               |");
        System.out.println("-------------------------------------------------------");

        // Create a map to store the workouts scheduled for each day of the week
        Map<LocalDate, List<String>> dayWorkoutMap = new LinkedHashMap<>();
        for (Schedule sched : schedule) {
            LocalDate workoutDate = sched.getDate();

            // Only include workouts within the week range
            if (!workoutDate.isBefore(weekStartDate) && !workoutDate.isAfter(weekEndDate)) {
                dayWorkoutMap.computeIfAbsent(workoutDate, k -> new ArrayList<>()).add(sched.getWorkoutID());
            }
        }

        // Print out each day of the week with scheduled workouts or "Rest day" if none
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate date = weekStartDate.with(day);
            List<String> workouts = dayWorkoutMap.getOrDefault(date, Collections.emptyList());

            String workoutsDisplay = workouts.isEmpty() ? "Rest day" : String.join(", ", workouts);
            System.out.printf("|  %-10s |  %-11s |  %-20s |\n", day, date.format(formatter), workoutsDisplay);
        }
        System.out.println("-------------------------------------------------------");
    }

    public void displayWholeScheduleForCourse(List<Schedule> schedule) {
        // Loop to check for subsequent weeks
        Scanner scanner = new Scanner(System.in);
        int weekNumber = 2; // Start checking from the second week
        while (true) {
            // Use the helper method to check for next week's schedule
            if (doesNextWeekScheduleExist(schedule, weekNumber)) {
                System.out.print("Do you want to display the schedule for week " + weekNumber + "? (y/n): ");
                String response = scanner.next();
                if ("y".equalsIgnoreCase(response)) {
                    displayWeeklyScheduleForCourse(schedule, weekNumber);
                } else {
                    break; // Exit loop if the user does not want to see the next week
                }
            } else {
                System.out.println("No workouts scheduled for week " + weekNumber + ".");
                break; // Exit loop if no workouts are scheduled
            }
            weekNumber++; // Increment week number for the next iteration
        }
    }

    //----------------------------------------------------
    public void displayWeeklyScheduleForUser(List<Schedule> schedule, int weekNumber) {
        // Find the earliest workout date
        LocalDate firstWorkoutDate = schedule.stream()
                .map(Schedule::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now()); // Default to today if no workouts exist

        // Calculate the start date based on weekNumber relative to the first workout date
        LocalDate weekStartDate = firstWorkoutDate.with(DayOfWeek.MONDAY).plusWeeks(weekNumber - 1);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        System.out.println("Schedule for Week " + weekNumber + " (" + weekStartDate.format(formatter) + " to " + weekEndDate.format(formatter) + ")");
        System.out.println("-------------------------------------------------------");
        System.out.println("|  Day        |  Date        |  Workouts               |");
        System.out.println("-------------------------------------------------------");

        // Create a map to store the workouts scheduled for each day of the week
        Map<LocalDate, List<Schedule>> dayWorkoutMap = new LinkedHashMap<>();
        for (Schedule sched : schedule) {
            LocalDate workoutDate = sched.getDate();

            // Only include workouts within the week range
            if (!workoutDate.isBefore(weekStartDate) && !workoutDate.isAfter(weekEndDate)) {
                dayWorkoutMap.computeIfAbsent(workoutDate, k -> new ArrayList<>()).add(sched);
            }
        }

        // Print out each day of the week with scheduled workouts or "Rest day" if none
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate date = weekStartDate.with(day);
            List<Schedule> workouts = dayWorkoutMap.getOrDefault(date, Collections.emptyList());

            StringBuilder workoutsDisplay = new StringBuilder();
            if (workouts.isEmpty()) {
                workoutsDisplay.append("Rest day");
            } else {
                // Group workouts by Course ID
                Map<String, List<String>> courseWorkoutMap = new LinkedHashMap<>();
                for (Schedule sched : workouts) {
                    String courseId = sched.getCourseID();
                    String workoutId = sched.getWorkoutID();

                    // Add the workout ID to the corresponding course
                    courseWorkoutMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(workoutId);
                }

                // Build the display string
                for (Map.Entry<String, List<String>> entry : courseWorkoutMap.entrySet()) {
                    String courseId = entry.getKey();
                    List<String> workoutIds = entry.getValue();
                    workoutsDisplay.append(courseId).append(": ").append(String.join(", ", workoutIds)).append(" | ");
                }

                // Remove the trailing separator if present
                if (workoutsDisplay.length() > 0) {
                    workoutsDisplay.setLength(workoutsDisplay.length() - 3); // Remove the last " | "
                }
            }

            System.out.printf("|  %-10s |  %-11s |  %-20s |\n", day, date.format(formatter), workoutsDisplay.toString());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void displayWholeScheduleForUser(List<Schedule> schedule) {
        // Loop to check for subsequent weeks
        Scanner scanner = new Scanner(System.in);
        int weekNumber = 2; // Start checking from the second week
        while (true) {
            // Use the helper method to check for next week's schedule
            if (doesNextWeekScheduleExist(schedule, weekNumber)) {
                System.out.print("Do you want to display the schedule for week " + weekNumber + "? (y/n): ");
                String response = scanner.next();
                if ("y".equalsIgnoreCase(response)) {
                    displayWeeklyScheduleForUser(schedule, weekNumber);
                } else {
                    break; // Exit loop if the user does not want to see the next week
                }
            } else {
                System.out.println("No workouts scheduled for week " + weekNumber + ".");
                break; // Exit loop if no workouts are scheduled
            }
            weekNumber++; // Increment week number for the next iteration
        }
    }

    //----------------------------------------------------
    // Helper function
    public boolean doesNextWeekScheduleExist(List<Schedule> schedule, int weekNumber) {
        LocalDate nextWeekStartDate = LocalDate.now().plusWeeks(weekNumber).with(DayOfWeek.MONDAY);
        LocalDate nextWeekEndDate = nextWeekStartDate.plusDays(6);

        return schedule.stream()
                .anyMatch(sched -> {
                    LocalDate workoutDate = sched.getDate();
                    return !workoutDate.isBefore(nextWeekStartDate) && !workoutDate.isAfter(nextWeekEndDate);
                });
    }

//----------------------------------------------------
    public String getMonthlySummary(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();

        long completedCount = userSchedules.stream()
                .filter(schedule -> schedule.getDate().getMonthValue() == currentMonth && schedule.isStatus())
                .count();
        long missedCount = userSchedules.stream()
                .filter(schedule -> schedule.getDate().getMonthValue() == currentMonth && !schedule.isStatus() && schedule.getDate().isBefore(today))
                .count();

        return String.format("Monthly Summary (Completed: %d, Missed: %d)", completedCount, missedCount);
    }
//----------------------------------------------------
    // Option 1: Move a workout from a date to another date

    public void moveWorkoutFromDate(List<Schedule> schedule) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date of the workout to move (dd/MM/yyyy): ");
        String oldDateStr = scanner.nextLine();

        LocalDate oldDate = LocalDate.parse(oldDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Retrieve workouts for the specified old date
        List<Schedule> workoutsOnDate = getSchedulesInWeek(schedule, oldDate);

        // Check if there are any workouts for the specified date
        if (workoutsOnDate.isEmpty()) {
            System.out.println("No workouts found on this date.");
            return;
        }

        // Request the user to enter the workout ID to move
        System.out.print("Enter the workout ID to move: ");
        String workoutIdToMove = scanner.nextLine();

        // Find the workout with the specified ID
        Schedule workoutToMove = null;
        for (Schedule workout : workoutsOnDate) {
            if (workout.getWorkoutID().equals(workoutIdToMove)) {
                workoutToMove = workout;
                break;
            }
        }

        if (workoutToMove == null || workoutToMove.isStatus()) {
            System.out.println("No workout found with the specified ID or it is already completed. Please try again.");
            return;
        }

        System.out.print("Enter the new date for the workout (dd/MM/yyyy): ");
        String newDateStr = scanner.nextLine();
        LocalDate newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Move the workout to the new date
        workoutToMove.setDate(newDateStr);
        scheduleRepository.replaceFile(workoutToMove); // Persist changes
        System.out.println("Workout ID " + workoutIdToMove + " moved to " + newDateStr);
    }

    // Option 2: Move all workouts in a week
    public void moveAllWorkoutsInWeek(List<Schedule> schedule) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the start date of the week (dd/MM/yyyy): ");
        String startDateStr = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<Schedule> weekSchedules = getSchedulesInWeek(schedule, startDate);
        if (!weekSchedules.isEmpty()) {
            System.out.print("Enter the number of days to move (positive for forward, negative for backward): ");
            int days = scanner.nextInt();

            for (Schedule sch : weekSchedules) {
                if (!sch.isStatus()) {
                    sch.setDate(sch.getDate().plusDays(days).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    scheduleRepository.replaceFile(sch, days); // Persist each change
                }
            }
            System.out.println("All workouts moved by " + days + " days.");
        } else {
            System.out.println("No workouts found for this week.");
        }
    }

    // Option 3: Move workouts from a set of weeks
    public void moveWorkoutsFromWeeks(List<Schedule> schedule) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the start date of the first week (dd/MM/yyyy): ");
        String startDateStr = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Enter the number of weeks to consider: ");
        int numberOfWeeks = scanner.nextInt();

        System.out.print("Enter the number of days to move (positive for forward, negative for backward): ");
        int days = scanner.nextInt();

        for (int i = 0; i < numberOfWeeks; i++) {
            LocalDate weekStartDate = startDate.plusWeeks(i);
            List<Schedule> weekSchedules = getSchedulesInWeek(schedule, weekStartDate);

            for (Schedule sch : weekSchedules) {
                if (!sch.isStatus()) {
                    scheduleRepository.replaceFile(sch, days);
                }
            }
        }
        System.out.println("All workouts moved by " + days + " days for the specified weeks.");
    }

    // Option 4: Move all remaining workouts after today
    public void moveRemainingWorkouts(List<Schedule> schedule) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of days to move (positive for forward, negative for backward): ");
        int days = scanner.nextInt();

        List<Schedule> remainingSchedules = getRemainingSchedules(schedule, LocalDate.now());
        for (Schedule sch : remainingSchedules) {
            if (!sch.isStatus()) {
                // Directly pass each Schedule with the days to move
                scheduleRepository.replaceFile(sch, days);
            }
        }
        System.out.println("All remaining workouts moved by " + days + " days.");
    }

//----------------------------------------------------
    // Get a Schedule by date
    private Schedule getScheduleByDate(List<Schedule> schedules, LocalDate date) {
        for (Schedule schedule : schedules) {
            if (schedule.getDate().equals(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))) {
                return schedule; // Return the first matching schedule
            }
        }
        return null; // Return null if no matching schedule is found
    }

    // Get all Schedules in a specific week
    private List<Schedule> getSchedulesInWeek(List<Schedule> schedules, LocalDate startDate) {
        List<Schedule> weekSchedules = new ArrayList<>();
        LocalDate endDate = startDate.plusDays(6); // Define the end of the week

        for (Schedule schedule : schedules) {
            LocalDate scheduleDate = schedule.getDate();
            if ((scheduleDate.isEqual(startDate) || scheduleDate.isAfter(startDate)) && scheduleDate.isBefore(endDate)) {
                weekSchedules.add(schedule); // Add schedules that fall within the week
            }
        }
        return weekSchedules; // Return the list of schedules for the week
    }

    // Get all remaining Schedules after a specified date
    private List<Schedule> getRemainingSchedules(List<Schedule> schedules, LocalDate currentDate) {
        List<Schedule> remainingSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            LocalDate scheduleDate = schedule.getDate();
            if (scheduleDate.isAfter(currentDate) || scheduleDate.isEqual(currentDate)) {
                remainingSchedules.add(schedule); // Add remaining schedules after today
            }
        }
        return remainingSchedules; // Return the list of remaining schedules
    }

}
