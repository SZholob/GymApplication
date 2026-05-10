package com.epam.project.ui.actionImpl.trainingAction;

import com.epam.project.model.Training;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GetTrainerTrainingsAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "11"; }
    @Override public String getDescription() { return "Get Trainer Trainings List (with filters)"; }

    @Override
    public void execute() {
        System.out.print("Enter Trainer Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        if (!facade.authenticate(username, scanner.nextLine())) {
            System.out.println("Auth failed."); return;
        }

        System.out.println("--- Filters (Press Enter to skip) ---");
        LocalDate from;
        LocalDate to;

        try {
            System.out.print("From Date (yyyy-mm-dd): ");
            String fromStr = scanner.nextLine();
            from = fromStr.isEmpty() ? null : LocalDate.parse(fromStr);

            System.out.print("To Date (yyyy-mm-dd): ");
            String toStr = scanner.nextLine();
            to = toStr.isEmpty() ? null : LocalDate.parse(toStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Returning to main menu.");
            return;
        }

        System.out.print("Trainee Username: ");
        String traineeUsername = scanner.nextLine();
        if (traineeUsername.isEmpty()) traineeUsername = null;

        List<Training> trainings = facade.getTrainerTrainings(username, from, to, traineeUsername);

        if (trainings == null || trainings.isEmpty()) {
            System.out.println("No trainings found matching the criteria.");
        } else {
            trainings.forEach(t -> System.out.println(
                    "Training: " + t.getTrainingName() +
                            " | Date: " + t.getTrainingDate() +
                            " | Trainee: " + t.getTrainee().getUser().getUsername() +
                            " | Trainer: " + t.getTrainer().getUser().getUsername()
            ));
        }
    }
}