package service;

import model.Workout;
import repository.WorkoutRepository;

public class WorkoutService implements IWorkoutService {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    public WorkoutService() {
    }
//----------------------------------------------------

    @Override
    public Workout findById(String id) {
        for (Workout workout : workoutRepository.getWorkoutList()) {
            if (workout.getId().equals(id)) {
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
    public void update(Workout updatedWorkout) {
        Workout existingWorkout = findById(updatedWorkout.getId());
        if (existingWorkout != null) {
            existingWorkout.setWorkoutName(updatedWorkout.getWorkoutName());
            existingWorkout.setListOfExercise(updatedWorkout.getListOfExercise());
            existingWorkout.setStatus(updatedWorkout.isStatus());
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
