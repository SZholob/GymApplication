package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DeleteTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner =  new Scanner(System.in);

    @Autowired
    public DeleteTraineeAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "3";
    }

    @Override
    public String getDescription() {
        return "Delete Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Deleting a trainee profile...");
        System.out.println("Please enter the trainee's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println("Are you sure you want to delete the trainee profile with ID " + id + "? (Y/N)");
        String confirmation = scanner.nextLine();
        while (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("N")) {
            System.out.println("Invalid input. Please enter 'Y' to confirm deletion or 'N' to cancel.");
            confirmation = scanner.nextLine();
        }
        if (confirmation.equalsIgnoreCase("Y")) {
            facade.deleteTraineeProfile(id);
            System.out.println("Trainee profile deleted successfully!");
            System.out.println("Deletion cancelled.");
        }
        if (confirmation.equalsIgnoreCase("N")) {
            System.out.println("Deletion cancelled.");
        }
    }
}
