package model;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public abstract class Person {

    protected String id;
    private String name;
    private LocalDate birthDate;
    private boolean gender;
    private String phoneNumber;
    private String email;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Person() {
    }

    public Person(String id, String name, String birthDate, boolean gender, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        setBirthDate(birthDate);
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Pattern.matches("([A-Z][a-z]*\\s*)+", name)) {
            System.err.println("Invalid name. Each word must start with an uppercase letter.");
        }
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDateStr) throws DateTimeParseException {
        try {
            this.birthDate = LocalDate.parse(birthDateStr, dateFormatter); // Parsing string to LocalDate
        } catch (DateTimeParseException e) {
            System.err.println("");
        }
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!Pattern.matches("^0\\d{9}$", phoneNumber)) {
            System.err.println("Invalid phone number. 10 digits max and start with '0'!");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Pattern.matches("\\w+@\\w+\\.\\w+", email)) {
            System.err.println("Invalid email. Must be in the format <username>@<domain>.<extension>");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %-15s | %-6s | %-20s | %-20s |",
                id,
                name,
                birthDate.format(dateFormatter), // Formatting LocalDate to string
                gender ? "Male" : "Female",
                phoneNumber,
                email
        );
    }
}
