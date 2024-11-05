package service;

import java.util.List;
import model.Workout;
import repository.WorkoutRepository;

public class WorkoutService implements IWorkoutService {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    public WorkoutService() {
    }
//----------------------------------------------------

    public String generateWorkoutID() {
        int maxId = 0;
        for (Workout workout : workoutRepository.getWorkoutList()) {
            String[] parts = workout.getId().split("-");
            if (parts.length == 2 && parts[0].equals("WOR")) {
                try {
                    int idNum = Integer.parseInt(parts[1]);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid workout ID format: " + workout.getId());
                }
            }
        }
        return "WOR-" + String.format("%04d", maxId + 1);
    }

//----------------------------------------------------
    @Override
    public Workout findById(String id) {
        String trimmedId = id.trim();
        for (Workout workout : workoutRepository.getWorkoutList()) {
            if (workout.getId().equals(trimmedId)) {
                return workout;
            }
        }
        return null;
    }
//----------------------------------------------------

    @Override
    public void display() {
        if (workoutRepository.getWorkoutList().isEmpty()) {
            System.out.println("No workouts available.");
        } else {
            for (Workout workout : workoutRepository.getWorkoutList()) {
                System.out.println(workout);
            }
        }
    }
//----------------------------------------------------

    @Override
    public void add(Workout newWorkout) {
        if (findById(newWorkout.getId()) == null) {
            workoutRepository.getWorkoutList().add(newWorkout);
            System.out.println("Workout added successfully.");
            save();
        } else {
            System.out.println("Workout with ID " + newWorkout.getId() + " already exists.");
        }
    }
//----------------------------------------------------

    @Override
    public void delete(Workout workoutToDelete) {
        if (workoutToDelete == null) {
            System.err.println("Error: Workout not found.");
            return;
        }

        // Attempt to remove the workout from the list
        if (workoutRepository.getWorkoutList().remove(workoutToDelete)) {
            System.out.println("Workout with ID " + workoutToDelete.getId() + " has been successfully deleted.");
            // Save the updated workout list to the file
            save();
        } else {
            System.err.println("Error: Failed to delete workout with ID " + workoutToDelete.getId());
        }
    }
//----------------------------------------------------

    @Override
    public void update(Workout updatedWorkout) {
        Workout existingWorkout = findById(updatedWorkout.getId());
        if (existingWorkout != null) {
            existingWorkout.setWorkoutName(updatedWorkout.getWorkoutName());
            existingWorkout.setListOfExercise(updatedWorkout.getListOfExercise());
            System.out.println("Workout updated successfully.");
            save();
        } else {
            System.out.println("Workout with ID " + updatedWorkout.getId() + " not found.");
        }
    }
//----------------------------------------------------

    @Override
    public void save() {
        workoutRepository.writeFile(workoutRepository.getWorkoutList());
        System.out.println("Workouts saved to file.");
    }
}
