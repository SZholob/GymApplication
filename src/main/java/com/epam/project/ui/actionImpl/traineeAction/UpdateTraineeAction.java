package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainee;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;

@Component
public class UpdateTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public UpdateTraineeAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "2";
    }

    @Override
    public String getDescription() {
        return "Update Trainee Profile";
    }

    @Override
    public void execute() {
        System.out.println("Updating a trainee profile...");

        System.out.println("Please enter the trainee's username: ");
        String username = scanner.nextLine();

        System.out.println("Please enter the trainee's password: ");
        String password = scanner.nextLine();

        if (!facade.authenticate(username, password)) {
            System.out.println("Authentication failed. Please check your username and password.");
            return;
        }

        Trainee trainee;
        try {
            trainee = facade.selectTraineeProfile(username);
        } catch (Exception e) {
            System.out.println("Error retrieving trainee profile: " + e.getMessage());
            return;
        }

        System.out.println("Current profile: " + trainee);

        System.out.println("Enter new first name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            trainee.getUser().setFirstName(firstName);
        }

        System.out.println("Enter new last name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            trainee.getUser().setLastName(lastName);
        }

        System.out.println("Enter new date of birth (yyyy-MM-dd) (or press Enter to keep current): ");
        String dobInput = scanner.nextLine();
        if (!dobInput.isEmpty()) {
            try {
                LocalDate dateOfBirth = LocalDate.parse(dobInput);
                trainee.setDateOfBirth(dateOfBirth);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Keeping current date of birth.");
            }
        }

        System.out.println("Please enter the trainee's new address (or press Enter to keep current): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            trainee.setAddress(address);
        }
        Trainee updatedTrainee = facade.updateTraineeProfile(trainee);
        System.out.println("Trainee profile updated successfully: " + updatedTrainee);
    }
}