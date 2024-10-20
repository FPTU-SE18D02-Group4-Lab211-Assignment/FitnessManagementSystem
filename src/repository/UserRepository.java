package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.User;

public final class UserRepository implements IUserRepository {
    private final ArrayList<User> userList = new ArrayList<>();
    
    public UserRepository() {
        readFile();
    }
    
    public ArrayList<User> getUserList() {
        return userList;
    }
     
    @Override
    public ArrayList<User> readFile() {
        return userList;
    }
    
    @Override
    public void writeFile(ArrayList<User> users){
        
    }    
}
