package view;

import model.Coach;
import service.CoachService;
import utils.Validation;

public class CoachView {

    private final CoachService coachSrv = new CoachService();

    public void displayDeleteCoach() {
        try {
            Coach coach = Validation.validateAndFindCoach(coachSrv);
            System.out.println(coach);

            String confirmation = Validation.getValue("Are you sure you want to delete this coach? (y/n): ").trim().toLowerCase();

            if (confirmation.equals("y")) {
                coachSrv.delete(coach);
            } else {
                System.out.println("Deletion canceled.");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
