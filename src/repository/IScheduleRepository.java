package repository;

import java.util.ArrayList;
import model.Schedule;

public interface IScheduleRepository extends Repository<Schedule, ArrayList<Schedule>>{
    
    final String schedulePath = "\\data\\schedule";
    
    public ArrayList<Schedule> readFileWithUserCourseID(String userID, String courseID);
    
    public ArrayList<Schedule> readFileWithUserID(String userID);
    
    public void writeFileWithUserCourseID(Schedule schedule);
}
