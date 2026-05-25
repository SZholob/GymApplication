package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class UpdateTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getCommandCode() {
        return "6";
    }

    @Override
    public String getDescription() {
        return "Update Trainer Profile";
    }

    @Override
    public void execute() {
        System.out.println("Updating a trainer profile...");

        System.out.println("Please enter the trainer's username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter the trainer's password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        Trainer trainer;
        try {
            trainer = facade.selectTrainerProfile(username);
        } catch (Exception e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
            return;
        }

        System.out.println("Current trainer details: " + trainer);

        System.out.println("Please enter the trainer's new first name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            trainer.getUser().setFirstName(firstName);
        }

        System.out.println("Please enter the trainer's new last name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            trainer.getUser().setLastName(lastName);
        }

        Trainer updatedTrainer;
        try {
            updatedTrainer = facade.updateTrainerProfile(trainer);
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
            return;
        }
        System.out.println("Trainer profile updated successfully! Updated details: " + updatedTrainer);
    }
}