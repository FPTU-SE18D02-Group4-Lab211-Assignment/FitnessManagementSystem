package controller;

import model.Course;
import model.User;
import service.CoachService;
import service.CourseService;
import service.UserService;
import view.ExerciseView;
import view.Menu;
import view.ScheduleView;
import view.WorkoutView;

public class FitnessManagement extends Menu<String> {

    CourseService courseSrv = new CourseService();
    UserService userService = new UserService();
    CoachService coaSrv = new CoachService();
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
                        userService.signInNewCourse();
                    }
                    break;
                    case 3: {
                        scheduleView.viewUserSchedule();
                        break;
                    }
                    case 4: {
                        scheduleView.viewEditUserSchedule(); // Edit user schedule
                        break;
                    }
                    case 5: {
//                        scheduleView.editUserSchedule(); // Edit user schedule
                        break;
                    }
                    case 6: {
                        scheduleView.viewUpcomingWorkouts();// Edit user schedule
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
                    }
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
                    }
                    break;
                    case 3: {
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
//----------------------------------------------------

    public void viewUserManagement() {

        String[] menuOptions = {"Display list of users",
            "Add new user",
            "Edit user",
            "Sign in new course",
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
                        exerciseView.displayUpdateExercise();
                        break;
                }
            }
        };
        m.run();
    }
}
