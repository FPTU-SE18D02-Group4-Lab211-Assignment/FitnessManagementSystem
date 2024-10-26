package view;

import model.Workout;
import model.Exercise;
import service.WorkoutService;
import service.ExerciseService;
import utils.Utils;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class WorkoutView {

    private final WorkoutService workoutService = new WorkoutService();
    private final ExerciseService exerciseService = new ExerciseService();
//----------------------------------------------------

    public void displayAddWorkout() {
        System.out.println("\n--- Add New Workout ---");

        String id;
        do {
            id = Utils.getValue("Enter Workout ID (WOR-XXXX): ");
            if (workoutService.findById(id) != null) { // Check if the ID already exists
                System.out.println("This Workout ID already exists. Please enter a unique ID.");
            }
        } while (!Validation.validateWorkoutID(id) || workoutService.findById(id) != null);

        String name;
        do {
            name = Utils.getValue("Enter Workout Name: ");
        } while (!Validation.validateName(name));

        // Adding exercises to the workout
        List<Exercise> exerciseList = new ArrayList<>();
        String addAnother;
        do {
            String exerciseId = Utils.getValue("Enter Exercise ID (EXE-XXXX) to add to the workout: ");
            Exercise exercise = exerciseService.findById(exerciseId);
            if (exercise != null) {
                exerciseList.add(exercise);
                addAnother = Utils.getValue("Add another exercise? (y/n): ");
                while (!addAnother.equalsIgnoreCase("y") && !addAnother.equalsIgnoreCase("n")) {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    addAnother = Utils.getValue("Add another exercise? (y/n): ");
                }
            } else {
                System.out.println("Invalid Exercise ID. Please try again.");
                addAnother = "y"; // Prompt again if the exercise is invalid
            }
        } while (addAnother.equalsIgnoreCase("y"));

        Workout newWorkout = new Workout(id, name, exerciseList);
        workoutService.add(newWorkout);
        System.out.println("Workout added successfully.");
    }

    public void displayUpdateWorkout() {
        System.out.println("\n--- Update Workout ---");
        String id = Utils.getValue("Enter Workout ID to update: ");

        Workout existingWorkout = workoutService.findById(id);
        if (existingWorkout == null) {
            System.out.println("Workout with ID " + id + " not found.");
            return;
        }

        String name;
        do {
            name = Utils.getValue("Enter new Workout Name (leave blank to keep current): ");
            if (name.isEmpty()) {
                name = existingWorkout.getWorkoutName();
                break;
            }
        } while (!Validation.validateName(name));

        // Updating exercises in the workout
        List<Exercise> exerciseList = existingWorkout.getListOfExercise();
        String updateExercises = Utils.getValue("Do you want to update exercises? (y/n): ");
        while (!updateExercises.equalsIgnoreCase("y") && !updateExercises.equalsIgnoreCase("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            updateExercises = Utils.getValue("Do you want to update exercises? (y/n): ");
        }

        if (updateExercises.equalsIgnoreCase("y")) {
            String action;
            do {
                System.out.println("\nCurrent Exercises:");
                for (int i = 0; i < exerciseList.size(); i++) {
                    System.out.println((i + 1) + ". " + exerciseList.get(i));
                }

                action = Utils.getValue("Do you want to (add/delete) an exercise or finish updating? (add/delete/finish): ");
                if (action.equalsIgnoreCase("add")) {
                    String exerciseId = Utils.getValue("Enter Exercise ID (EXE-XXXX) to add to the workout: ");
                    Exercise exercise = exerciseService.findById(exerciseId);
                    if (exercise != null) {
                        exerciseList.add(exercise);
                        System.out.println("Exercise added successfully.");
                    } else {
                        System.out.println("Invalid Exercise ID. Please try again.");
                    }
                } else if (action.equalsIgnoreCase("delete")) {
                    int indexToDelete = Integer.parseInt(Utils.getValue("Enter the number of the exercise to delete: ")) - 1;
                    if (indexToDelete >= 0 && indexToDelete < exerciseList.size()) {
                        exerciseList.remove(indexToDelete);
                        System.out.println("Exercise removed successfully.");
                    } else {
                        System.out.println("Invalid number. No exercise removed.");
                    }
                }
            } while (!action.equalsIgnoreCase("finish"));
        }

        existingWorkout.setWorkoutName(name);
        existingWorkout.setListOfExercise(exerciseList);
        workoutService.update(existingWorkout);
        System.out.println("Workout updated successfully.");
    }
}
