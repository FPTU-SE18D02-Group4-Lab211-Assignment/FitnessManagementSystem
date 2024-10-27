package repository;

import java.util.ArrayList;
import model.Exercise;

public interface IExerciseRepository extends Repository<Exercise, ArrayList<Exercise>>{
    final String exercisePath = "\\data\\exercise.csv";
    
    @Override
    public ArrayList<Exercise> readFile();
    
    @Override
    public void writeFile(ArrayList<Exercise> exercises);
}
