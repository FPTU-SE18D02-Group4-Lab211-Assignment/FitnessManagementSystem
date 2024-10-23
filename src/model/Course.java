/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class Course {
    private String courseID;
    private String courseName;
    private String courseDescription;
    private double coursePrice;

    public Course() {
    }

    public Course(String courseID, String courseName, String courseDescription, double coursePrice) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.coursePrice = coursePrice;
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

    public double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(double coursePrice) {
        this.coursePrice = coursePrice;
    }

    @Override
    public String toString() {
        return "Course{" + "courseID=" + courseID + ", courseName=" + courseName + ", courseDescription=" + courseDescription + ", coursePrice=" + coursePrice + '}';
    }
    
}
