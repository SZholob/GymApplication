package com.epam.project.ui.actionImpl.traineeAction;

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
public class GetTraineeTrainingsAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Override public String getCommandCode() { return "10"; }
    @Override public String getDescription() { return "Get Trainee Trainings List (with filters)"; }

    @Override
    public void execute() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        if (!facade.authenticate(username, scanner.nextLine())) {
            System.out.println("Auth failed."); return;
        }

        System.out.println("--- Enter Filters (Press Enter to skip any filter) ---");
        System.out.print("From Date (yyyy-mm-dd): ");
        String fromStr = scanner.nextLine();
        LocalDate from = fromStr.isEmpty() ? null : LocalDate.parse(fromStr);

        System.out.print("To Date (yyyy-mm-dd): ");
        String toStr = scanner.nextLine();
        LocalDate to = toStr.isEmpty() ? null : LocalDate.parse(toStr);

        System.out.print("Trainer Username: ");
        String trainerUsername = scanner.nextLine();
        if (trainerUsername.isEmpty()) trainerUsername = null;

        System.out.print("Training Type Name: ");
        String typeName = scanner.nextLine();
        if (typeName.isEmpty()) typeName = null;

        List<Training> trainings = facade.getTraineeTrainings(username, from, to, trainerUsername, typeName);
        if (trainings.isEmpty()) {
            System.out.println("No trainings found matching these criteria.");
        } else {
            trainings.forEach(t -> System.out.println(t.getTrainingName() + " | Date: " + t.getTrainingDate()));
        }
    }
}