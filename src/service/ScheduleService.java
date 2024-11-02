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
        int totalWorkouts = workoutIDs.size(); // Total workouts available
        int totalDays = daysPerWeek * totalWeeks;
        int averageWorkoutsPerWeek = totalWorkouts / totalWeeks;
        int workoutsPerDay = (int) Math.ceil((double) averageWorkoutsPerWeek / daysPerWeek); // Determine workouts per day

        // Map to track how many workouts are scheduled for each day
        Map<LocalDate, Integer> workoutCountByDate = new HashMap<>();

        for (int week = 0; week < totalWeeks; week++) {
            for (int session = 0; session < daysPerWeek; session++) {
                LocalDate sessionDate = startDate.plusDays(week * 7 + session);

                // Calculate the index of the next workout to schedule
                int workoutIndex = workoutCountByDate.getOrDefault(sessionDate, 0);

                // Schedule the required number of workouts for this session
                for (int i = 0; i < workoutsPerDay; i++) {
                    if (workoutIndex < totalWorkouts) {
                        Schedule schedule = new Schedule(userID, workoutIDs.get(workoutIndex), courseID, workoutIndex + 1, sessionDate.format(formatter), false);
                        personalizedSchedule.add(schedule);
                        workoutIndex++; // Increment index for the next workout
                    } else {
                        break; // Exit if we've exhausted the workouts
                    }
                }

                // Update the workout count for the date
                workoutCountByDate.put(sessionDate, workoutIndex);
            }
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

    public boolean deleteWorkoutSchedule(String userID, String courseID, String workoutID, int order) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        boolean removed = userSchedules.removeIf(schedule
                -> schedule.getWorkoutID().equals(workoutID)
                && schedule.getOrder() == order);

        if (removed) {
            // Rewrite the updated list to the file
            for (Schedule schedule : userSchedules) {
                scheduleRepository.writeFile(schedule);
            }
        }

        return removed;
    }

//----------------------------------------------------
    public Schedule getNextWorkout(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        LocalDate today = LocalDate.now();

        return userSchedules.stream()
                .filter(schedule -> !schedule.isStatus() && schedule.getDate().isAfter(today))
                .min((s1, s2) -> s1.getDate().compareTo(s2.getDate()))
                .orElse(null);
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

    public List<Schedule> viewWeeklySchedule(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);

        List<Schedule> weeklySchedule = new ArrayList<>();
        for (Schedule schedule : userSchedules) {
            if (!schedule.isStatus() && !schedule.getDate().isBefore(today) && !schedule.getDate().isAfter(weekFromNow)) {
                weeklySchedule.add(schedule);
            }
        }

        return weeklySchedule;
    }

    public void displayWeeklySchedule(List<Schedule> schedule, int weekNumber) {
        // Find the earliest workout date
        LocalDate firstWorkoutDate = schedule.stream()
                .map(Schedule::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now()); // Default to today if no workouts exist

        // Calculate the start date for the specified week number
        LocalDate weekStartDate = LocalDate.now().plusWeeks(weekNumber - 1).with(DayOfWeek.MONDAY);
        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // Adjust weekStartDate if it's before the first workout week
        LocalDate firstWorkoutWeekStartDate = firstWorkoutDate.with(DayOfWeek.MONDAY);
        if (weekStartDate.isBefore(firstWorkoutWeekStartDate)) {
            weekStartDate = firstWorkoutWeekStartDate;
            weekEndDate = weekStartDate.plusDays(6);
        }

        System.out.println("Schedule for Week " + weekNumber + " (" + weekStartDate + " to " + weekEndDate + ")");
        System.out.println("-------------------------------------------------------");
        System.out.println("|  Day        |  Date        |  Workouts               |");
        System.out.println("-------------------------------------------------------");

        // Create a map to store the workouts scheduled for each day of the week
        Map<LocalDate, List<String>> dayWorkoutMap = new LinkedHashMap<>();
        for (Schedule sched : schedule) {
            LocalDate workoutDate = sched.getDate();

            // Only include workouts within the week range
            if (!workoutDate.isBefore(weekStartDate) && !workoutDate.isAfter(weekEndDate)) {
                dayWorkoutMap.computeIfAbsent(workoutDate, k -> new ArrayList<>()).add(sched.getWorkoutID()); // Collect workouts
            }
        }

        // Print out each day of the week with scheduled workouts or "Rest day" if none
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate date = weekStartDate.with(day);
            List<String> workouts = dayWorkoutMap.getOrDefault(date, Collections.emptyList());

            String workoutsDisplay = workouts.isEmpty() ? "Rest day" : String.join(", ", workouts);
            System.out.printf("|  %-10s |  %-11s |  %-20s |\n", day, date, workoutsDisplay);
        }
        System.out.println("-------------------------------------------------------");
    }


    // Helper function
    public boolean doesNextWeekScheduleExist(List<Schedule> schedule, int weekNumber) {
        LocalDate nextWeekStartDate = LocalDate.now().plusWeeks(weekNumber - 1).with(DayOfWeek.MONDAY);
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
    public void listAllSessions(String userID, String courseID) {
        List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);

        if (userSchedules.isEmpty()) {
            System.out.println("No sessions found for this course.");
            return;
        }

        for (Schedule schedule : userSchedules) {
            System.out.println(schedule);
        }
    }

    public List<Schedule> getSchedulesForUser(String userID, String courseID) {
        List<Schedule> allSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);

        return allSchedules;
    }
}
