package view;

import model.Exercise;
import service.ExerciseService;
import utils.Utils;
import utils.Validation;

public class ExerciseView {

    private final ExerciseService exerciseService = new ExerciseService();

    public void displayAllExercises() {
        exerciseService.display();
    }
//----------------------------------------------------

    public void displayAddExercise() {
        System.out.println("\n--- Add New Exercise ---");

        String id = exerciseService.generateId();
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
        exerciseService.add(newExercise);
    }
//----------------------------------------------------

    public void displayDeleteExercise() {
        System.out.println("\n--- Delete Exercise ---");
        String id = Utils.getValue("Enter Exercise ID to delete: ");

        Exercise exerciseToDelete = exerciseService.findById(id);
        System.out.println(exerciseToDelete);
        if (exerciseToDelete != null) {
            String confirmation = Validation.getValue("Are you sure you want to delete this course? (y/n): ");
            if (confirmation.equalsIgnoreCase("y")) {
                exerciseService.delete(exerciseToDelete);
            } else {
                System.out.println("Deletion canceled.");
            }

        } else {
            System.out.println("Exercise with ID " + id + " not found.");
        }
    }
//----------------------------------------------------

    public void displayUpdateExercise() {
        System.out.println("\n--- Update Exercise ---");
        String id = Utils.getValue("Enter Exercise ID to update: ");

        Exercise existingExercise = exerciseService.findById(id);
        if (existingExercise == null) {
            System.out.println("Exercise with ID " + id + " not found.");
            return;
        }

        String name;
        do {
            name = Utils.getValue("Enter new Exercise Name (leave blank to keep current): ");
            if (name.isEmpty()) {
                name = existingExercise.getName();
                break;
            }
        } while (!Validation.validateName(name));

        String detail;
        do {
            detail = Utils.getValue("Enter new Exercise Detail (leave blank to keep current): ");
            if (detail.isEmpty()) {
                detail = existingExercise.getDetail();
                break;
            }
        } while (false);

        int duration;
        String durationInput = Utils.getValue("Enter new Exercise Duration (in minutes, leave blank to keep current): ");
        if (durationInput.isEmpty()) {
            duration = existingExercise.getDuration();
        } else {
            duration = Integer.parseInt(Utils.getValue(durationInput));
            while (duration <= 0) {
                duration = Integer.parseInt(Utils.getValue("Duration must be positive. Enter Exercise Duration again: "));
            }
        }

        existingExercise.setName(name);
        existingExercise.setDetail(detail);
        existingExercise.setDuration(duration);

        exerciseService.update(existingExercise);
    }
}
