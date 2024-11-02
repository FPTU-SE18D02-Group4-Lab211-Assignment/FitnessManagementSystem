package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Schedule {

    private String userID;
    private String workoutID;
    private String courseID;
    private int order;
    private LocalDate date;
    private boolean status;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Schedule(String userID, String workoutID, String courseID, int order, String date, boolean status) {
        this.userID = userID;
        this.workoutID = workoutID;
        this.courseID = courseID;
        this.order = order;
        this.setDate(date);
        this.status = status;
    }
//----------------------------------------------------

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(String workoutID) {
        this.workoutID = workoutID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        try {
            this.date = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
//----------------------------------------------------

    public String generateFileName() {
        return String.format("%s_%s.csv", userID, courseID);
    }
//----------------------------------------------------

    @Override
    public String toString() {
        return String.format("| %-10s | %-10s | %-10s | %-6d | %-10s | %-15s |",
                userID,
                workoutID,
                courseID,
                order,
                date.format(formatter),
                (status ? "Completed" : "Not Completed")
        );
    }
}
