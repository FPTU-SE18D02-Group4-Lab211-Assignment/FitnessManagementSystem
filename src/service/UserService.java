package service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import model.Course;
import model.Schedule;
import model.User;
import repository.ScheduleRepository;
import repository.UserRepository;
import utils.Validation;

public class UserService implements IUserService {

    private static final UserRepository userRepo = new UserRepository();
    ScheduleRepository scheduleRepo = new ScheduleRepository();
    private Map<String, User> users = new HashMap<>();
    private Map<String, Map<String, Integer>> userCourseStatus = new HashMap<>();
//----------------------------------------------------

    private String generateUserId() {
        int maxId = 0;
        for (User user : users.values()) {
            String[] parts = user.getId().split("-");
            if (parts.length == 2) {
                try {
                    int idNumber = Integer.parseInt(parts[1]);
                    if (idNumber > maxId) {
                        maxId = idNumber;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        // Generate new ID by incrementing the highest existing ID
        return String.format("USER-%04d", maxId + 1);
    }
//----------------------------------------------------

    public void signInNewCourse() {
        ScheduleService scheduleSrv = new ScheduleService();
        CourseService courseService = new CourseService();
        Scanner scanner = new Scanner(System.in);

        try {
            String userId = Validation.checkString("Your user ID: ", "Wrong format, must be USER-YYYY", "USER-\\d{4}");

            // Get schedules for the user
            List<Schedule> userSchedules = scheduleRepo.readFileWithUserID(userId);

            // Filter available courses
            List<Course> availableCourses = courseService.getAvailableCourses(userSchedules);

            if (availableCourses.isEmpty()) {
                System.out.println("No available courses for sign-in.");
                return; // Exit if no courses are available
            }

            // Display available courses
            System.out.println("Available Courses:");
            for (Course course : availableCourses) {
                System.out.println(course);
            }

            // Collect valid course IDs
            Set<String> availableCourseIDs = new HashSet<>();
            for (Course course : availableCourses) {
                availableCourseIDs.add(course.getCourseID());
            }

            String courseID;
            do {
                courseID = Validation.checkString("Course ID to sign in: ", "Wrong format, must be COU-YYYY", "COU-\\d{4}");
                // Check if the entered courseID is valid
                if (!availableCourseIDs.contains(courseID)) {
                    System.out.println("Invalid Course ID. Please select a valid Course ID from the available courses.");
                }
            } while (!availableCourseIDs.contains(courseID)); // Continue prompting until a valid ID is entered

            // Get the number of days per week for workouts
            int daysPerWeek = Validation.checkInt("How many days per week do you want to work out? ", "Must be a positive integer");

            int totalWeeks;
            while (true) {
                System.out.print("Enter the number of weeks you want to complete the course: ");
                totalWeeks = scanner.nextInt();

                // Check if average workouts per week is valid
                Course course = courseService.findById(courseID);
                List<String> workoutIDs = course.getListOfWorkout();
                double leastNumberOfWorkouts = (double) (daysPerWeek * (totalWeeks - 1) + 1);
                if (leastNumberOfWorkouts <= workoutIDs.size()) {
                    break;
                } else {
                    System.out.println("The least number of workouts: " + leastNumberOfWorkouts + " must be lower than or at least equal to total number of workouts: " + workoutIDs.size() + ". Please re-enter.");
                }
            }

            // Generate the personalized schedule
            List<Schedule> schedule = scheduleSrv.generatePersonalizedSchedule(userId, courseID, daysPerWeek, totalWeeks);

            // Print a list of schedule objects for checking
            System.out.println("Temporary Schedule to check:");
            for (Schedule sch : schedule) {
                System.out.println(sch);
            }

            // Display the first week's schedule
            scheduleSrv.displayWeeklyScheduleForCourse(schedule, 1);

            // Display whole schedule
            scheduleSrv.displayWholeScheduleForCourse(schedule);

            // Confirm the generated schedule
            System.out.print("Do you agree with this schedule? (y/n): ");
            String response = scanner.next();

            if ("y".equalsIgnoreCase(response)) {
                scheduleRepo.createFile(userId, courseID);
                for (Schedule sched : schedule) {
                    scheduleRepo.writeFile(sched);
                }
                System.out.println("Schedule saved successfully!");
            } else {
                System.out.println("Please enter the days per week and course duration again.");
                signInNewCourse(); // Recursive call for re-signing
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

//----------------------------------------------------
    @Override
    public User findById(String id) {
        String trimmedId = id.trim();
        for (User user : userRepo.getUserList()) { 
            if (user.getId().equals(trimmedId)) {
                return user;
            }
        }
        return null;
    }
//----------------------------------------------------

    @Override
    public void add(User user) {
        if (users.containsKey(user.getId())) {
            System.out.println("User with ID " + user.getId() + " already exists.");
        } else {
            users.put(user.getId(), user);
            userCourseStatus.put(user.getId(), new HashMap<>());
            System.out.println("User added successfully: " + user);
        }
    }
//----------------------------------------------------

    public void addnewU(User user) {
        try {
            String id = generateUserId();  // Automatically generate a new user ID
            System.out.println("New User ID: " + id);
            String name = Validation.checkName("Enter user name: ", "Each word in name must have its first letter capitalized");
            String dob = Validation.checkDob("Enter user date of birth: ", "This person need to be older than 18");
            boolean gender = Validation.convertStringToGender(Validation.getValue("Male or Female: "));
            String phone = Validation.checkString("Enter user phone number: ", "Invalid phone number format. Expected 10 digits.", "^0\\d{9}$");
            String email = Validation.checkString("Enter user email: ", "Invalid email. Must be in the format <username>@<domain>.<extension>", "\\w+@\\w+\\.\\w+");
            user = new User(id, name, dob, gender, phone, email);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
        userRepo.getUserList().add(user);
        save();
    }
//----------------------------------------------------

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
//----------------------------------------------------

    public void updateCourseStatus(String userId, String courseId, int status) {
        if (userCourseStatus.containsKey(userId) && userCourseStatus.get(userId).containsKey(courseId)) {
            userCourseStatus.get(userId).put(courseId, status);
            System.out.println("Course " + courseId + " for user " + userId + " updated to status " + status);
        } else {
            System.out.println("Course or User not found.");
        }
    }
//----------------------------------------------------

    @Override
    public void display() {
        for (User user : userRepo.getUserList()) {
            System.out.println(user);
        }
    }
//----------------------------------------------------

    @Override
    public void save() {
        try {
            userRepo.writeFile(userRepo.getUserList());
            System.out.println("User data saved.");
        } catch (Exception e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }
//----------------------------------------------------

    @Override
    public void update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            System.out.println("User updated successfully.");
        } else {
            System.out.println("User not found!");
        }
    }
//----------------------------------------------------

    public void editUser() {
        User user;
        while (true) {
            String id = Validation.getValue("Enter user ID to update: ");
            user = findById(id);

            if (user != null) {
                System.out.println("Editing user: " + user.getName());
                break;
            } else {
                System.out.println("User with ID " + id + " not found. Please enter a valid ID.");
            }
        }

        Field[] personFields = user.getClass().getSuperclass().getDeclaredFields();
        Field[] userFields = user.getClass().getDeclaredFields();
        Field[] allFields = new Field[personFields.length + userFields.length];
        System.arraycopy(personFields, 0, allFields, 0, personFields.length);
        System.arraycopy(userFields, 0, allFields, personFields.length, userFields.length);

        while (true) {
            System.out.println("Select a field to update:");
            for (int i = 0; i < allFields.length; i++) {
                System.out.println((i + 1) + ". " + allFields[i].getName());
            }
            System.out.println((allFields.length + 1) + ". Back");

            int choice = Integer.parseInt(Validation.getValue("Enter your choice: "));
            if (choice == allFields.length + 1) {
                break;
            }

            if (choice >= 1 && choice <= allFields.length) {
                Field selectedField = allFields[choice - 1];
                selectedField.setAccessible(true);
                String newValue = Validation.getValue("Enter new value for " + selectedField.getName() + ": ");

                try {
                    if (selectedField.getType() == LocalDate.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dateValue = LocalDate.parse(newValue, formatter);
                        selectedField.set(user, dateValue);
                    } else if (selectedField.getType() == boolean.class) {
                        boolean boolValue = newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("male");
                        selectedField.set(user, boolValue);
                    } else {
                        selectedField.set(user, newValue);
                    }
                    update(user);
                    System.out.println("Field " + selectedField.getName() + " has been updated.");
                } catch (IllegalAccessException | DateTimeParseException ex) {
                    System.out.println("Error updating field: " + ex.getMessage());
                }
            }
        }
    }
}
