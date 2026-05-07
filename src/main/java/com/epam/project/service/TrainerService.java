package com.epam.project.service;

import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;

public interface TrainerService {
    Trainer createProfile(String firstName, String lastName, String trainingTypeName);

    Trainer updateProfile(Trainer trainer);

    Trainer selectProfile(String username);

    void changePassword(String username, String newPassword);

    void toggleActivation(String username);
}
