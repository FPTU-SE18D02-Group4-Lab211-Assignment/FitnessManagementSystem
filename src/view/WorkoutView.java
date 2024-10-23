package view;

import model.Workout;
import service.WorkoutService;
import utils.Utils;
import utils.Validation;

public class WorkoutView {

    private final WorkoutService workoutService = new WorkoutService();

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

        String description;
        do {
            description = Utils.getValue("Enter Description: ");
        } while (!Validation.validateDescription(description));

        int duration;
        do {
            duration = Integer.parseInt(Utils.getValue("Enter Duration (in minutes): "));
        } while (!Validation.validateWorkoutDuration(duration));

        String type;
        do {
            type = Utils.getValue("Enter Workout Type (cardio/strength/flexibility): ");
        } while (!Validation.validateWorkoutType(type));

        String intensity;
        do {
            intensity = Utils.getValue("Enter Workout Intensity (low/medium/high): ");
        } while (!Validation.validateWorkoutIntensity(intensity));

        Workout newWorkout = new Workout(id, name, description, duration, type, intensity);
        workoutService.add(newWorkout);
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
                name = existingWorkout.getWorkoutName(); // Keep the current name if blank
                break;
            }
        } while (!Validation.validateName(name));

        String description;
        do {
            description = Utils.getValue("Enter new Description (leave blank to keep current): ");
            if (description.isEmpty()) {
                description = existingWorkout.getDescription(); // Keep the current description if blank
                break;
            }
        } while (!Validation.validateDescription(description));

        int duration;
        do {
            String durationInput = Utils.getValue("Enter new Duration (in minutes) (leave blank to keep current): ");
            if (durationInput.isEmpty()) {
                duration = existingWorkout.getDuration(); // Keep the current duration if blank
                break;
            }
            duration = Integer.parseInt(durationInput);
        } while (!Validation.validateWorkoutDuration(duration));

        String type;
        do {
            type = Utils.getValue("Enter new Workout Type (cardio/strength/flexibility) (leave blank to keep current): ");
            if (type.isEmpty()) {
                type = existingWorkout.getType(); // Keep the current type if blank
                break;
            }
        } while (!Validation.validateWorkoutType(type));

        String intensity;
        do {
            intensity = Utils.getValue("Enter new Workout Intensity (low/medium/high) (leave blank to keep current): ");
            if (intensity.isEmpty()) {
                intensity = existingWorkout.getIntensity(); // Keep the current intensity if blank
                break;
            }
        } while (!Validation.validateWorkoutIntensity(intensity));

        Workout updatedWorkout = new Workout(id, name, description, duration, type, intensity);
        workoutService.update(updatedWorkout);
    }
}
