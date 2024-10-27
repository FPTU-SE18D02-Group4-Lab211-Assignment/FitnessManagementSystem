package model;

import java.util.Arrays;

public class Workout {

    private String id;
    private String workoutName;
    private String[] listOfExercise;

    public Workout(String id, String workoutName, String[] listOfExercise) {
        this.id = id;
        this.workoutName = workoutName;
        this.listOfExercise = listOfExercise;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String[] getListOfExercise() {
        return listOfExercise;
    }

    public void setListOfExercise(String[] listOfExercise) {
        this.listOfExercise = listOfExercise;
    }

    @Override
    public String toString() {
        return "Workout ID: " + id + "\n"
                + "Workout Name: " + workoutName + "\n"
                + "Exercises: " + Arrays.toString(listOfExercise) + "\n";
    }
}
