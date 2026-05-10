package com.epam.project.service.impl;

import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.dao.UserDao;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;
import com.epam.project.model.User;
import com.epam.project.service.TrainerService;
import com.epam.project.service.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TrainingDao trainingDao;
    private final TrainingTypeDao trainingTypeDao;

    @Override
    public Trainer createProfile(String firstName, String lastName, String trainingTypeName) {
        String baseUsername = firstName + "." + lastName;

        Set<String> existingUsernames = new HashSet<>(userDao.findUsernamesByPrefix(baseUsername));

        String username = UserUtils.generateUsername(firstName, lastName, existingUsernames);
        String password = UserUtils.generatePassword();

        User user = new User(null, firstName, lastName, username, password, true);

        TrainingType specialization = trainingTypeDao.findByTypeName(trainingTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Training Type not found: " + trainingTypeName));

        Trainer trainer = new Trainer(null, specialization, user, null, null);

        Trainer saved = trainerDao.save(trainer);
        logger.info("Trainer Profile created. Username: {}, Password: {}", username, password);
        return saved;
    }

    @Override
    public Trainer updateProfile(Trainer trainer) {
        Trainer updated = trainerDao.save(trainer);
        logger.info("Updated trainer profile: {}", updated.getUser().getUsername());
        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer selectProfile(String username) {
        return trainerDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer with username " + username + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedActiveTrainers(String traineeUsername) {
        logger.debug("Fetching unassigned active trainers for trainee: {}", traineeUsername);
        return trainerDao.findUnassignedActiveTrainers(traineeUsername);
    }

    @Override
    public List<Training> getTrainerTrainingsList(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername) {
        return trainingDao.findTrainerTrainingsByCriteria(trainerUsername, fromDate, toDate, traineeUsername);
    }
}
