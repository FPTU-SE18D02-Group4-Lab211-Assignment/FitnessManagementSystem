package view;

import model.Exercise;
import service.ExerciseService;
import utils.Utils;
import utils.Validation;

public class ExerciseView {

    private final ExerciseService exerciseSrv = new ExerciseService();

    public void displayAllExercises() {
        exerciseSrv.display();
    }
//----------------------------------------------------

    public void displayAddExercise() {
        System.out.println("\n--- Add New Exercise ---");

        String id = exerciseSrv.generateExerciseID();
        System.out.println("New Exercise ID: " + id);

        String name;
        do {
            name = Utils.getValue("Enter Exercise Name: ");
        } while (!Validation.validateName(name));

        String detail = Utils.getValue("Enter Exercise Detail: ");

        int duration;
        do {
            duration = Integer.parseInt(Utils.getValue("Enter Exercise Duration (in minutes): "));
        } while (duration <= 0);

        Exercise newExercise = new Exercise(id, name, detail, duration);
        exerciseSrv.add(newExercise);
    }
//----------------------------------------------------

    public void displayDeleteExercise() {
        System.out.println("\n--- Delete Exercise ---");

        Exercise exerciseToDelete = Validation.validateAndFindExercise(exerciseSrv);
        String id = exerciseToDelete.getId();

        System.out.println(exerciseToDelete);
        String confirmation = Validation.getValue("Are you sure you want to delete this course? (y/n): ");
        if (confirmation.equalsIgnoreCase("y")) {
            exerciseSrv.delete(exerciseToDelete);
        } else {
            System.out.println("Deletion canceled.");
        }

    }
//----------------------------------------------------

    public void displayUpdateExercise() {
        System.out.println("\n--- Update Exercise ---");

        // Validate and find the existing exercise
        Exercise existingExercise = Validation.validateAndFindExercise(exerciseSrv);
        if (existingExercise == null) {
            System.out.println("Exercise not found.");
            return;
        }

        // Display current exercise details
        System.out.println("Current Exercise: " + existingExercise);

        // Update Exercise Name
        String name;
        do {
            name = Utils.getValue("Enter new Exercise Name (leave blank to keep current): ");
            if (name.isEmpty()) {
                name = existingExercise.getName(); // Keep current name if input is blank
                break;
            }
        } while (!Validation.validateName(name));
        existingExercise.setName(name);

        // Update Exercise Detail
        String detail = Utils.getValue("Enter new Exercise Detail (leave blank to keep current): ");
        if (detail.isEmpty()) {
            detail = existingExercise.getDetail(); // Keep current detail if input is blank
        }
        existingExercise.setDetail(detail);

        // Update Exercise Duration
        int duration;
        String durationInput = Utils.getValue("Enter new Exercise Duration (in minutes, leave blank to keep current): ");
        if (durationInput.isEmpty()) {
            duration = existingExercise.getDuration(); // Keep current duration if input is blank
        } else {
            try {
                duration = Integer.parseInt(durationInput);
                while (duration <= 0) {
                    duration = Integer.parseInt(Utils.getValue("Duration must be positive. Enter Exercise Duration again: "));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Duration must be a number.");
                return;
            }
        }
        existingExercise.setDuration(duration);

        // Update the exercise in the service
        exerciseSrv.update(existingExercise);
        System.out.println("Exercise updated successfully!");
    }

}
