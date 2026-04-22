package com.epam.project.ui.actionImpl.trainerAction;

import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.ui.GymFacade;
import com.epam.project.ui.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UpdateTrainerAction implements MenuAction {

    private final GymFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public UpdateTrainerAction(GymFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getCommandCode() {
        return "6";
    }

    @Override
    public String getDescription() {
        return "Update Trainer Profile";
    }

    @Override
    public void execute() {
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
}
