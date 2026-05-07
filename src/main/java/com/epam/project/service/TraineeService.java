package com.epam.project.service;

import com.epam.project.model.Trainee;
import com.epam.project.model.Training;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TraineeService {

    Trainee createProfile(String firstName, String lastName, LocalDate dateOfBirth, String address);

    Trainee updateProfile(Trainee trainee);

    void deleteProfile(String username);

    Trainee selectProfile(String username);


    void changePassword(String username, String newPassword);

    void toggleActivation(String username);

    void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames);

    @Transactional(readOnly = true)
    List<Training> getTraineeTrainingsList(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName);
}
