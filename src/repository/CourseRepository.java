package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Coach;
import model.Course;
import model.Workout;

public final class CourseRepository implements ICourseRepository {

    private static final CoachRepository coaRepo = new CoachRepository();
    private final ArrayList<Course> courseList = new ArrayList<>();

    public CourseRepository() {
        readFile();
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    @Override
    public ArrayList<Course> readFile() {
        ArrayList<Course> courseList = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(path + coursePath))) {
            String line;

            while ((line = input.readLine()) != null) {
                String[] tokString = line.split("\\|");

                if (tokString.length == 7) { 
                    try {

                        List<Workout> listOfWorkout = new ArrayList<>();
                        String[] workoutIDs = tokString[6].substring(1, tokString[6].length() - 1).split(","); // Removing brackets and splitting

                        for (String workoutID : workoutIDs) {
                            listOfWorkout.add(new Workout(workoutID.trim(), "", new ArrayList<>())); // Initialize with empty details
                        }

                        Coach coach = null;
                        for (Coach c : coaRepo.getCouchList()) {
                            if (c.getId().equals(tokString[5].trim())) { 
                                coach = c;
                                break;
                            }
                        }

                        if (coach == null) {
                            System.err.println("Coach ID not found: " + tokString[5]);
                            continue; 
                        }

                        Course course = new Course(
                                tokString[0].trim(),
                                tokString[1].trim(), 
                                tokString[2].trim(), 
                                Integer.parseInt(tokString[3].trim()), 
                                Double.parseDouble(tokString[4].trim()), 
                                coach, 
                                listOfWorkout 
                        );

                        courseList.add(course);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing data for course: " + tokString[0]);
                    }
                } else {
                    System.err.println("Malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return courseList;
    }

    @Override
    public void writeFile(ArrayList<Course> courses) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + coursePath))) {
            for (Course course : courses) {
                StringBuilder workoutIDs = new StringBuilder("[");
                for (Workout workout : course.getListOfWorkout()) {
                    workoutIDs.append(workout.getId()).append(", ");
                }

                if (workoutIDs.length() > 1) {
                    workoutIDs.setLength(workoutIDs.length() - 2);
                }
                workoutIDs.append("]");

                output.write(
                        course.getCourseID() + "|"
                        + course.getCourseName() + "|"
                        + course.getCourseDescription() + "|"
                        + course.getCourseDuration() + "|"
                        + course.getCoursePrice() + "|"
                        + course.getCoachID().getId() + "|"
                        + workoutIDs.toString()
                );
                output.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

}
