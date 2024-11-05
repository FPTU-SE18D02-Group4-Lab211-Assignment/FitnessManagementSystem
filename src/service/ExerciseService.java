package service;

import model.Exercise;
import repository.ExerciseRepository;

public class ExerciseService implements IExerciseService {

    private final ExerciseRepository exerciseRepository = new ExerciseRepository();

    public ExerciseService() {
    }

    //----------------------------------------------------
    public String generateExerciseID() {
        int maxId = 0;
        for (Exercise exercise : exerciseRepository.getExerciseList()) {
            String[] parts = exercise.getId().split("-");
            if (parts.length == 2 && parts[0].equals("EXE")) {
                try {
                    int idNum = Integer.parseInt(parts[1]);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid exercise ID format: " + exercise.getId());
                }
            }
        }
        return "EXE-" + String.format("%04d", maxId + 1);
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
            save();
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
            save();
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
