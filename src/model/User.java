package model;

public class User extends Person {

    public User() {
    }

    public User(String id, String name, String birthDate, boolean gender, String phoneNumber, String email) {
        super(id, name, birthDate, gender, phoneNumber, email);
    }
//----------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!id.matches("USER-\\d{4}")) {
            throw new RuntimeException("Invalid User ID. Please enter again with format USER-xxxx..");
        }
        this.id = id;
    }
//----------------------------------------------------

    @Override
    public String toString() {
        return super.toString();
    }

}
