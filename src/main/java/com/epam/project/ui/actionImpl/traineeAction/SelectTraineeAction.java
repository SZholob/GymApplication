package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainee;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SelectTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public SelectTraineeAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "4";
    }

    @Override
    public String getDescription() {
        return "Select Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Selecting a trainee profile...");
        System.out.println("Please enter the trainee's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Trainee trainee = facade.selectTraineeProfile(id);
        if (trainee != null) {
            System.out.println("Trainee profile found: " + trainee);
        } else {
            System.out.println("Trainee not found with ID: " + id);
        }
    }
}
