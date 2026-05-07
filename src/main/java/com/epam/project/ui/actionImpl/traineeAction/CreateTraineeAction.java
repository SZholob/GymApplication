package com.epam.project.ui.actionImpl.traineeAction;

import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CreateTraineeAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

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

        System.out.println("Please enter the trainee's date of birth (yyyy-mm-dd) (or press Enter to skip): ");
        String dobInput = scanner.nextLine();
        LocalDate dateOfBirth = null;
        if (!dobInput.isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(dobInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Profile creation aborted.");
                return;
            }
        }

        System.out.println("Please enter the trainee's address (or press Enter to skip): ");
        String address = scanner.nextLine();
        if (address.isEmpty()) {
            address = null;
        }

        System.out.println("Trainee profile created successfully!\nDetails: " +
                facade.createTraineeProfile(firstName, lastName, dateOfBirth, address));
    }
}