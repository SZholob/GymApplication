package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.service.TrainerService;
import com.epam.project.service.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDao trainerDao;

    private TraineeDao traineeDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainer createProfile(String firstName, String lastName, TrainingType specialization) {
        Set<String> existingUsernames = trainerDao
                .findAll()
                .stream()
                .map(Trainer::getUsername)
                .collect(Collectors.toSet());

        existingUsernames.addAll(traineeDao
                .findAll()
                .stream()
                .map(Trainee::getUsername)
                .collect(Collectors.toSet()));

        String username = UserUtils.generateUsername(firstName, lastName, existingUsernames);
        String password = UserUtils.generatePassword();
        Trainer trainer = new Trainer(null, firstName, lastName, username, password, true, specialization);
        Trainer saved = trainerDao.save(trainer);
        logger.info("Trainer with username {} was created successfully. Details: {}",
                saved.getUsername(), saved);
        return saved;
    }

    public Trainer updateProfile(Trainer trainer) {
        Trainer updated = trainerDao.save(trainer);
        logger.info("Updated trainer profile with username {}. Details: {}",
                updated.getUsername(),  updated);
        return updated;
    }

    public Trainer selectProfile(Long id) {
        Trainer trainer = trainerDao.findById(id);
        if (trainer != null) {
            logger.debug("Selected trainer profile with username {}. Details: {}",
                    trainer.getUsername(), trainer);
        }
        return trainer;
    }
}
