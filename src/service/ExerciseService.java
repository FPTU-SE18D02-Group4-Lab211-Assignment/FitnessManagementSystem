package service;

import java.util.List;
import model.Exercise;
import repository.ExerciseRepository;

public class ExerciseService implements IExerciseService {

    private final ExerciseRepository exerciseRepository = new ExerciseRepository();

    public ExerciseService() {
    }

    //----------------------------------------------------
    public String generateId() {
        List<Exercise> exercises = exerciseRepository.getExerciseList();
        if (exercises.isEmpty()) {
            return "EXE-0001";
        }

        // Sort exercises by ID and retrieve the last (highest) ID
        exercises.sort((e1, e2) -> e2.getId().compareTo(e1.getId()));
        String lastExerciseId = exercises.get(0).getId();

        // Extract the numeric part of the ID and increment it
        int lastNumber = Integer.parseInt(lastExerciseId.substring(4));
        return String.format("EXE-%04d", lastNumber + 1);
    }

//----------------------------------------------------
    @Override
    public Exercise findById(String id) {
        String trimmedId = id.trim();
        for (Exercise exercise : exerciseRepository.getExerciseList()) {
            if (exercise.getId().equals(trimmedId)) {
                return exercise;
            }
        }
        return null;
    }
//----------------------------------------------------

    @Override
    public void display() {
        if (exerciseRepository.getExerciseList().isEmpty()) {
            System.out.println("No exercises available.");
        } else {
            for (Exercise exercise : exerciseRepository.getExerciseList()) {
                System.out.println(exercise);
            }
        }
    }
//----------------------------------------------------

    @Override
    public void add(Exercise newExercise) {
        if (findById(newExercise.getId()) == null) {
            exerciseRepository.getExerciseList().add(newExercise);
            System.out.println("Exercise added successfully.");
        } else {
            System.out.println("Exercise with ID " + newExercise.getId() + " already exists.");
        }
    }
//----------------------------------------------------

    @Override
    public void delete(Exercise exercise) {
        if (exercise == null) {
            System.err.println("Error: Exercise not found.");
            return;
        }

        if (exerciseRepository.getExerciseList().remove(exercise)) {
            System.out.println("Exercise with ID " + exercise.getId() + " has been successfully deleted.");
            save();
        } else {
            System.err.println("Error: Failed to delete exercise with ID " + exercise.getId());
        }
    }

//----------------------------------------------------
    @Override
    public void update(Exercise updatedExercise) {
        Exercise existingExercise = findById(updatedExercise.getId());
        if (existingExercise != null) {
            existingExercise.setName(updatedExercise.getName());
            existingExercise.setDetail(updatedExercise.getDetail());
            existingExercise.setDuration(updatedExercise.getDuration());
            System.out.println("Exercise updated successfully.");
        } else {
            System.out.println("Exercise with ID " + updatedExercise.getId() + " not found.");
        }
    }
//----------------------------------------------------

    @Override
    public void save() {
        exerciseRepository.writeFile(exerciseRepository.getExerciseList());
        System.out.println("Exercises saved to file.");
    }
}
