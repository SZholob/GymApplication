package com.epam.project.ui.actionImpl.trainingAction;

import com.epam.project.model.Training;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CreateTrainingAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "12"; }
    @Override public String getDescription() { return "Create Training"; }

    @Override
    public void execute() {
        System.out.println("--- Create New Training ---");

        System.out.print("Enter your username (authorization): ");
        String authUsername = scanner.nextLine();
        System.out.print("Enter your password: ");
        if (!facade.authenticate(authUsername, scanner.nextLine())) {
            System.out.println("Authentication failed. Access denied.");
            return;
        }

        System.out.print("Enter Trainee Username: ");
        String traineeUsername = scanner.nextLine();
        System.out.print("Enter Trainer Username: ");
        String trainerUsername = scanner.nextLine();
        System.out.print("Enter Training Name: ");
        String trainingName = scanner.nextLine();
        System.out.print("Enter Training Date (yyyy-mm-dd): ");
        LocalDate trainingDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Training Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());

        Training training = facade.createTraining(traineeUsername, trainerUsername, trainingName, trainingDate, duration);
        System.out.println("Training created successfully! ID: " + training.getId());
    }
}