package com.epam.project.ui;

import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTraineeProfile(String firstName, String lastName, Date dateOfBirth, String address) {
        return traineeService.createProfile(firstName, lastName, dateOfBirth, address);
    }

    public Trainee updateTraineeProfile(Trainee trainee) {
        return traineeService.updateProfile(trainee);
    }

    public void deleteTraineeProfile(Long id) {
        traineeService.deleteProfile(id);
    }

    public Trainee selectTraineeProfile(Long id) {
        return traineeService.selectProfile(id);
    }

    public Trainer createTrainerProfile(String firstName, String lastName, TrainingType specialization) {
        return trainerService.createProfile(firstName, lastName, specialization);
    }

    public Trainer updateTrainerProfile(Trainer trainer) {
        return trainerService.updateProfile(trainer);
    }

    public Trainer selectTrainerProfile(Long id) {
        return trainerService.selectProfile(id);
    }


    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public Training selectTraining(Long id) {
        return trainingService.selectTraining(id);
    }
}
