package repository;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Schedule;

public class ScheduleRepository implements IScheduleRepository {

    private static final List<Schedule> schedules = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ScheduleRepository() {
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
//----------------------------------------------------

    @Override
    public ArrayList<Schedule> readFileWithUserCourseID(String userID, String courseID) {
        String fileName = generateFileName(userID, courseID);
        ArrayList<Schedule> courseSchedules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 6) {
                    Schedule schedule = parseSchedule(values);
                    courseSchedules.add(schedule);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return courseSchedules;
    }
//----------------------------------------------------

    @Override
    public ArrayList<Schedule> readFileWithUserID(String userID) {
        ArrayList<Schedule> userSchedules = new ArrayList<>();

        Path directory = Paths.get(path + schedulePath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, userID + "_*.csv")) {
            for (Path file : stream) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
                    String line;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values.length == 6 && values[0].equals(userID)) {
                            Schedule schedule = parseSchedule(values);
                            userSchedules.add(schedule);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file " + file + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error navigating files for user ID " + userID + ": " + e.getMessage());
        }
        return userSchedules;
    }
//----------------------------------------------------

    @Override
    public ArrayList<Schedule> readFile() {
        schedules.clear();

        Path directory = Paths.get(path + schedulePath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.csv")) {
            for (Path file : stream) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
                    String line;
                    br.readLine(); // Skip header line
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values.length == 6) { // Check for valid column count
                            Schedule schedule = parseSchedule(values);
                            schedules.add(schedule);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file " + file + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error accessing schedule directory: " + e.getMessage());
        }

        return new ArrayList<>(schedules);
    }
//----------------------------------------------------

    public void createFile(String userID, String courseID) {
        String fileName = this.generateFileName(userID, courseID);
        Path directory = Paths.get(path + schedulePath);

        // Ensure the directory exists
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("Error creating directory: " + e.getMessage());
                return; // Exit if directory creation fails
            }
        }

        Path filePath = directory.resolve(fileName); // Full path to the CSV file

        // Check if file already exists; if not, create it with a header row
        if (!Files.exists(filePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                // Write the header row
                writer.write("userID,workoutID,courseID,order,date,status");
                writer.newLine(); // Move to the next line
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }
//----------------------------------------------------

    // Hàm ghi dữ liệu một đối tượng Schedule mới vào file CSV
    @Override
    public void writeFile(Schedule schedule) {
        String fileName = schedule.generateFileName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + schedulePath + fileName, true))) {
            writer.write(String.format("%s,%s,%s,%d,%s,%s\n",
                    schedule.getUserID(),
                    schedule.getWorkoutID(),
                    schedule.getCourseID(),
                    schedule.getOrder(),
                    (schedule.getDate() != null ? schedule.getDate().format(formatter) : "Unknown Date"),
                    schedule.isStatus() ? "Completed" : "Not Completed"
            ));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

//----------------------------------------------------
    public void replaceFile(Schedule schedule) {
        String fileName = schedule.generateFileName();
        Path filePath = Paths.get(path + schedulePath + fileName);

        // Read existing lines from the file
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            return;
        }

        // Replace the line corresponding to the workout ID
        for (int i = 1; i < lines.size(); i++) { // Start from 1 to skip the header
            String[] values = lines.get(i).split(",");
            if (values.length >= 2 && values[1].equals(schedule.getWorkoutID())) {
                lines.set(i, String.format("%s,%s,%s,%d,%s,%s",
                        schedule.getUserID(),
                        schedule.getWorkoutID(),
                        schedule.getCourseID(),
                        schedule.getOrder(),
                        (schedule.getDate() != null ? schedule.getDate().format(formatter) : "Unknown Date"),
                        schedule.isStatus() ? "Completed" : "Not Completed"
                ));
                break;
            }
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

//----------------------------------------------------
    // Phương thức tiện ích để tạo đối tượng Schedule từ dữ liệu trong mảng chuỗi
    private Schedule parseSchedule(String[] values) {
        String userID = values[0];
        String workoutID = values[1];
        String courseID = values[2];
        int order = Integer.parseInt(values[3]);
        String date = values[4];
        boolean status = Boolean.parseBoolean(values[5]);
        return new Schedule(userID, workoutID, courseID, order, date, status);
    }
//----------------------------------------------------

    private String generateFileName(String userID, String courseID) {
        return path + schedulePath + String.format("%s_%s.csv", userID, courseID);
    }

//----------------------------------------------------
    @Override
    public void writeFile(ArrayList<Schedule> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
