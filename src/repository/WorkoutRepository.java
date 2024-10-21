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

    @Override
    public ArrayList<Workout> readFile() {
        String line;
        try (BufferedReader input = new BufferedReader(new FileReader(path + workoutPath))) {
            while ((line = input.readLine()) != null) {
                // Assuming the CSV format is: id,workoutName,description,duration,type,intensity
                String[] tokens = line.split(",");

                if (tokens.length == 6) {
                    // Create a Workout instance from the parsed CSV data
                    Workout workout = new Workout(
                            tokens[0], // id
                            tokens[1], // workoutName
                            tokens[2], // description
                            Integer.parseInt(tokens[3]), // duration
                            tokens[4], // type
                            tokens[5] // intensity
                    );
                    workoutList.add(workout);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading workout data: " + e.getMessage());
        }
        return workoutList;
    }

    @Override
    public void writeFile(ArrayList<Workout> workouts) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + workoutPath))) {
            for (Workout workout : workouts) {
                // Write workout data to file in CSV format
                String line = String.join(",",
                        workout.getId(),
                        workout.getWorkoutName(),
                        workout.getDescription(),
                        String.valueOf(workout.getDuration()),
                        workout.getType(),
                        workout.getIntensity()
                );
                output.write(line);
                output.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing workout data: " + e.getMessage());
        }
    }
}
