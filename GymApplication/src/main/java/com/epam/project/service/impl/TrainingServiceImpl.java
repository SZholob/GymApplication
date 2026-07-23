package com.epam.project.service.impl;

import com.epam.project.actuator.GymMetrics;
import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.dto.ActionType;
import com.epam.project.dto.WorkloadRequest;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;
import com.epam.project.service.TrainingService;
import com.epam.project.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingTypeDao trainingTypeDao;
    private final ValidationService validationService;
    private final GymMetrics gymMetrics;
    private final JmsTemplate jmsTemplate;

    public Training createTraining(String traineeUsername, String trainerUsername, String trainingName, LocalDate trainingDate, Integer trainingDuration) {

        Trainee trainee = traineeDao.findByUsername(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + traineeUsername));

        Trainer trainer = trainerDao.findByUsername(trainerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + trainerUsername));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainer.getSpecialization());
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        if (!trainee.getTrainers().contains(trainer)) {
            trainee.getTrainers().add(trainer);
        }

        if (!trainer.getTrainees().contains(trainee)) {
            trainer.getTrainees().add(trainee);
        }
        validationService.validate(training);

        long startTime = System.currentTimeMillis();

        Training savedTraining = trainingDao.save(training);

        long endTime = System.currentTimeMillis();
        gymMetrics.recordTrainingCreationTime(endTime - startTime);

        WorkloadRequest workloadRequest = new WorkloadRequest(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ActionType.ADD
        );
        String txId = MDC.get("transactionId");

        jmsTemplate.convertAndSend("workload.queue", workloadRequest, message -> {

            if (txId != null) {
                message.setStringProperty("X-Transaction-ID", txId);
            }
            return message;
        });

        logger.info("Sent an asynchronous message to ActiveMQ for the trainer: {}", workloadRequest.trainerUsername());

        logger.info("Created new training: '{}' for trainee: '{}' and trainer: '{}'",
                trainingName, traineeUsername, trainerUsername);

        gymMetrics.incrementTrainingCount(training.getTrainingType().getTrainingTypeName());

        return savedTraining;
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        logger.info("Fetching all training types");
        return trainingTypeDao.findAll();
    }

    @Override
    public void deleteTraining(Long trainingId) {
        Training training = trainingDao.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found: " + trainingId));

        trainingDao.deleteById(trainingId);

        WorkloadRequest workloadRequest = new WorkloadRequest(
                /*training.getTrainer().getUser().getUsername()*/"",
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(),
                training.getTrainer().getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ActionType.DELETE
        );

        String txId = MDC.get("transactionId");

        jmsTemplate.convertAndSend("workload.queue", workloadRequest, message -> {

            if (txId != null) {
                message.setStringProperty("X-Transaction-ID", txId);
            }
            return message;
        });

        logger.info("Sent an asynchronous message to ActiveMQ for the trainer: {}", workloadRequest.trainerUsername());

        logger.info("Deleted training with ID: {}", trainingId);
    }
}
