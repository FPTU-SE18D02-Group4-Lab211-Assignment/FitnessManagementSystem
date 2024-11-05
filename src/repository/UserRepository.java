package repository;

import model.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class UserRepository implements IUserRepository {

    private static ArrayList<User> userList = new ArrayList<>();

    private static final Pattern ID_PATTERN = Pattern.compile("USER-\\d{4}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+@\\w+\\.\\w+");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        userList = new UserRepository().readFile();
    }
//----------------------------------------------------

    public ArrayList<User> getUserList() {
        return userList;
    }
//----------------------------------------------------

    @Override
    public ArrayList<User> readFile() {
        ArrayList<User> userList = new ArrayList<>(); // Initialize userList to avoid null pointer issues

        try (BufferedReader reader = new BufferedReader(new FileReader(path + userPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String id = data[0].trim();
                    String name = data[1].trim();
                    String birthday = data[2].trim();
                    String genderStr = data[3].trim();
                    String phoneNumber = data[4].trim();
                    String email = data[5].trim();

                    // Validate ID format
                    if (!ID_PATTERN.matcher(id).matches()) {
                        System.err.println("Invalid ID format for user: " + id);
                        continue;
                    }

                    // Validate phone number format
                    if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                        System.err.println("Invalid phone number format for user: " + phoneNumber);
                        continue;
                    }

                    // Validate email format
                    if (!EMAIL_PATTERN.matcher(email).matches()) {
                        System.err.println("Invalid email format for user: " + email);
                        continue;
                    }

                    // Parse birth date
                    LocalDate birthDate;
                    try {
                        birthDate = LocalDate.parse(birthday, DATE_FORMATTER);
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid birth date format for user: " + birthday);
                        continue;
                    }

                    // Determine gender
                    boolean gender = genderStr.equalsIgnoreCase("male");

                    // Create and add the user
                    User user = new User(id, name, birthday, gender, phoneNumber, email);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return userList;
    }

//----------------------------------------------------
    @Override
    public void writeFile(ArrayList<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + userPath))) {
            for (User user : users) {
                String line = String.join(",",
                        user.getId(),
                        user.getName(),
                        user.getBirthDate().format(DATE_FORMATTER),
                        user.isGender() ? "Male" : "Female",
                        user.getPhoneNumber(),
                        user.getEmail()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
