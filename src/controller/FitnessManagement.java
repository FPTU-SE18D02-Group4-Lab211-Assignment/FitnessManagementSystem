package controller;

import service.CoachService;
import view.Menu;

public class FitnessManagement extends Menu<String> {
    static String[] menu = {"Courses Management", 
                            "Couches Management", 
                            "Users Management", 
                            "Workout Management", 
                            "Exit"};
    
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
                CouchManagement();
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
    
    public void CouchManagement()  {    
        CoachService coaSrv = new CoachService();
        
        String[] menuOptions = {"Display list of couches", 
                               "Add new couch", 
                               "Edit couch",
                               "Create new course",
                               "Return main menu"};
        Menu m = new Menu("Couch Management", menuOptions) {
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
