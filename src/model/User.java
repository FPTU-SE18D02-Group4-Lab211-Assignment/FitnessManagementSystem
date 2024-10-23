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
public class User {

    private String userID;
    private String userPhone;
    private String userAddress;
    private String userGender;
    LocalDate userDob;
    private String nutrition;

    public User() {
    }

    public User(String userID, String userPhone, String userAddress,
            String userGender, LocalDate userDob, String nutrition) {
        this.userID = userID;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userGender = userGender;
        this.userDob = userDob;
        this.nutrition = nutrition;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public LocalDate getUserDob() {
        return userDob;
    }

    public void setUserDob(LocalDate userDob) {
        this.userDob = userDob;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+----------------+----------------+----------------+------------+------------+----------------+\n");
        sb.append("| User ID        | Phone          | Address        | Gender     | Date of Birth | Nutrition      |\n");
        sb.append("+----------------+----------------+----------------+------------+------------+----------------+\n");
        sb.append(String.format("| %-14s | %-14s | %-14s | %-10s | %-11s | %-14s |\n",
                userID, userPhone, userAddress, userGender, userDob, nutrition));
        sb.append("+----------------+----------------+----------------+------------+------------+----------------+\n");
        return sb.toString();
    }
}
