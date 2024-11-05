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

    private final WorkoutService workoutSrv = new WorkoutService();
    private final ExerciseService exerciseService = new ExerciseService();

    public void displayAllWorkouts() {
        workoutSrv.display();
    }

    //----------------------------------------------------
    public void displayAddWorkout() {
        System.out.println("\n--- Add New Workout ---");

        // Generate and display new Workout ID
        String id = workoutSrv.generateId();
        System.out.println("New Workout ID: " + id);

        // Input and validate Workout Name
        String name;
        do {
            name = Utils.getValue("Enter Workout Name: ");
        } while (!Validation.validateName(name));

        // Adding exercises to the workout
        List<String> exerciseIds = new ArrayList<>();
        String addAnother;
        do {
            // Prompt user to enter an exercise ID
            String exerciseId = Utils.getValue("Enter Exercise ID to add to the workout: ");
            Exercise exercise = exerciseService.findById(exerciseId); // Use entered ID to look up exercise

            if (exercise != null) {
                // If exercise exists, add its ID to the list
                exerciseIds.add(exercise.getId());
                System.out.println("Exercise " + exercise.getName() + " added to workout.");
            } else {
                // Invalid ID entered
                System.out.println("Invalid Exercise ID. Please try again.");
            }

            // Ask if user wants to add another exercise
            addAnother = Utils.getValue("Add another exercise? (y/n): ");
            while (!addAnother.equalsIgnoreCase("y") && !addAnother.equalsIgnoreCase("n")) {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
                addAnother = Utils.getValue("Add another exercise? (y/n): ");
            }

        } while (addAnother.equalsIgnoreCase("y"));

        // Convert exerciseIds list to a String array
        String[] exerciseArray = exerciseIds.toArray(new String[0]);

        // Create a new Workout and add it to the service
        Workout newWorkout = new Workout(id, name, exerciseArray);
        workoutSrv.add(newWorkout);
        System.out.println("New workout added successfully!");
    }

//----------------------------------------------------

    public void displayDeleteWorkout() {
        System.out.println("===== Delete Workout =====");

        Workout workoutToDelete = Validation.validateAndFindWorkout(workoutSrv);
        System.out.println("Workout found: " + workoutToDelete);

        String confirmation = Utils.getValue("Are you sure you want to delete this workout? (y/n): ");
        if (confirmation.equalsIgnoreCase("y")) {
            workoutSrv.delete(workoutToDelete);
        } else {
            System.out.println("Deletion canceled.");
        }

    }

//----------------------------------------------------
    public void displayUpdateWorkout() {
        System.out.println("\n--- Update Workout ---");
        Workout existingWorkout = Validation.validateAndFindWorkout(workoutSrv);

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
                // Display the updated list of exercises
                System.out.println("\nCurrent Exercises:");
                for (int i = 0; i < exerciseNames.size(); i++) {
                    System.out.println((i + 1) + ". " + exerciseNames.get(i));
                }

                action = Utils.getValue("Do you want to (add/delete) an exercise or finish updating? (add/delete/finish): ");
                if (action.equalsIgnoreCase("add")) {
                    String exerciseId = Utils.getValue("Enter Exercise ID (EXE-XXXX) to add to the workout: ");
                    if (!Validation.validateExerciseID(exerciseId)) {
                        System.out.println("Invalid Exercise ID format. Please try again.");
                        continue;
                    }

                    Exercise exercise = exerciseService.findById(exerciseId);
                    if (exercise != null) {
                        if (exerciseNames.contains(exercise.getId())) {
                            System.out.println("Exercise already exists in the workout.");
                        } else {
                            exerciseNames.add(exercise.getId());
                            System.out.println("Exercise added successfully.");
                        }
                    } else {
                        System.out.println("Invalid Exercise ID. Please try again.");
                    }
                } else if (action.equalsIgnoreCase("delete")) {
                    int indexToDelete = Utils.checkInt("Enter the number of the exercise to delete: ", "Must be a positive integer") - 1;
                    if (indexToDelete >= 0 && indexToDelete < exerciseNames.size()) {
                        exerciseNames.remove(indexToDelete);
                        System.out.println("Exercise removed successfully.");
                    } else {
                        System.out.println("Invalid number. No exercise removed.");
                    }
                }
            } while (!action.equalsIgnoreCase("finish"));
        }

        // Update the workout with the new name and exercise list
        existingWorkout.setWorkoutName(name);
        existingWorkout.setListOfExercise(exerciseNames.toArray(new String[0])); // Convert List to String array
        workoutSrv.update(existingWorkout);
        System.out.println("Workout updated successfully!");
    }

}
