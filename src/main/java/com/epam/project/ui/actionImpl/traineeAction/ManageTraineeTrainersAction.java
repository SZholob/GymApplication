package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ManageTraineeTrainersAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "11"; }
    @Override public String getDescription() { return "Get Unassigned Trainers & Update Trainers List"; }

    @Override
    public void execute() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        if (!facade.authenticate(username, scanner.nextLine())) {
            System.out.println("Auth failed."); return;
        }

        List<Trainer> unassigned = facade.getUnassignedActiveTrainers(username);
        System.out.println("--- Unassigned Active Trainers ---");
        if (unassigned.isEmpty()) {
            System.out.println("No unassigned active trainers available.");
            return;
        }
        unassigned.forEach(t -> System.out.println("- " + t.getUser().getUsername() + " (" + t.getSpecialization().getTrainingTypeName() + ")"));

        System.out.print("\nEnter comma-separated usernames of trainers you want to add (or press Enter to skip): ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            List<String> trainerUsernames = Arrays.stream(input.split(",")).map(String::trim).toList();
            facade.updateTraineeTrainers(username, trainerUsernames);
            System.out.println("Trainers list updated successfully!");
        }
    }
}