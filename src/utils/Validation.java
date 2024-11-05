package utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import model.Coach;
import model.Course;
import model.Exercise;
import model.User;
import model.Workout;
import service.CoachService;
import service.CourseService;
import service.ExerciseService;
import service.UserService;
import service.WorkoutService;

public class Validation {

//----------------------------------------------------    
    public static String getValue(String input) {
        Scanner sc = new Scanner(System.in);
        System.out.print(input);
        return sc.nextLine();
    }

//----------------------Validate-ID-----------------------------
    public static boolean validateUserID(String id) {
        if (id.matches("USER-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid User ID format. Expected USER-YYYY (Y is a digit).");
            return false;
        }
    }

    public static boolean validateCoachID(String id) {
        if (id.matches("COA-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid Coach ID format. Expected COA-YYYY (Y is a digit).");
            return false;
        }
    }

    public static boolean validateCourseID(String id) {
        if (id.matches("COU-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid Course ID format. Expected COU-YYYY (Y is a digit).");
            return false;
        }
    }

    public static boolean validateWorkoutID(String id) {
        if (id.matches("WOR-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid Workout ID format. Expected WOR-YYYY (Y is a digit).");
            return false;
        }
    }

    public static boolean validateExerciseID(String id) {
        if (id.matches("EXE-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid Exercise ID format. Expected EXE-YYYY (Y is a digit).");
            return false;
        }
    }
//-----------------------Validate-Find-ID---------------------------

    public static User validateAndFindUser(UserService userSrv) {
        User user = null;
        String userID;

        do {
            userID = Utils.getValue("Enter the User ID to delete: ");

            if (!validateUserID(userID)) {
                System.out.println("Invalid User ID format. Please try again.");
                continue;
            }

            user = userSrv.findById(userID);
            if (user == null) {
                System.out.println("User with ID " + userID + " not found.");
            }
        } while (user == null);

        return user;
    }

    public static Coach validateAndFindCoach(CoachService coachSrv) throws Exception {
        Coach coach = null;
        String coachID;

        do {
            coachID = Utils.getValue("Enter the Coach ID: ");

            if (!validateCoachID(coachID)) {
                System.out.println("Invalid Coach ID format. Please try again.");
                continue;
            }

            coach = coachSrv.findById(coachID);
            if (coach == null) {
                System.out.println("Coach with ID " + coachID + " not found.");
            }
        } while (coach == null);

        return coach;
    }

    public static Course validateAndFindCourse(CourseService courseSrv) {
        Course course = null;
        String courseID;

        do {
            courseID = Utils.getValue("Enter the Course ID: ");

            if (!validateCourseID(courseID)) {
                System.out.println("Invalid Course ID format. Please try again.");
                continue;
            }

            course = courseSrv.findById(courseID);
            if (course == null) {
                System.out.println("Course with ID " + courseID + " not found.");
            }
        } while (course == null);

        return course;
    }

    public static Workout validateAndFindWorkout(WorkoutService workoutSrv) {
        Workout workout = null;
        String workoutID;

        do {
            workoutID = Utils.getValue("Enter the Workout ID: ");

            if (!validateWorkoutID(workoutID)) {
                System.out.println("Invalid Workout ID format. Please try again.");
                continue;
            }

            workout = workoutSrv.findById(workoutID);
            if (workout == null) {
                System.out.println("Workout with ID " + workoutID + " not found.");
            }
        } while (workout == null);

        return workout;
    }

    public static Exercise validateAndFindExercise(ExerciseService exerciseSrv) {
        Exercise exercise = null;
        String exerciseID;

        do {
            exerciseID = Utils.getValue("Enter the Exercise ID: ");

            if (!validateExerciseID(exerciseID)) {
                System.out.println("Invalid Exercise ID format. Please try again.");
                continue;
            }

            exercise = exerciseSrv.findById(exerciseID);
            if (exercise == null) {
                System.out.println("Exercise with ID " + exerciseID + " not found.");
            }
        } while (exercise == null);

        return exercise;
    }
//----------------------------------------------------

    public static boolean convertStringToGender(String msg) {
        if (msg.equalsIgnoreCase("Male") || msg.equalsIgnoreCase("male") || msg.equalsIgnoreCase("M") || msg.equalsIgnoreCase("m")) {
            return true;
        } else if (msg.equalsIgnoreCase("Female") || msg.equalsIgnoreCase("female") || msg.equalsIgnoreCase("F") || msg.equalsIgnoreCase("f")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input for gender");
        }
    }
//----------------------------------------------------

    public static LocalDate convertStringToDate(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please enter date in format dd/MM/yyyy.");
            return null;
        }
    }
//----------------------------------------------------

    public static boolean validAge(LocalDate date) {
        LocalDate current = LocalDate.now();
        int age = Period.between(date, current).getYears();
        return age >= 18;
    }
//----------------------------------------------------

    public static String checkDob(String msg, String errMsg) {
        while (true) {
            try {
                String result = Utils.getValue(msg);
                if (result.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                    if (validAge(convertStringToDate(result))) {
                        return result;
                    }
                } else {
                    System.err.println(errMsg);
                }
            } catch (Exception e) {
                System.err.println(errMsg);
            }
        }
    }
//----------------------------------------------------

    public static String checkName(String msg, String errMsg) {
        boolean check = true;
        String result = null;
        do {
            try {
                result = Utils.getValue(msg);
                result = result.trim();
                if (result.matches("([A-Z][a-z]+\\s?)+")) {
                    return result;
                } else {
                    check = false;
                    System.err.println(errMsg);
                }
            } catch (Exception e) {
                System.err.println(errMsg);
            }
        } while (!check);
        return result;
    }
//----------------------------------------------------    

    public static int checkInt(String s, String errmsg) {
        int num = 0;
        while (true) {
            try {
                num = Integer.parseInt(Utils.getValue(s));
                if (num <= 0) {
                    System.out.println("Enter a number > 0");
                } else {
                    return num;
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static double checkDouble(String s, String errmsg) {
        double num = 0;
        while (true) {
            try {
                num = Double.parseDouble(Utils.getValue(s));
                if (num <= 0) {
                    System.out.println("Enter a number > 0");
                } else {
                    return num;
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static String checkString(String inputmsg, String errmsg, String regex) {

        while (true) {
            try {
                String s = Utils.getValue(inputmsg);
                if (s.matches(regex)) {
                    return s;
                } else {
                    System.out.println(errmsg);
                }
            } catch (Exception e) {
                System.err.println(errmsg);
            }
        }
    }
//----------------------------------------------------    

    public static char checkChar(String inputMsg, String errMsg, String regex) {

        while (true) {

            try {
                String input = Utils.getValue(inputMsg);
                if (input.isEmpty()) {
                    System.err.println(" Please enter a single character.");
                    continue;
                }

                // Check if input length is greater than 1
                if (input.length() > 1) {
                    System.err.println(errMsg + " Please enter only one character.");
                    continue;
                }

                char ch = input.charAt(0);

                // Check if character matches the regex (if provided)
                if (regex != null && !String.valueOf(ch).matches(regex)) {
                    System.err.println(errMsg);
                    continue;
                }

                return ch;
            } catch (Exception e) {
                System.err.println(errMsg);
            }
        }
    }

    // Validate Email
    public static boolean validateEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (Pattern.matches(emailPattern, email)) {
            return true;
        } else {
            System.out.println("Invalid email format.");
            return false;
        }
    }

    // Validate Phone Number
    public static boolean validatePhoneNumber(String phoneNumber) {
        String phonePattern = "^\\+?\\d{10}$"; // Allows optional + and expects 10
        if (Pattern.matches(phonePattern, phoneNumber)) {
            return true;
        } else {
            System.out.println("Invalid phone number format. Expected 10 digits.");
            return false;
        }
    }

    // Validate Person Name
    public static String validatePersonName(String name) {
        if (name != null && name.matches("[A-Za-z ]+")) {
            String[] words = name.split(" ");
            StringBuilder capitalized = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    capitalized.append(Character.toUpperCase(word.charAt(0)));
                    capitalized.append(word.substring(1).toLowerCase());
                    capitalized.append(" ");
                }
            }

            return capitalized.toString().trim();
        } else {
            System.out.println("Invalid name. Name should contain letters only.");
            return null;
        }
    }

    // Validate any Name
    public static boolean validateName(String courseName) {
        if (courseName != null && !courseName.trim().isEmpty()) {
            return true;
        } else {
            System.out.println("Name cannot be empty.");
            return false;
        }
    }

    // Validate Course Description
    public static boolean validateDescription(String courseDescription) {
        if (courseDescription != null && courseDescription.length() <= 500) {
            return true;
        } else {
            System.out.println("Description must not exceed 500 characters.");
            return false;
        }
    }
}
