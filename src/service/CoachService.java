package service;

import model.Coach;
import repository.CoachRepository;
import utils.Utils;
import utils.Validation;

public class CoachService implements ICoachService {

    private static final CoachRepository coaRepo = new CoachRepository();

//----------------------------------------------------
    private String generateCoachID() {
        int maxId = 0;
        for (Coach coach : coaRepo.getCouchList()) {
            String[] parts = coach.getId().split("-");
            if (parts.length == 2 && parts[0].equals("COA")) {
                try {
                    int idNum = Integer.parseInt(parts[1]);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid course ID format: " + coach.getId());
                }
            }
        }
        return "COA-" + String.format("%04d", maxId + 1);
    }

//----------------------------------------------------
    @Override
    public Coach findById(String id) throws Exception {
        try {
            for (Coach coa : coaRepo.getCouchList()) {
                if (coa.getId().equals(id)) {
                    return coa;
                }
            }
        } catch (Exception e) {
            throw new Exception("Coach not found.");
        }
        return null;
    }

    public static void main(String[] args) {

    }
//----------------------------------------------------

    @Override
    public void display() {
        for (Coach coa : coaRepo.getCouchList()) {
            System.out.println(coa);
        }
    }
//----------------------------------------------------

    @Override
    public void add(Coach coa) {
        try {
            String id = generateCoachID();
            System.out.println("New Coach ID: " + id);
            String name = Validation.checkName("Enter coach name: ", "Each word in name must have its first letter capitalized");
            String dob = Validation.checkDob("Enter coach date of birth: ", "This person need to be older than 18");
            boolean gender = Validation.convertStringToGender(Utils.getValue("Male or Female: "));
            String phone = Validation.checkString("Enter coach phone number: ", "Invalid phone number format. Expected 10 digits.", "^0\\d{9}$");
            String email = Validation.checkString("Enter coach email: ", "Invalid email. Must be in the format <username>@<domain>.<extension>", "\\w+@\\w+\\.\\w+");
            coa = new Coach(id, name, dob, gender, phone, email);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
        coaRepo.getCouchList().add(coa);
        save();
    }

//----------------------------------------------------
    @Override
    public void delete(Coach coach) {
        if (coach == null) {
            System.err.println("Error: Coach not found.");
            return;
        }

        if (coaRepo.getCouchList().remove(coach)) {
            System.out.println("Coach with ID " + coach.getId() + " has been deleted.");
            save();
        } else {
            System.err.println("Error: Failed to delete coach with ID " + coach.getId());
        }
    }

//----------------------------------------------------
    @Override
    public void update(Coach e) {
        CoachService coachSrv = new CoachService();
        try {
            Coach coach = Validation.validateAndFindCoach(coachSrv);
            String id = coach.getId();

            String attribute = Validation.getValue("Enter attribute to update (name, dob, gender, phoneNumber, email): ");
            switch (attribute.toLowerCase()) {
                case "name":
                    coach.setName(Validation.checkName("Enter coach name: ", "Each word in name must have its first letter capitalized"));
                    break;
                case "dob":
                    coach.setBirthDate(Validation.checkDob("Enter coach date of birth: ", "This person need to be older than 18"));
                    break;
                case "gender":
                    coach.setGender(Validation.convertStringToGender(Validation.getValue("Male or Female: ")));
                    break;
                case "phonenumber":
                    coach.setPhoneNumber(Validation.checkString("Enter new phone number: ", "Invalid phone number format. Expected 10 digits.", "^0\\d{9}$"));
                    break;
                case "email":
                    coach.setEmail(Validation.checkString("Enter coach email: ", "Invalid email. Must be in the format <username>@<domain>.<extension>", "\\w+@\\w+\\.\\w+"));
                    break;
                default:
                    System.out.println("Invalid attribute.");
                    return;
            }
            save();
        } catch (Exception ex) {
            System.err.println("Error updating employee: " + ex.getMessage());
        }
    }

//----------------------------------------------------
    @Override
    public void save() {
        try {
            coaRepo.writeFile(coaRepo.getCouchList());
            System.out.println("Coach data saved.");
        } catch (Exception e) {
            System.err.println("Error saving coach data: " + e.getMessage());
        }
    }
}
