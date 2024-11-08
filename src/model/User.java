package model;

public class User extends Person {

    public User() {
    }

    public User(String id, String name, String birthDate, boolean gender, String phoneNumber, String email) {
        super(id, name, birthDate, gender, phoneNumber, email);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!id.matches("USER-\\d{4}")) {
            throw new RuntimeException("Invalid User ID. Please enter again with format USER-xxxx..");
        }
        this.id = id;
    }
    
    public boolean getGender() {
        return gender; 
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Birth Date: %s, Gender: %s, Phone: %s, Email: %s",
                id,
                name,
                birthDate,
                gender ? "Male" : "Female", 
                phoneNumber,
                email);
    }

}
