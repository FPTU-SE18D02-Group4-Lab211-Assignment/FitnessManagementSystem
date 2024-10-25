package controller;

import model.Course;
import service.CoachService;
import service.CourseService;
import view.Menu;

public class FitnessManagement extends Menu<String> {
    static String[] menu = {"Courses Management", 
                            "Coaches Management", 
                            "Users Management", 
                            "Workout Management", 
                            "Exit"};
    
    private CourseService courseService = new CourseService();
    
    private Course course;
    
    public FitnessManagement() {      
        super("Fitness Management System", menu);
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                CourseManagement();
                break;
            case 2:
                CoachManagement();
                break;
            case 3:
                UserManagement();
                break;
            case 4:
                WorkoutManagement();
                break;
            case 5: 
                System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        FitnessManagement fit = new FitnessManagement();
        fit.run();
    }
    
    public void CourseManagement()  {       
        String[] menuOptions = {"Display list of courses", 
                               "Add new course", 
                               "Edit course",
                               "Return main menu"};
        Menu m = new Menu("Course Management", menuOptions) {
            @Override
            public void execute(int n) {              
                switch (n) {
                    case 1: {
                        courseService.display();
                           break;
                    }
                    case 2: {
                        courseService.add(course);
                    }     
                        break;
                    case 3: {
                        courseService.update(course);
                    }               
                }                
            }
        };
        m.run();
    }
    
    public void CoachManagement()  {    
        CoachService coaSrv = new CoachService();
        
        String[] menuOptions = {"Display list of coaches", 
                               "Add new coach", 
                               "Edit coach",
                               "Create new course",
                               "Return main menu"};
        Menu m = new Menu("Coach Management", menuOptions) {
            @Override
            public void execute(int n) {              
                switch (n) {
                    case 1:
                        coaSrv.display();
                        break;
                    case 2:              
                        coaSrv.add(null);
                        break;
                    case 3:
                        coaSrv.update(null);
                        break;  
                    case 4:
                        coaSrv.createCourse();
                        break;
                }                
            }
        };
        m.run();
    }
    
    public void UserManagement()  {       
        String[] menuOptions = {"Display list of users", 
                               "Add new user", 
                               "Edit user",
                               "Return main menu"};
        Menu m = new Menu("User Management", menuOptions) {
            @Override
            public void execute(int n) {              
                switch (n) {
                    case 1:
                        break;
                    case 2:              
                        break;
                    case 3:
                        break;                 
                }                
            }
        };
        m.run();
    }
    
    public void WorkoutManagement()  {       
        String[] menuOptions = {"Display list of workouts", 
                               "Add new workout", 
                               "Edit workout",
                               "Return main menu"};
        Menu m = new Menu("Course Management", menuOptions) {
            @Override
            public void execute(int n) {              
                switch (n) {
                    case 1:
                        break;
                    case 2:              
                        break;
                    case 3:
                        break;                 
                }                
            }
        };
        m.run();
    }
}
