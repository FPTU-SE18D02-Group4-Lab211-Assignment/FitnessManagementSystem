package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Course;
import model.User;
import service.CoachService;
import service.CourseService;
import service.UserService;
import view.CoachView;
import view.CourseView;
import view.ExerciseView;
import view.Menu;
import view.ScheduleView;
import view.UserView;
import view.WorkoutView;

public class FitnessManagement extends Menu<String> {

    CourseService courseSrv = new CourseService();
    UserService userService = new UserService();
    CoachService coaSrv = new CoachService();
    CoachView coachV = new CoachView();
    UserView userV = new UserView();
    CourseView courseV = new CourseView();
    WorkoutView workoutView = new WorkoutView();
    ExerciseView exerciseView = new ExerciseView();
    ScheduleView scheduleView = new ScheduleView();
    Course course = new Course();
    User user = new User();
//----------------------------------------------------
    static String[] menu = {"User",
        "Coach",
        "Admin",
        "Exit"};

    public FitnessManagement() {
        super("Fitness Management System", menu);
    }
//----------------------------------------------------

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                viewUser();
                break;
            case 2:
                viewCoach();
                break;
            case 3:
                viewAdmin();
                break;
            case 4:
                System.exit(0);
        }
    }
//----------------------------------------------------

    public static void main(String[] args) {
        FitnessManagement fit = new FitnessManagement();
        fit.run();
    }
//----------------------------------------------------

    public void viewUser() {
        String[] menu = {"View all Courses",
            "View all Exercises",
            "Register new course",
            "View Schedule",
            "Edit Schedule",
            "Complete Workouts",
            "View Notifications",
            "Exit"};
        Menu m = new Menu("User Management", menu) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1: {
                        courseSrv.display();
                        break;
                    }
                    case 2: {
                        exerciseView.displayAllExercises();
                        break;
                    }
                    case 3: {
                        userService.signInNewCourse();
                    }
                    break;
                    case 4: {
                        scheduleView.viewUserSchedule();
                        break;
                    }
                    case 5: {
                        try {
                            scheduleView.viewEditUserSchedule(); // Edit user schedule
                        } catch (IOException ex) {
                            Logger.getLogger(FitnessManagement.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                    case 6: {
                        scheduleView.viewToCompleteWorkouts();
                        break;
                    }
                    case 7: {
                        scheduleView.viewUpcomingWorkouts();
                        break;
                    }
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewCoach() {
        String[] menu = {"Add new Course",
            "Edit new Course",
            "Delete Course",
            "Add new Exercise",
            "Edit Exercise",
            "Delete Exercise",
            "Add new Workout (a set of exercises)",
            "Edit Workout",
            "Delete Workout",
            "View Users' Progress",
            "Exit"};
        Menu m = new Menu("Coach Management", menu) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1: {
                        courseSrv.add(course);
                        break;
                    }
                    case 2: {
                        courseSrv.update(course);
                        break;
                    }
                    case 3:
                        courseV.displayDeleteCourse();
                        break;
                    case 4:
                        exerciseView.displayAddExercise();
                        break;
                    case 5:
                        exerciseView.displayUpdateExercise();
                        break;
                    case 6:
                        exerciseView.displayDeleteExercise();
                        break;
                    case 7:
                        workoutView.displayAddWorkout();
                        break;
                    case 8:
                        workoutView.displayUpdateWorkout();
                        break;
                    case 9:
                        workoutView.displayDeleteWorkout();
                        break;
                    case 10:
                        scheduleView.viewUsersProgress();
                        break;
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewAdmin() {
        String[] menu = {"Courses Management",
            "Coaches Management",
            "Users Management",
            "Workout Management",
            "Exercise Management",
            "Exit"};
        Menu m = new Menu("Course Management", menu) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1: {
                        viewCourseManagement();
                        break;
                    }
                    case 2: {
                        viewCoachManagement();
                    }
                    break;
                    case 3: {
                        viewUserManagement();
                    }
                    case 4: {
                        viewWorkoutManagement();
                    }
                    case 5: {
                        viewExerciseManagement();
                    }
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewCourseManagement() {

        String[] menuOptions = {"Display list of courses",
            "Add new course",
            "Edit course",
            "Delete course",
            "Return main menu"};
        Menu m = new Menu("Course Management", menuOptions) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1: {
                        courseSrv.display();
                        break;
                    }
                    case 2: {
                        courseSrv.add(null);
                        break;
                    }
                    case 3: {
                        courseV.displayDeleteCourse();
                        break;
                    }
                    case 4: {
                        courseSrv.update(null);
                    }
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewCoachManagement() {

        String[] menuOptions = {"Display list of coaches",
            "Add new coach",
            "Delete a coach",
            "Edit coach",
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
                        coachV.displayDeleteCoach();
                        break;
                    case 4:
                        coaSrv.update(null);
                        break;
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewUserManagement() {

        String[] menuOptions = {"Display list of users",
            "Add new user",
            "Edit user",
            "Delete user",
            "Return main menu"};
        Menu m = new Menu("User Management", menuOptions) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1:
                        break;
                    case 2:
                        userService.addnewU(user);
                        break;
                    case 3:
                        userService.editUser();
                        break;
                    case 4:
                        userV.displayDeleteUser();
                        break;
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewWorkoutManagement() {

        String[] menuOptions = {"Display list of workouts",
            "Add new workout",
            "Edit workout",
            "Delete workout",
            "Return main menu"};
        Menu m = new Menu("Workout Management", menuOptions) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1:
                        workoutView.displayAllWorkouts();
                        break;
                    case 2:
                        workoutView.displayAddWorkout();
                        break;
                    case 3:
                        workoutView.displayDeleteWorkout();
                        break;
                    case 4:
                        workoutView.displayUpdateWorkout();
                        break;
                }
            }
        };
        m.run();
    }
//----------------------------------------------------

    public void viewExerciseManagement() {

        String[] menuOptions = {"Display list of exercises",
            "Add new exercise",
            "Edit exercise",
            "Delete exercise",
            "Return main menu"};
        Menu m = new Menu("Exercise Management", menuOptions) {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1:
                        exerciseView.displayAllExercises();
                        break;
                    case 2:
                        exerciseView.displayAddExercise();
                        break;
                    case 3:
                        exerciseView.displayDeleteExercise();
                        break;
                    case 4:
                        exerciseView.displayUpdateExercise();
                        break;
                }
            }
        };
        m.run();
    }
}
