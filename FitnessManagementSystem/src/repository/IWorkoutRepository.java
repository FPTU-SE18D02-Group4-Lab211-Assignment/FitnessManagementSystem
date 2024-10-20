package repository;

import java.util.ArrayList;
import model.Workout;

public interface IWorkoutRepository extends Repository<Workout, ArrayList<Workout>> {
    final String workoutPath = "\\data\\workout.csv";
    
    @Override
    public ArrayList<Workout> readFile();
    
    @Override
    public void writeFile(ArrayList<Workout> workouts);
}
