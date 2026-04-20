package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.service.TraineeService;
import com.epam.project.service.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDao traineeDao;

    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainee createProfile(String firstName, String lastName, Date dateOfBirth, String address) {
        Set<String> existingUsernames = traineeDao
                .findAll()
                .stream()
                .map(Trainee::getUsername)
                .collect(Collectors.toSet());

        existingUsernames.addAll(trainerDao
                .findAll()
                .stream()
                .map(Trainer::getUsername)
                .collect(Collectors.toSet()));

        String username = UserUtils.generateUsername(firstName, lastName, existingUsernames);
        String password = UserUtils.generatePassword();
        Trainee trainee = new Trainee(null, firstName, lastName, username, password, true, dateOfBirth, address);
        Trainee saved = traineeDao.save(trainee);
        logger.info("Trainee with username {} was created successfully. Details: {}",
                saved.getUsername(), saved);
        return saved;
    }

    public Trainee updateProfile(Trainee trainee) {
        Trainee updated = traineeDao.save(trainee);
        logger.info("Updated trainee profile with username {}. Details: {}",
                updated.getUsername(), updated);
        return updated;
    }

    public void deleteProfile(Long id) {
        traineeDao.delete(id);
        logger.info("Deleted trainee profile with id {}", id);
    }

    public Trainee selectProfile(Long id) {
        Trainee trainee = traineeDao.findById(id);
        if (trainee != null) {
            logger.debug("Selected trainee profile with username {}. Details: {}",
                    trainee.getUsername(), trainee);
        }
        return trainee;
    }
}
