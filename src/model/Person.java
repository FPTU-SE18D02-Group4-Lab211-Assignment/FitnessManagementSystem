package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class Person {

    private String id;
    private String name;
    private LocalDate birthDate;
    private boolean gender;
    private String phoneNumber;
    private String email;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDateStr) throws DateTimeParseException {
        this.birthDate = LocalDate.parse(birthDateStr, dateFormatter); // Parsing string to LocalDate
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
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
