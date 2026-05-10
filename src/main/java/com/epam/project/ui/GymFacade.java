package com.epam.project.ui;

import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public boolean authenticate(String username, String password) {
        return authenticationService.authenticate(username, password);
    }

    // --- TRAINEE ---
    public Trainee createTraineeProfile(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        return traineeService.createProfile(firstName, lastName, dateOfBirth, address);
    }

    public Trainee updateTraineeProfile(Trainee trainee) {
        return traineeService.updateProfile(trainee);
    }

    public void deleteTraineeProfile(String username) {
        traineeService.deleteProfile(username);
    }

    public Trainee selectTraineeProfile(String username) {
        return traineeService.selectProfile(username);
    }

    public List<Training> getTraineeTrainings(String username, LocalDate from, LocalDate to, String trainerUsername, String trainingType) {
        return traineeService.getTraineeTrainingsList(username, from, to, trainerUsername, trainingType);
    }

    public void updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        traineeService.updateTraineeTrainersList(traineeUsername, trainerUsernames);
    }

    // --- TRAINER ---
    public Trainer createTrainerProfile(String firstName, String lastName, String specialization) {
        return trainerService.createProfile(firstName, lastName, specialization);
    }

    public Trainer updateTrainerProfile(Trainer trainer) {
        return trainerService.updateProfile(trainer);
    }

    public Trainer selectTrainerProfile(String username) {
        return trainerService.selectProfile(username);
    }

    public Training createTraining(String traineeUsername, String trainerUsername, String trainingName, LocalDate trainingDate, int trainingDuration) {
        return trainingService.createTraining(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);
    }

    public List<Trainer> getUnassignedActiveTrainers(String traineeUsername) {
        return trainerService.getUnassignedActiveTrainers(traineeUsername);
    }

    // --- USER ---
    public void changeUserPassword(String username, String newPassword) {
        userService.changePassword(username, newPassword);
    }

    public void toggleUserActivation(String username) {
        userService.toggleActivation(username);
    }
}
