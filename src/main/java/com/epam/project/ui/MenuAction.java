package com.epam.project.ui;

public interface MenuAction {

    String getCommandCode();

    String getDescription();

    void execute();
}
