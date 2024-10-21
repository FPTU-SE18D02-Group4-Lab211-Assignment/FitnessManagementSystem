package service;

import model.Workout;
import repository.WorkoutRepository;

public class WorkoutService implements IWorkoutService {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    public WorkoutService() {
    }

    // Find a workout by its ID
    @Override
    public Workout findById(String id) {
        for (Workout workout : workoutRepository.getWorkoutList()) {
            if (workout.getId().equals(id)) {
                return workout;
            }
        }
        return null; // Return null if no workout with the given ID is found
    }

    // Display all workouts in the list
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

    // Add a new workout
    @Override
    public void add(Workout newWorkout) {
        if (findById(newWorkout.getId()) == null) {
            workoutRepository.getWorkoutList().add(newWorkout);
            System.out.println("Workout added successfully.");
        } else {
            System.out.println("Workout with ID " + newWorkout.getId() + " already exists.");
        }
    }

    // Update an existing workout
    @Override
    public void update(Workout updatedWorkout) {
        Workout existingWorkout = findById(updatedWorkout.getId());
        if (existingWorkout != null) {
            // Update fields of the found workout
            existingWorkout.setWorkoutName(updatedWorkout.getWorkoutName());
            existingWorkout.setDescription(updatedWorkout.getDescription());
            existingWorkout.setDuration(updatedWorkout.getDuration());
            existingWorkout.setType(updatedWorkout.getType());
            existingWorkout.setIntensity(updatedWorkout.getIntensity());
            System.out.println("Workout updated successfully.");
        } else {
            System.out.println("Workout with ID " + updatedWorkout.getId() + " not found.");
        }
    }

    // Save the current list of workouts to the file
    @Override
    public void save() {
        workoutRepository.writeFile(workoutRepository.getWorkoutList());
        System.out.println("Workouts saved to file.");
    }
}
