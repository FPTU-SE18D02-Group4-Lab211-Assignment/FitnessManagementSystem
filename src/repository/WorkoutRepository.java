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
        try (BufferedReader input = new BufferedReader(new FileReader(path + workoutPath))) {
            while ((line = input.readLine()) != null) {
                String[] tokens = line.split(",");

                if (tokens.length == 2) {
                    String id = tokens[0];
                    String workoutName = tokens[1];

                    Workout workout = new Workout(id, workoutName, new ArrayList<>());
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
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + workoutPath))) {
            for (Workout workout : workouts) {
                String line = String.join(",",
                        workout.getId(),
                        workout.getWorkoutName(),
                        String.valueOf(workout.isStatus())
                );
                output.write(line);
                output.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing workout data: " + e.getMessage());
        }
    }
}
