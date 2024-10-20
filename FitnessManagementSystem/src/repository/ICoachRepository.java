package repository;

import java.util.ArrayList;
import model.Coach;

public interface ICoachRepository extends Repository<Coach, ArrayList<Coach>> {
    final String coachPath = "\\data\\coach.csv";
    
    @Override
    public ArrayList<Coach> readFile();
    
    @Override
    public void writeFile(ArrayList<Coach> coaches);
}
