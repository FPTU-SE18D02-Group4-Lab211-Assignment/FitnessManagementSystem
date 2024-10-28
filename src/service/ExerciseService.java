package service;

import model.Exercise;
import repository.ExerciseRepository;

import java.util.List;

public class ExerciseService implements IExerciseService {

    private final ExerciseRepository exerciseRepository = new ExerciseRepository();

    public ExerciseService() {
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
        List<Exercise> exercises = exerciseRepository.getExerciseList();
        if (exercises.isEmpty()) {
            System.out.println("No exercises available.");
        } else {
            for (Exercise exercise : exercises) {
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
    public void delete(Exercise exerciseToDelete) {
        if (exerciseRepository.getExerciseList().remove(exerciseToDelete)) {
            System.out.println("Exercise removed successfully.");
        } else {
            System.out.println("Exercise with ID " + exerciseToDelete.getId() + " not found.");
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
