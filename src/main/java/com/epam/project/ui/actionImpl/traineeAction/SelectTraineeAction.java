package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainee;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class SelectTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getCommandCode() {
        return "4";
    }

    @Override
    public String getDescription() {
        return "Select Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Selecting a trainee profile...");

        System.out.println("Please enter the trainee's username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter the trainee's password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }


        try {
            Trainee trainee = facade.selectTraineeProfile(username);
            System.out.println("Trainee profile found: " + trainee);
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}