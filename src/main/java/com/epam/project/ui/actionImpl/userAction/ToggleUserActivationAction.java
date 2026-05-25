package com.epam.project.ui.actionImpl.userAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ToggleUserActivationAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "9"; }
    @Override public String getDescription() { return "Toggle Profile Activation Status (Trainee/Trainer)"; }

    @Override
    public void execute() {
        System.out.println("--- Toggle Profile Activation ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        facade.toggleUserActivation(username);
        System.out.println("Profile activation status changed successfully!");
    }
}