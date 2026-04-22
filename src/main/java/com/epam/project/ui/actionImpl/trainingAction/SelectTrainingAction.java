package com.epam.project.ui.actionImpl.trainingAction;

import com.epam.project.model.Training;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SelectTrainingAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public SelectTrainingAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "9";
    }

    @Override
    public String getDescription() {
        return "Select a training session by ID";
    }

    @Override
    public void execute() {
        System.out.println("Selecting a training session...");
        System.out.println("Please enter the training's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Training training = facade.selectTraining(id);
        if (training != null) {
            System.out.println("Training session found: " + training);
        } else {
            System.out.println("Training session not found with ID: " + id);
        }
    }
}
