package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Exercise;

public final class ExerciseRepository implements IExerciseRepository {
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    
    public ExerciseRepository() {
        exerciseList = readFile();
    }
    
    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }
     
    @Override
    public ArrayList<Exercise> readFile() {
        ArrayList<Exercise >exerciseListRead = new ArrayList<>();
 
        String line;
        try {
            BufferedReader input = new BufferedReader(new FileReader(path + exercisePath));
            while ((line = input.readLine()) != null) {
                String[] tokString = line.split(",");
                Exercise exercise = new Exercise(tokString[0], tokString[1], tokString[2], Integer.parseInt(tokString[3]));
                exerciseListRead.add(exercise);
            }
            return exerciseListRead;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    @Override
    public void writeFile(ArrayList<Exercise> exercises){
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + exercisePath, true))) {
            for (Exercise exercise : exercises) {
                String line = exercise.getId() + "," +
                        exercise.getName() + "," +
                        exercise.getDetail() + "," +
                        exercise.getDuration();
                output.write(line);
                output.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
