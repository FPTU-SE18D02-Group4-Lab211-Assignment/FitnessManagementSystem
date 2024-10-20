package utils;

import java.util.regex.Pattern;

public class Validation {

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
