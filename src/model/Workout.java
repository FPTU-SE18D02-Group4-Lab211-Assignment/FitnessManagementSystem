package model;

import java.util.List;

public class Workout {

    private String id;
    private String workoutName;
    private List<Exercise> listOfExercise;
    private boolean status;

    public Workout(String id, String workoutName, List<Exercise> listOfExercise) {
        this.id = id;
        this.workoutName = workoutName;
        this.listOfExercise = listOfExercise;
        this.status = false;
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

    public List<Exercise> getListOfExercise() {
        return listOfExercise;
    }

    public void setListOfExercise(List<Exercise> listOfExercise) {
        this.listOfExercise = listOfExercise;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Workout ID: " + id + "\n"
                + "Workout Name: " + workoutName + "\n"
                + "Exercises: " + listOfExercise + "\n"
                + "Status: " + (status ? "Done" : "Not yet") + "\n";
    }
}
