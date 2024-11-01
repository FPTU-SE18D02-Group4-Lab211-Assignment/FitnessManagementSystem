package service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import model.User;
import utils.Validation;

public class UserService implements IUserService {

    private Map<String, User> users = new HashMap<>();
    private Map<String, Map<String, Integer>> userCourseStatus = new HashMap<>();

    @Override
    public User findById(String id) {
        return users.get(id);
    }

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

    public void addnewU() {
        while (true) {
            String id = Validation.getValue("Enter ID: ");

            if (users.containsKey(id)) {
                System.out.println("User with ID " + id + " already exists. Please enter a different ID.");
                continue;
            }

            String name = Validation.getValue("Enter name: ");
            String email = Validation.getValue("Enter email: ");
            String phone = Validation.getValue("Enter phone: ");
            LocalDate birthDate = null;

            while (birthDate == null) {
                String dateStr = Validation.getValue("Enter birth date (dd/MM/yyyy): ");
                birthDate = parseDate(dateStr);
                if (birthDate == null) {
                    System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
                }
            }

            boolean gender;
            while (true) {
                String genderInput = Validation.getValue("Enter gender (Male/Female): ");
                if (genderInput.equalsIgnoreCase("Male")) {
                    gender = true;
                    break;
                } else if (genderInput.equalsIgnoreCase("Female")) {
                    gender = false;
                    break;
                }
                System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
            }

            User user = new User(id, name, birthDate.toString(), gender, phone, email);

            add(user);
            userCourseStatus.put(id, new HashMap<>());

            System.out.println("User added successfully: " + user);
            break;
        }
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void updateCourseStatus(String userId, String courseId, int status) {
        if (userCourseStatus.containsKey(userId) && userCourseStatus.get(userId).containsKey(courseId)) {
            userCourseStatus.get(userId).put(courseId, status);
            System.out.println("Course " + courseId + " for user " + userId + " updated to status " + status);
        } else {
            System.out.println("Course or User not found.");
        }
    }

    @Override
    public void display() {
        users.forEach((id, user) -> {
            System.out.println("User: " + user);
            System.out.println("Course Status: " + userCourseStatus.get(id));
        });
    }

    @Override
    public void save() {
        System.out.println("Save functionality is not implemented yet.");
    }

    @Override
    public void update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            System.out.println("User updated successfully.");
        } else {
            System.out.println("User not found!");
        }
    }

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
