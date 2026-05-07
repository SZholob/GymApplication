package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.UserDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.User;
import com.epam.project.service.TraineeService;
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
@RequiredArgsConstructor
@Transactional
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingDao trainingDao;
    private final UserDao userDao;

    @Override
    public Trainee createProfile(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        String baseUsername = firstName + "." + lastName;

        Set<String> existingUsernames = new HashSet<>(userDao.findUsernamesByPrefix(baseUsername));

        String username = UserUtils.generateUsername(firstName, lastName, existingUsernames);
        String password = UserUtils.generatePassword();

        User user = new User(null, firstName, lastName, username, password, true);
        Trainee trainee = new Trainee(null, dateOfBirth, address, user, null, null);

        Trainee savedTrainee = traineeDao.save(trainee);
        logger.info("Created Trainee Profile. Username: {}, Password: {}", username, password);
        return savedTrainee;
    }

    @Override
    public Trainee updateProfile(Trainee trainee) {
        Trainee updated = traineeDao.save(trainee);
        logger.info("Updated Trainee profile: {}", trainee.getUser().getUsername());
        return updated;
    }

    @Override
    public void deleteProfile(String username) {
        traineeDao.deleteByUsername(username);
        logger.info("Deleted Trainee profile: {}", username);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee selectProfile(String username) {
        return traineeDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee with username " + username + " not found"));
    }


    @Override
    public void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = selectProfile(traineeUsername);

        List<Trainer> newTrainers = trainerUsernames.stream()
                .map(tUsername -> trainerDao.findByUsername(tUsername)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + tUsername)))
                .toList();

        trainee.setTrainers(newTrainers);
        traineeDao.save(trainee);
        logger.info("Updated trainers list for trainee: {}", traineeUsername);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getTraineeTrainingsList(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName) {
        return trainingDao.findTraineeTrainingsByCriteria(traineeUsername, fromDate, toDate, trainerUsername, trainingTypeName);
    }
}
