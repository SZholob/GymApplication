package com.epam.project.ui.actionImpl.userAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ChangeUserPasswordAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "8"; }
    @Override public String getDescription() { return "Change Password (Trainee/Trainer)"; }

    @Override
    public void execute() {
        System.out.println("--- Change User Password ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your current password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        System.out.print("Enter your NEW password: ");
        String newPassword = scanner.nextLine();
        try {
            facade.changeUserPassword(username, newPassword);
        } catch (Exception e) {
            System.out.println("Error updating password: " + e.getMessage());
            return;
        }

        System.out.println("Password updated successfully!");
    }
}