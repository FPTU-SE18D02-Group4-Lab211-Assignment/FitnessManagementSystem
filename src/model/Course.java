package model;

import java.time.LocalDate;
import java.util.List;
 
public class Course {
    
    private String courseID;
    private String courseName;
    private String courseDescription;
    private int courseDuration;
    private double coursePrice;
    private Coach coachID;
    private List<String> listOfWorkout;

    public Course() {
    }

    public Course(String courseID, String courseName, String courseDescription, int courseDuration, double coursePrice, Coach coachID, List<String> listOfWorkout) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseDuration = courseDuration;
        this.coursePrice = coursePrice;
        this.coachID = coachID;
        this.listOfWorkout = listOfWorkout;
    }

    public List<String> getListOfWorkout() {
        return listOfWorkout;
    }

    public void setListOfWorkout(List<String> listOfWorkout) {
        this.listOfWorkout = listOfWorkout;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Coach getCoachID() {
        return coachID;
    }

    public void setCoachID(Coach coachID) {
        this.coachID = coachID;
    }

    @Override
    public String toString() {
        return "Course{" + "courseID=" + courseID + ", courseName=" + courseName + ", courseDescription=" + courseDescription + ", courseDuration=" + courseDuration + ", coursePrice=" + coursePrice + ", coachID=" + coachID + ", listOfWorkout=" + listOfWorkout + '}';
    }

    
}
