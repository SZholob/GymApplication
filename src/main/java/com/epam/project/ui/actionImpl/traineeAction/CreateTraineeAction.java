package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Component
public class CreateTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public CreateTraineeAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "1";
    }

    @Override
    public String getDescription() {
        return "Create Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Creating a new trainee profile...");
        System.out.println("Please enter the trainee's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter the trainee's last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter the trainee's date of birth (yyyy-mm-dd): ");
        Date dateOfBirthday;
        try {
            dateOfBirthday = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
        }catch (Exception e){
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        System.out.println("Please enter the trainee's address: ");
        String address = scanner.nextLine();
        System.out.println("Trainee profile created successfully!" +
                facade.createTraineeProfile(firstName, lastName, dateOfBirthday, address));
    }
}
