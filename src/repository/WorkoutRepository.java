package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Workout;

public final class WorkoutRepository implements IWorkoutRepository {

    private final ArrayList<Workout> workoutList = new ArrayList<>();

    public WorkoutRepository() {
        readFile();
    }

    public ArrayList<Workout> getWorkoutList() {
        return workoutList;
    }

    //----------------------------------------------------
    @Override
    public ArrayList<Workout> readFile() {
        String line;
        try (BufferedReader input = new BufferedReader(new FileReader(workoutPath))) {
            while ((line = input.readLine()) != null) {
                String[] tokens = line.split(",");

                if (tokens.length == 3) {
                    String id = tokens[0];
                    String workoutName = tokens[1];
                    String[] listOfExercise = tokens[2].split("\\|"); 

                    Workout workout = new Workout(id, workoutName, listOfExercise);
                    workoutList.add(workout);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading workout data: " + e.getMessage());
        }
        return workoutList;
    }

    //----------------------------------------------------
    @Override
    public void writeFile(ArrayList<Workout> workouts) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(workoutPath))) {
            for (Workout workout : workouts) {
                String exerciseIds = String.join("|", workout.getListOfExercise()); // Join exercise IDs with "|"
                String line = String.join(",",
                        workout.getId(),
                        workout.getWorkoutName(),
                        exerciseIds
                );
                output.write(line);
                output.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing workout data: " + e.getMessage());
        }
    }
}
