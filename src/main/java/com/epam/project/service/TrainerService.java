package com.epam.project.service;

import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {
    Trainer createProfile(String firstName, String lastName, String trainingTypeName);

    Trainer updateProfile(Trainer trainer);

    Trainer selectProfile(String username);

    List<Trainer> getUnassignedActiveTrainers(String traineeUsername);

    @Transactional(readOnly = true)
    List<Training> getTrainerTrainingsList(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername);
}
