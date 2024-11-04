package view;

import model.User;
import service.UserService;
import utils.Utils;

public class UserView {

    UserService UserSrv = new UserService();

    public void displayDeleteUser() {
        System.out.println("===== Delete User =====");

        String userId = Utils.getValue("Enter the User ID to delete: ");

        User userToDelete = UserSrv.findById(userId);
        if (userToDelete != null) {
            System.out.println("User found: " + userToDelete);

            String confirmation = Utils.getValue("Are you sure you want to delete this user? (y/n): ");
            if (confirmation.equalsIgnoreCase("y")) {
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
