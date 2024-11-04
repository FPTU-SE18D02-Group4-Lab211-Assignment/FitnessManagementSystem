package view;

import model.User;
import service.UserService;
import utils.Utils;

public class UserView {

    UserService UserSrv = new UserService();

    public void displayDeleteUser() { // This method is focused on deleting a user
        System.out.println("===== Delete User =====");

        // Prompt for the user ID
        String userId = Utils.getValue("Enter the User ID to delete: ");

        // Find the user by ID
        User userToDelete = UserSrv.findById(userId); // Assuming this method exists in UserService
        if (userToDelete != null) {
            // Display the user details to the user
            System.out.println("User found: " + userToDelete);

            // Confirm before deletion
            String confirmation = Utils.getValue("Are you sure you want to delete this user? (y/n): ");
            if (confirmation.equalsIgnoreCase("y")) {
                // Delete the user using the service
                UserSrv.delete(userToDelete);
                System.out.println("User with ID " + userId + " has been successfully deleted.");
            } else {
                System.out.println("Deletion canceled.");
            }
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }
}
