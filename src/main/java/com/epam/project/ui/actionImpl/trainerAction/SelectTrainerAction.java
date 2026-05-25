package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class SelectTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getCommandCode() {
        return "7";
    }

    @Override
    public String getDescription() {
        return "Select Trainer Profile";
    }

    @Override
    public void execute() {
        System.out.println("Selecting a trainer profile...");

        System.out.println("Please enter the trainer's username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter the trainer's password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        try {
            Trainer trainer = facade.selectTrainerProfile(username);
            System.out.println("Trainer profile found: " + trainer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}