package com.epam.project.ui;

import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

@Component
public class ConsoleMenu {
    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public ConsoleMenu(GymFacade facade) {
        this.facade = facade;
    }

    public void start() {
        System.out.println("Welcome to the Gym CRM Application!");

        System.out.println("\n--- Gym CRM Menu ---");
        while (true) {
            displayMenu();
            System.out.print("Please select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    try {
                        createTraineeProfile();
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                    }
                    break;
                case "2":
                    updateTraineeProfile();
                    break;
                case "3":
                    deleteTraineeProfile();
                    break;
                case "4":
                    selectTraineeProfile();
                    break;
                case "5":
                    createTrainerProfile();
                    break;
                case "6":
                    updateTrainerProfile();
                    break;
                case "7":
                    selectTrainerProfile();
                    break;
                case "8":
                    // Code to create training session
                    break;
                case "9":
                    // Code to select training session
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createTrainingSession(){
        Training training = new Training();
        System.out.println("Please enter the name of the trainee you want to create: ");
        String name = scanner.nextLine();
        System.out.println("Please enter the name of the trainer you want to create: ");
        String trainerName = scanner.nextLine();

    }

    private void selectTrainerProfile() {
        System.out.println("Selecting a trainer profile...");
        System.out.println("Please enter the trainer's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Trainer trainer = facade.selectTrainerProfile(id);
        if (trainer != null) {
            System.out.println("Trainer profile found: " + trainer);
        } else {
            System.out.println("Trainer not found with ID: " + id);
        }
    }

    private void updateTrainerProfile() {
        System.out.println("Updating a trainer profile...");
        System.out.println("Please enter the trainer's ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Trainer trainer = facade.selectTrainerProfile(id);
        if (trainer == null) {
            System.out.println("Trainer not found with ID: " + id);
            return;
        }
        System.out.println("Current trainer details: " + trainer);
        System.out.println("Please enter the trainer's new first name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            trainer.setFirstName(firstName);
        }
        System.out.println("Please enter the trainer's new last name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            trainer.setLastName(lastName);
        }
        System.out.println("Available specializations:");
        TrainingType[] types = TrainingType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Please select a new specialization by number (or press Enter to keep current): ");
        String specializationInput = scanner.nextLine();
        if (!specializationInput.isEmpty()) {
            int choice = Integer.parseInt(specializationInput);
            if (choice < 1 || choice > types.length) {
                System.out.println("Invalid choice. Keeping current specialization.");
            } else {
                trainer.setSpecialization(types[choice - 1]);
            }
        }
        Trainer updatedTrainer = facade.updateTrainerProfile(trainer);
        System.out.println("Trainer profile updated successfully! Updated details: " + updatedTrainer);
    }

    private void createTrainerProfile() {
        System.out.println("Creating a new trainer profile...");
        System.out.println("Please enter the trainer's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter the trainer's last name: ");
        String lastName = scanner.nextLine();

        System.out.println("Available specializations:");
        TrainingType[] types = TrainingType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Please select a specialization by number: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > types.length) {
            System.out.println("Invalid choice. Trainer profile creation cancelled.");
            return;
        }
        TrainingType specialization = types[choice - 1];

        Trainer trainer = facade.createTrainerProfile(firstName, lastName, specialization);
        System.out.println("Trainer profile created successfully! " + trainer);
    }


private void selectTraineeProfile() {
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

private void deleteTraineeProfile() {
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

private void updateTraineeProfile() {
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


private void displayMenu() {
    System.out.println("1. Create Trainee Profile");
    System.out.println("2. Update Trainee Profile");
    System.out.println("3. Delete Trainee Profile");
    System.out.println("4. Select Trainee Profile");
    System.out.println("5. Create Trainer Profile");
    System.out.println("6. Update Trainer Profile");
    System.out.println("7. Select Trainer Profile");
    System.out.println("8. Create Training Session");
    System.out.println("9. Select Training Session");
    System.out.println("0. Exit");
}

private void createTraineeProfile() throws ParseException {
    System.out.println("Creating a new trainee profile...");
    System.out.println("Please enter the trainee's first name: ");
    String firstName = scanner.nextLine();
    System.out.println("Please enter the trainee's last name: ");
    String lastName = scanner.nextLine();
    System.out.println("Please enter the trainee's date of birth (yyyy-mm-dd): ");
    Date dateOfBirthday = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
    System.out.println("Please enter the trainee's address: ");
    String address = scanner.nextLine();
    System.out.println("Trainee profile created successfully!" +
            facade.createTraineeProfile(firstName, lastName, dateOfBirthday, address));
}
}
