package com.epam.project.service;

import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;

public interface TrainerService {
    Trainer createProfile(String firstName, String lastName, TrainingType specialization);

    Trainer updateProfile(Trainer trainer);

    Trainer selectProfile(Long id);
}
