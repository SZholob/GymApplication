package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.model.Trainee;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        System.out.println("Please enter the trainee's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Trainee trainee = facade.selectTraineeProfile(id);
        if (trainee == null) {
            System.out.println("Trainee not found with ID: " + id);
            return;
        }
        System.out.println("Current trainee details: " + trainee);
        System.out.println("Please enter the trainee's new first name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            trainee.setFirstName(firstName);
        }
        System.out.println("Please enter the trainee's new last name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            trainee.setLastName(lastName);
        }
        System.out.println("Please enter the trainee's new date of birth (yyyy-mm-dd) (or press Enter to keep current): ");
        String dobInput = scanner.nextLine();
        if (!dobInput.isEmpty()) {
            try {
                Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dobInput);
                trainee.setDateOfBirth(dateOfBirth);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd. Keeping current date of birth.");
            }
        }
        System.out.println("Please enter the trainee's new address (or press Enter to keep current): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            trainee.setAddress(address);
        }
        Trainee updatedTrainee = facade.updateTraineeProfile(trainee);
        System.out.println("Trainee profile updated successfully! Updated details: " + updatedTrainee);
    }
}
