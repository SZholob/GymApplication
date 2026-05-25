package com.epam.project.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, MenuAction> actionMap = new HashMap<>();

    @Autowired
    public ConsoleMenu(List<MenuAction> allActions) {
        for (MenuAction action : allActions) {
            actionMap.put(action.getCommandCode(), action);
        }
    }

    public void start() {
        System.out.println("Welcome to the Gym CRM Application!");

        while (true) {
            displayMenu();
            System.out.println("Please enter a command:");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("Exiting the application. Goodbye!");
                break;
            }

            MenuAction action = actionMap.get(choice);
            if (action != null) {
                action.execute();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

    }

    private void displayMenu() {
        System.out.println("\n--- Gym CRM Menu ---");

        actionMap.values().stream()
                .sorted(Comparator.comparingInt(action -> Integer.parseInt(action.getCommandCode())))
                .forEach(action -> System.out.println(action.getCommandCode() + ". " + action.getDescription()));

        System.out.println("0. Exit");
    }
}
