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

        String id = exerciseSrv.generateId();
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

        Exercise existingExercise = Validation.validateAndFindExercise(exerciseSrv);
        System.out.println(existingExercise);

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

        exerciseSrv.update(existingExercise);
    }
}
