package view;

import model.Workout;
import model.Exercise; // Ensure this import exists if needed
import service.WorkoutService;
import service.ExerciseService;
import utils.Utils;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class WorkoutView {

    private final WorkoutService workoutService = new WorkoutService();
    private final ExerciseService exerciseService = new ExerciseService();

    public void displayAllWorkouts() {
        workoutService.display();
    }
    
    //----------------------------------------------------
    public void displayAddWorkout() {
        System.out.println("\n--- Add New Workout ---");

        String id;
        do {
            id = Utils.getValue("Enter Workout ID (WOR-XXXX): ");
        } while (!Validation.validateWorkoutID(id) || workoutService.findById(id) != null);

        String name;
        do {
            name = Utils.getValue("Enter Workout Name: ");
        } while (!Validation.validateName(name));

        // Adding exercises to the workout
        List<String> exerciseNames = new ArrayList<>();
        String addAnother;
        do {
            String exerciseId = Utils.getValue("Enter Exercise ID (EXE-XXXX) to add to the workout: ");
            if (!Validation.validateExerciseID(exerciseId)) {
                addAnother = "y"; // Prompt again if the exercise ID is invalid
                continue; // Skip to the next iteration
            }

            Exercise exercise = exerciseService.findById(exerciseId); // Specify the type
            if (exercise != null) {
                exerciseNames.add(exercise.getName()); // Store exercise name as String
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

        // Convert exerciseNames list to a String array
        String[] exerciseArray = exerciseNames.toArray(new String[0]);
        Workout newWorkout = new Workout(id, name, exerciseArray);
        workoutService.add(newWorkout);
        System.out.println("Workout added successfully.");
    }

    //----------------------------------------------------
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
        List<String> exerciseNames = new ArrayList<>(List.of(existingWorkout.getListOfExercise()));
        String updateExercises = Utils.getValue("Do you want to update exercises? (y/n): ");
        while (!updateExercises.equalsIgnoreCase("y") && !updateExercises.equalsIgnoreCase("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            updateExercises = Utils.getValue("Do you want to update exercises? (y/n): ");
        }

        if (updateExercises.equalsIgnoreCase("y")) {
            String action;
            do {
                System.out.println("\nCurrent Exercises:");
                for (int i = 0; i < exerciseNames.size(); i++) {
                    System.out.println((i + 1) + ". " + exerciseNames.get(i));
                }

                action = Utils.getValue("Do you want to (add/delete) an exercise or finish updating? (add/delete/finish): ");
                if (action.equalsIgnoreCase("add")) {
                    String exerciseId = Utils.getValue("Enter Exercise ID (EXE-XXXX) to add to the workout: ");
                    if (!Validation.validateExerciseID(exerciseId)) {
                        continue; // Skip to the next iteration if invalid
                    }

                    Exercise exercise = exerciseService.findById(exerciseId); // Specify the type
                    if (exercise != null) {
                        exerciseNames.add(exercise.getName());
                        System.out.println("Exercise added successfully.");
                    } else {
                        System.out.println("Invalid Exercise ID. Please try again.");
                    }
                } else if (action.equalsIgnoreCase("delete")) {
                    int indexToDelete = Integer.parseInt(Utils.getValue("Enter the number of the exercise to delete: ")) - 1;
                    if (indexToDelete >= 0 && indexToDelete < exerciseNames.size()) {
                        exerciseNames.remove(indexToDelete);
                        System.out.println("Exercise removed successfully.");
                    } else {
                        System.out.println("Invalid number. No exercise removed.");
                    }
                }
            } while (!action.equalsIgnoreCase("finish"));
        }

        // Update the workout details
        existingWorkout.setWorkoutName(name);
        existingWorkout.setListOfExercise(exerciseNames.toArray(new String[0])); // Convert List to String array
        workoutService.update(existingWorkout);
        System.out.println("Workout updated successfully.");
    }
}
