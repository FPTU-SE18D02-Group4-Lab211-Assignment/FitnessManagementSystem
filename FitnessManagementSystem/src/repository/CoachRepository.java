package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Coach;

public final class CoachRepository implements ICoachRepository {
    private final ArrayList<Coach> coachList = new ArrayList<>();
    
    public CoachRepository() {
        readFile();
    }
    
    public ArrayList<Coach> getCouchList() {
        return coachList;
    }
     
    @Override
    public ArrayList<Coach> readFile() {
        return coachList;
    }
    
    @Override
    public void writeFile(ArrayList<Coach> coaches){
        
    }    
}
