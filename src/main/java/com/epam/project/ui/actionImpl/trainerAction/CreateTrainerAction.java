package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public CreateTrainerAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "5";
    }

    @Override
    public String getDescription() {
        return "Create Trainer Profile";
    }

    @Override
    public void execute() {
        System.out.println("Creating a new trainer profile...");
        System.out.println("Please enter the trainer's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter the trainer's last name: ");
        String lastName = scanner.nextLine();

        TrainingType specialization = getTrainingType(scanner);
        if (specialization == null) {
            System.out.println("Invalid choice. Trainer profile creation cancelled.");
            return;
        }

        Trainer trainer = facade.createTrainerProfile(firstName, lastName, specialization);
        System.out.println("Trainer profile created successfully! " + trainer);
    }

    @Nullable
    public static TrainingType getTrainingType(Scanner scanner) {
        System.out.println("Available specializations:");
        TrainingType[] types = TrainingType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Please select a specialization by number: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > types.length) {
            return null;
        }
        return types[choice - 1];
    }
}
