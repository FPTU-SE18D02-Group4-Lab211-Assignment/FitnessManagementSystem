package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    // Generate a personalized schedule based on sessions per week
    public List<Schedule> generatePersonalizedSchedule(String userID, String courseID, int sessionsPerWeek) {
        Course course = courseService.findById(courseID);
        List<String> workoutIDs = course.getListOfWorkout();

        List<Schedule> personalizedSchedule = new ArrayList<>();
        if (workoutIDs.isEmpty()) {
            System.out.println("No workouts available for this course!");
            return personalizedSchedule;
        }

        LocalDate startDate = LocalDate.now();
        int workoutIndex = 0;
        int daysBetweenSessions = 7 / sessionsPerWeek;

        for (int week = 0; workoutIndex < workoutIDs.size(); week++) {
            for (int session = 0; session < sessionsPerWeek && workoutIndex < workoutIDs.size(); session++) {
                LocalDate sessionDate = startDate.plusDays(week * 7 + session * daysBetweenSessions);
                String formattedDate = sessionDate.format(formatter);
                Schedule schedule = new Schedule(userID, workoutIDs.get(workoutIndex), courseID, workoutIndex + 1, formattedDate, false);
                personalizedSchedule.add(schedule);
                workoutIndex++;
            }
        }
        
        for (Schedule schedule : personalizedSchedule) {
            System.out.println(schedule);
        }
        
        scheduleRepository.createFile(userID, courseID);
        
        for (Schedule schedule : personalizedSchedule) {
            scheduleRepository.writeFile(schedule);
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
        // Retrieve all schedules for the user from the repository
        List<Schedule> allSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);

        // Return the filtered list (if needed, but this could be the complete list already)
        return allSchedules; // Adjust as necessary depending on your filtering needs
    }

//
//    public void adjustProgramDuration(String userID, String courseID, int newWeeks) {
//    List<Schedule> userSchedules = scheduleRepository.readFileWithUserCourseID(userID, courseID);
//
//    if (userSchedules.isEmpty()) {
//        System.out.println("No workouts found for the specified user and course.");
//        return;
//    }
//
//        int totalWorkouts = userSchedules.size();
//        LocalDate startDate = LocalDate.parse(userSchedules.get(0).getDate());
//        LocalDate endDate = startDate.plusWeeks(newWeeks);
//
//        // Calculate new session frequency to distribute workouts over new duration
//        int newSessionsPerWeek = (int) Math.ceil((double) totalWorkouts / newWeeks);
//        int daysBetweenSessions = 7 / newSessionsPerWeek;
//
//        System.out.printf("Adjusting schedule: %d total workouts over %d weeks (%d sessions per week)%n",
//                totalWorkouts, newWeeks, newSessionsPerWeek);
//
//        // Adjust the schedule with new dates based on calculated sessions per week
//        int workoutIndex = 0;
//        LocalDate sessionDate = startDate;
//
//        for (int week = 0; workoutIndex < totalWorkouts; week++) {
//            for (int session = 0; session < newSessionsPerWeek && workoutIndex < totalWorkouts; session++) {
//                // Set the date for each workout session
//                Schedule schedule = userSchedules.get(workoutIndex);
//                schedule.setDate(sessionDate.toString());
//
//                // Advance to the next session date within the week
//                sessionDate = sessionDate.plusDays(daysBetweenSessions);
//                workoutIndex++;
//            }
//
//            // Move to the start of the next week after filling all sessions for the current week
//            sessionDate = startDate.plusDays((week + 1) * 7);
//        }
//
//        // Save the updated schedule back to the repository
//        for (Schedule schedule : userSchedules) {
//            scheduleRepository.writeFileWithUserCourseID(schedule);
//        }
//
//        System.out.printf("Program duration adjusted to %d weeks with approximately %d sessions per week.%n", newWeeks, newSessionsPerWeek);
//    }
}
