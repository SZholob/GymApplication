package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CreateTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

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

        System.out.println("Available specializations: FITNESS, YOGA, ZUMBA, STRETCHING, RESISTANCE");
        System.out.print("Please enter a specialization name from the list above: ");
        String specializationName = scanner.nextLine().toUpperCase();

        try {
            Trainer trainer = facade.createTrainerProfile(firstName, lastName, specializationName);
            System.out.println("Trainer profile created successfully!\nDetails: " + trainer);
        } catch (Exception e) {
            System.out.println("Error creating trainer profile: " + e.getMessage());
        }
    }
}