package model;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Coach extends Person {
    private ArrayList<Course> courseList;
    private ArrayList<User> userList;

    public Coach() {
    }

    public Coach(String id, String name, String birthDate, boolean gender, String phoneNumber, String email) {
        super(id, name, birthDate, gender, phoneNumber, email);
        ArrayList<Course> courseList = new ArrayList<>();
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }
    
    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public void setId(String id) {
        if (!Pattern.matches("COA-\\d{4}", id)) {
            System.err.println("Invalid coach ID. Must be in the format COA-YYYY.");
        }
        super.id = id;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
}
