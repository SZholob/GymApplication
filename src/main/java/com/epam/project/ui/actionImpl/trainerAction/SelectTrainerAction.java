package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SelectTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public SelectTrainerAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "7";
    }

    @Override
    public String getDescription() {
        return "Selecting a trainer profile...";
    }

    @Override
    public void execute() {
        System.out.println("Please enter the trainer's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Trainer trainer = facade.selectTrainerProfile(id);
        if (trainer != null) {
            System.out.println("Trainer profile found: " + trainer);
        } else {
            System.out.println("Trainer not found with ID: " + id);
        }
    }
}
