package com.epam.project.service;

import com.epam.project.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer createProfile(String firstName, String lastName, String trainingTypeName);

    Trainer updateProfile(Trainer trainer);

    Trainer selectProfile(String username);

    List<Trainer> getUnassignedActiveTrainers(String traineeUsername);
}
