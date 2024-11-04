package view;

import model.Coach;
import service.CoachService;
import utils.Validation;

public class CoachView {

    private final CoachService coachService = new CoachService();

    public void displayDeleteCoach() {
        try {
            // Prompt the user to enter the Coach ID they wish to delete
            String coachId = Validation.checkString("Enter the Coach ID to delete: ",
                    "ID must be in the format COA-XXXX", "^COA-[0-9]{4}");
            // Find the Coach by ID
            Coach coach = coachService.findById(coachId);
            System.out.println(coach);

            if (coach != null) {
                // Confirm with the user before deleting
                String confirmation = Validation.getValue("Are you sure you want to delete this coach? (y/n): ").trim().toLowerCase();

                if (confirmation.equals("y")) {
                    // Call delete method on CoachService to remove the coach
                    coachService.delete(coach);
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Coach with ID " + coachId + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
