package view;

import model.User;
import service.UserService;
import utils.Utils;
import utils.Validation;

public class UserView {
    
    UserService UserSrv = new UserService();
    
    public void displayDeleteUser() {
        System.out.println("===== Delete User =====");
        
        User userToDelete = Validation.validateAndFindUser(UserSrv);
        String userId = userToDelete.getId();

        // Confirm deletion
        System.out.println("User found: " + userToDelete);
        String confirmation = Utils.getValue("Are you sure you want to delete this user? (y/n): ");
        if (confirmation.equalsIgnoreCase("y")) {
            UserSrv.delete(userToDelete);
            System.out.println("User with ID " + userId + " has been successfully deleted.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }
}
