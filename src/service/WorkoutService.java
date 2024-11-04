package service;

import java.util.List;
import model.Workout;
import repository.WorkoutRepository;

public class WorkoutService implements IWorkoutService {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    public WorkoutService() {
    }
//----------------------------------------------------

    public String generateId() {
        List<Workout> workouts = workoutRepository.getWorkoutList();// Fetches all workouts
        if (workouts.isEmpty()) {
            return "WOR-0001";
        }

        // Sort workouts by ID and retrieve the last (highest) ID
        workouts.sort((w1, w2) -> w2.getId().compareTo(w1.getId()));
        String lastWorkoutId = workouts.get(0).getId();

        // Extract the numeric part of the ID and increment it
        int lastNumber = Integer.parseInt(lastWorkoutId.substring(4));
        return String.format("WOR-%04d", lastNumber + 1);
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
