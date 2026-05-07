package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GetUnassignedTrainersAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "16"; }
    @Override public String getDescription() { return "Get Active Trainers Not Assigned to Trainee"; }

    @Override
    public void execute() {
        System.out.print("Enter Trainee Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        if (!facade.authenticate(username, scanner.nextLine())) {
            System.out.println("Auth failed."); return;
        }

        List<Trainer> trainers = facade.getUnassignedActiveTrainers(username);
        System.out.println("--- Available Active Trainers ---");
        if (trainers.isEmpty()) {
            System.out.println("No available trainers found.");
        } else {
            trainers.forEach(t -> System.out.println("- " + t.getUser().getUsername() + " (" + t.getSpecialization().getTrainingTypeName() + ")"));
        }
    }
}