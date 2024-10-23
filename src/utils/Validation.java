package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validation {

    public static String getValue(String input) {
        Scanner sc = new Scanner(System.in);
        System.out.print(input);
        return sc.nextLine();
    }

    public static boolean validateWorkoutID(String id) {
        if (id.matches("WOR-\\d{4}")) {
            return true;
        } else {
            System.out.println("Invalid Workout ID format. Expected WOR-YYYY (Y is a digit).");
            return false;
        }
    }

    public static boolean convertStringToGender(String msg) {
        if (msg.equalsIgnoreCase("Male") || msg.equalsIgnoreCase("male") || msg.equalsIgnoreCase("M") || msg.equalsIgnoreCase("m")) {
            return true;
        } else if (msg.equalsIgnoreCase("Female") || msg.equalsIgnoreCase("female") || msg.equalsIgnoreCase("F") || msg.equalsIgnoreCase("f")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input for gender");
        }
    }

    public static LocalDate convertStringToDate(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please enter date in format dd/MM/yyyy.");
            return null;
        }
    }

    public static boolean validAge(LocalDate date) {
        LocalDate current = LocalDate.now();
        int age = Period.between(date, current).getYears();
        return age >= 18;
    }

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

    // Validate Course Duration
    public static boolean validateCourseDuration(int durationInWeeks) {
        if (durationInWeeks > 0 && durationInWeeks <= 52) { // 1 week to 1 year
            return true;
        } else {
            System.out.println("Duration must be between 1 and 52 weeks.");
            return false;
        }
    }

    // Validate Workout Duration
    public static boolean validateWorkoutDuration(int duration) {
        if (duration > 0 && duration <= 120) { // 1 to 120 minutes (2 hours max)
            return true;
        } else {
            System.out.println("Workout duration must be between 1 and 120 minutes.");
            return false;
        }
    }

    // Validate Workout Type
    public static boolean validateWorkoutType(String type) {
        if (type.equalsIgnoreCase("cardio") || type.equalsIgnoreCase("strength") || type.equalsIgnoreCase("flexibility")) {
            return true;
        } else {
            System.out.println("Invalid workout type. Expected: cardio, strength, flexibility.");
            return false;
        }
    }

    // Validate Workout Intensity
    public static boolean validateWorkoutIntensity(String intensity) {
        if (intensity.equalsIgnoreCase("low") || intensity.equalsIgnoreCase("medium") || intensity.equalsIgnoreCase("high")) {
            return true;
        } else {
            System.out.println("Invalid intensity. Expected: low, medium, high.");
            return false;
        }
    }

    // Validate User Fitness Level
    public static boolean validateUserFitnessLevel(int fitnessLevel) {
        if (fitnessLevel >= 1 && fitnessLevel <= 10) { // Fitness level 1-10 scale
            return true;
        } else {
            System.out.println("Fitness level must be between 1 and 10.");
            return false;
        }
    }

    // Validate User Weight
    public static boolean validateUserWeight(double weight) {
        if (weight > 0 && weight < 300) { // Weight limit between 0 and 300 kg
            return true;
        } else {
            System.out.println("Invalid weight. Expected value between 0 and 300 kg.");
            return false;
        }
    }

    // Validate User Body Fat Percentage
    public static boolean validateUserBodyFatPercentage(double bodyFatPercentage) {
        if (bodyFatPercentage >= 0 && bodyFatPercentage <= 100) { // Percentage should be within 0% to 100%
            return true;
        } else {
            System.out.println("Body fat percentage must be between 0 and 100.");
            return false;
        }
    }
}
