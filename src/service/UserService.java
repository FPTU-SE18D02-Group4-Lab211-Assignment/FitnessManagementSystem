package service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Course;
import model.Schedule;
import model.User;
import repository.CourseRepository;
import repository.ScheduleRepository;
import repository.UserRepository;
import utils.Validation;

public class UserService implements IUserService {

    private static final UserRepository userRepo = new UserRepository();
    ScheduleRepository scheduleRepo = new ScheduleRepository();
    CourseRepository courseRepo = new CourseRepository();
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
                    // Ignore any IDs that don't fit the USER-XXXX format
                }
            }
        }
        // Generate new ID by incrementing the highest existing ID
        return String.format("USER-%04d", maxId + 1);
    }
//----------------------------------------------------

    public void signInNewCourse() {
        CourseService couSrv = new CourseService();
        ScheduleService scheduleSrv = new ScheduleService();

        try {
            String userId = Validation.checkString("Your user ID: ", "Wrong format, must be USER-YYYY", "USER-\\d{4}");
            // Get schedules for the user
            List<Schedule> userSchedules = scheduleRepo.readFileWithUserID(userId);
            // Filter available courses
            List<Course> availableCourses = getAvailableCourses(userSchedules);

            if (availableCourses.isEmpty()) {
                System.out.println("No available courses for sign-in.");
                return; // Exit if no courses are available
            }

            // Display available courses using a for loop
            System.out.println("Available Courses:");
            for (Course course : availableCourses) {
                System.out.println(course); // Assuming Course has a suitable toString method
            }

            String courseID = Validation.checkString("Course ID to sign in: ", "Wrong format, must be COU-YYYY", "COU-\\d{4}");
            int session = Validation.checkInt("How many workouts do you want to practice per week: ", "Must be a positive integer");
            scheduleSrv.generatePersonalizedSchedule(userId, courseID, session);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
//----------------------------------------------------
    // Helper method to filter available courses

    private List<Course> getAvailableCourses(List<Schedule> userSchedules) {
        Set<String> registeredCourseIDs = new HashSet<>();
        for (Schedule schedule : userSchedules) {
            registeredCourseIDs.add(schedule.getCourseID());
        }

        List<Course> availableCourses = new ArrayList<>();
        for (Course course : courseRepo.getCourseList()) {
            if (!registeredCourseIDs.contains(course.getCourseID())) {
                availableCourses.add(course);
            }
        }
        return availableCourses;
    }

//----------------------------------------------------
    @Override
    public User findById(String id) {
        return users.get(id);
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
//        while (true) {
//            String id = Validation.getValue("Enter ID: ");
//
//            if (users.containsKey(id)) {
//                System.out.println("User with ID " + id + " already exists. Please enter a different ID.");
//                continue;
//            }
//
//            String name = Validation.getValue("Enter name: ");
//            String email = Validation.getValue("Enter email: ");
//            String phone = Validation.getValue("Enter phone: ");
//            LocalDate birthDate = null;
//
//            while (birthDate == null) {
//                String dateStr = Validation.getValue("Enter birth date (dd/MM/yyyy): ");
//                birthDate = parseDate(dateStr);
//                if (birthDate == null) {
//                    System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
//                }
//            }
//
//            boolean gender;
//            while (true) {
//                String genderInput = Validation.getValue("Enter gender (Male/Female): ");
//                if (genderInput.equalsIgnoreCase("Male")) {
//                    gender = true;
//                    break;
//                } else if (genderInput.equalsIgnoreCase("Female")) {
//                    gender = false;
//                    break;
//                }
//                System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
//            }
//
//            User user = new User(id, name, birthDate.toString(), gender, phone, email);
//
//            add(user);
//            userCourseStatus.put(id, new HashMap<>());
//
//            System.out.println("User added successfully: " + user);
//            break;
//        }
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
//        users.forEach((id, user) -> {
//            System.out.println("User: " + user);
//            System.out.println("Course Status: " + userCourseStatus.get(id));
//        });
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
