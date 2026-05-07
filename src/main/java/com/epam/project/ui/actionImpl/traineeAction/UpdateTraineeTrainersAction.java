package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class UpdateTraineeTrainersAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "17"; }
    @Override public String getDescription() { return "Update Trainee's Trainers List"; }

    @Override
    public void execute() {
        System.out.print("Enter Trainee Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        if (!facade.authenticate(username, scanner.nextLine())) {
            System.out.println("Auth failed."); return;
        }

        System.out.print("Enter comma-separated usernames of trainers: ");
        String input = scanner.nextLine();
        List<String> trainerUsernames = Arrays.stream(input.split(","))
                .map(String::trim)
                .toList();

        facade.updateTraineeTrainers(username, trainerUsernames);
        System.out.println("Trainers list updated successfully!");
    }
}