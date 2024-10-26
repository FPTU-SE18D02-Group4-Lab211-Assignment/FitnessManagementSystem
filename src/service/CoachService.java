package service;

import java.util.ArrayList;
import model.Coach;
import repository.CoachRepository;
import utils.Utils;
import utils.Validation;

public class CoachService implements ICoachService {
    
    private static final CoachRepository coaRepo = new CoachRepository();
    
    public void createCourse() {
        try {
            Coach coaCre = findById(Validation.checkString("Enter coach ID to create course: ", "ID must be COA-XXXX format", "^COA-[0-9]{4}"));
            
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

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

    @Override
    public void display() {
         for (Coach coa : coaRepo.getCouchList()) {
            System.out.println(coa);
        }
    }

    @Override
    public void add(Coach coa) {
        try {
            String id = Validation.checkString("Enter coach ID: ", "ID must be COA-XXXX format", "^COA-[0-9]{4}");
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

    @Override
    public void update(Coach e) {
        try {
            String id = Validation.checkString("Enter coach ID: ", "ID must be COA-XXXX format", "^COA-[0-9]{4}");
            Coach coa = findById(id);

            String attribute = Validation.getValue("Enter attribute to update (name, dob, gender, phoneNumber, email): ");
            switch (attribute.toLowerCase()) {
            case "name":
                coa.setName(Validation.checkName("Enter coach name: ", "Each word in name must have its first letter capitalized"));
                break;
            case "dob":
                coa.setBirthDate(Validation.checkDob("Enter coach date of birth: ", "This person need to be older than 18"));
                break;
            case "gender":
                coa.setGender(Validation.convertStringToGender(Validation.getValue("Male or Female: ")));
                break;
            case "phonenumber":
                coa.setPhoneNumber(Validation.checkString("Enter new phone number: ", "Invalid phone number format. Expected 10 digits.", "^0\\d{9}$"));
                break;
            case "email":
                coa.setEmail(Validation.checkString("Enter coach email: ", "Invalid email. Must be in the format <username>@<domain>.<extension>", "\\w+@\\w+\\.\\w+"));
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
