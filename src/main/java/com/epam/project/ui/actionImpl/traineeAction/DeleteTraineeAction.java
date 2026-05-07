package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class DeleteTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getCommandCode() {
        return "3";
    }

    @Override
    public String getDescription() {
        return "Delete Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Deleting a trainee profile...");


        System.out.println("Please enter the trainee's username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter the trainee's password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        System.out.println("Are you sure you want to delete the trainee profile with username '" + username + "'? (Y/N)");
        String confirmation = scanner.nextLine();

        while (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("N")) {
            System.out.println("Invalid input. Please enter 'Y' to confirm deletion or 'N' to cancel.");
            confirmation = scanner.nextLine();
        }

        if (confirmation.equalsIgnoreCase("Y")) {
            try {
                facade.deleteTraineeProfile(username);
                System.out.println("Trainee profile deleted successfully!");
            } catch (Exception e) {
                System.out.println("Error deleting profile: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}