package com.epam.project.ui.actionImpl.trainingAction;

import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static com.epam.project.ui.actionImpl.trainerAction.CreateTrainerAction.getTrainingType;

@Component
public class CreateTrainingAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public CreateTrainingAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "8";
    }

    @Override
    public String getDescription() {
        return "Create a new training session";
    }

    @Override
    public void execute() {
        System.out.println("Creating a new training session...");
        System.out.println("Please enter the trainer's ID: ");
        Long trainerId = Long.parseLong(scanner.nextLine());
        Trainer trainer = facade.selectTrainerProfile(trainerId);
        if (trainer == null) {
            System.out.println("Trainer with ID " + trainerId + " not found. Training creation cancelled.");
            return;
        }

        System.out.println("Please enter the trainee's ID: ");
        Long traineeId = Long.parseLong(scanner.nextLine());
        Trainee trainee = facade.selectTraineeProfile(traineeId);
        if (trainee == null) {
            System.out.println("Trainee with ID " + traineeId + " not found. Training creation cancelled.");
            return;
        }

        System.out.println("Please enter the training name: ");
        String trainingName = scanner.nextLine();

        TrainingType specialization = getTrainingType(scanner);
        if (specialization == null) {
            System.out.println("Invalid choice. Training creation cancelled.");
            return;
        }

        System.out.println("Please enter the training date (yyyy-mm-dd): ");
        Date trainingDate;
        try {
            trainingDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd. Training creation cancelled.");
            return;
        }

        System.out.println("Please enter the training duration in minutes: ");
        int trainingDuration;
        try {
            trainingDuration = Integer.parseInt(scanner.nextLine());
            if (trainingDuration < 0) {
                throw new NumberFormatException("Duration cannot be negative.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration format. Please enter a number. Training creation cancelled.");
            return;
        }
        Training training = facade.createTraining(trainerId, traineeId, trainingName, specialization, trainingDate, trainingDuration);
        System.out.println("Training session created successfully! \n" +
                "Details: " + training);

    }

}
