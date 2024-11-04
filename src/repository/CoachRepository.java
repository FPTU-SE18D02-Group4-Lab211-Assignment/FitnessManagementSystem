package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Coach;

public final class CoachRepository implements ICoachRepository {

    private static ArrayList<Coach> coachList = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        coachList = new CoachRepository().readFile();
    }
//----------------------------------------------------

    public ArrayList<Coach> getCouchList() {
        return coachList;
    }
//----------------------------------------------------

    @Override
    public ArrayList<Coach> readFile() {
        ArrayList<Coach> coachListRead = new ArrayList<>();

        String line;
        try {
            BufferedReader input = new BufferedReader(new FileReader(path + coachPath));
            input.readLine();
            while ((line = input.readLine()) != null) {
                String[] tokString = line.split(",");
                boolean gender = tokString[3].equals("Male");
                Coach coach = new Coach(tokString[0], tokString[1], tokString[2], gender, tokString[4], tokString[5]);
                coachListRead.add(coach);
            }
            return coachListRead;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
//----------------------------------------------------

    @Override
    public void writeFile(ArrayList<Coach> coaches) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + coachPath, true))) {
            for (Coach coach : coaches) {
                String line = coach.getId() + ","
                        + coach.getName() + ","
                        + coach.getBirthDate().format(formatter) + ","
                        + (coach.isGender() ? "Male" : "Female") + ","
                        + coach.getPhoneNumber() + ","
                        + coach.getEmail() + ",";
                output.write(line);
                output.newLine();
            }
        } catch (IOException e) {
        }
    }
}
