package com.epam.project.ui.actionImpl.trainingAction;

import com.epam.project.model.Training;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GetTrainerTrainingsAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "15"; }
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
        System.out.print("From Date (yyyy-mm-dd): ");
        String fromStr = scanner.nextLine();
        LocalDate from = fromStr.isEmpty() ? null : LocalDate.parse(fromStr);

        System.out.print("To Date (yyyy-mm-dd): ");
        String toStr = scanner.nextLine();
        LocalDate to = toStr.isEmpty() ? null : LocalDate.parse(toStr);

        System.out.print("Trainee Username: ");
        String traineeUsername = scanner.nextLine();
        if (traineeUsername.isEmpty()) traineeUsername = null;

        List<Training> trainings = facade.selectTrainerProfile(username).getTrainings();


        if (trainings == null || trainings.isEmpty()) {
            System.out.println("No trainings found.");
        } else {
            trainings.forEach(t -> System.out.println(t.getTrainingName() + " | Trainee: " + t.getTrainee().getUser().getUsername()));
        }
    }
}