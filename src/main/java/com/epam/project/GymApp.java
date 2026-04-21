package com.epam.project;

import com.epam.project.conf.AppConfig;
import com.epam.project.ui.ConsoleMenu;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Gym Application
 */
public class GymApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.registerShutdownHook();

        ConsoleMenu menu = context.getBean(ConsoleMenu.class);
        menu.start();

        context.close();
    }
}
