package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Workout;

public final class WorkoutRepository implements IWorkoutRepository {
    private final ArrayList<Workout> workoutList = new ArrayList<>();
    
    public WorkoutRepository() {
        readFile();
    }
    
    public ArrayList<Workout> getWorkoutList() {
        return workoutList;
    }
     
    @Override
    public ArrayList<Workout> readFile() {
        return workoutList;
    }
    
    @Override
    public void writeFile(ArrayList<Workout> workouts){
        
    }    
}
