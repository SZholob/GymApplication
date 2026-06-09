package com.epam.project.service.impl;

import com.epam.project.actuator.GymMetrics;
import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;
import com.epam.project.model.User;
import com.epam.project.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private ValidationService validationService;

    @Mock
    private GymMetrics gymMetrics;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Training testTraining;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 5, 10);
        testTrainingType = new TrainingType(1L, "YOGA");

        User traineeUser = new User(1L, "John", "Doe", "John.Doe", "password123", true);
        testTrainee = new Trainee(1L, LocalDate.of(1990, 1, 1), "Kyiv", traineeUser, new ArrayList<>(), null);

        User trainerUser = new User(2L, "Jane", "Smith", "Jane.Smith", "password456", true);
        testTrainer = new Trainer(2L, testTrainingType, trainerUser, new ArrayList<>(), null);

        testTraining = new Training();
        testTraining.setId(1L);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);
        testTraining.setTrainingName("Morning Yoga");
        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainingDate(testDate);
        testTraining.setTrainingDuration(60);
    }

    // ============== createTraining() Tests ==============

    @Test
    void testCreateTrainingSuccess() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));
        when(trainingDao.save(any(Training.class))).thenReturn(testTraining);

        Training result = trainingService.createTraining("John.Doe", "Jane.Smith", "Morning Yoga", testDate, 60);

        assertNotNull(result);
        assertEquals("Morning Yoga", result.getTrainingName());
        assertEquals(testDate, result.getTrainingDate());
        assertEquals(60, result.getTrainingDuration());
        assertEquals(testTrainee, result.getTrainee());
        assertEquals(testTrainer, result.getTrainer());
        assertEquals(testTrainingType, result.getTrainingType());

        // Verify that trainer was added to trainee's trainers list
        assertTrue(testTrainee.getTrainers().contains(testTrainer));
        // Verify that trainee was added to trainer's trainees list
        assertTrue(testTrainer.getTrainees().contains(testTrainee));

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("Jane.Smith");
        verify(trainingDao, times(1)).save(any(Training.class));
        verify(validationService, times(1)).validate(any(Training.class));
    }

    @Test
    void testCreateTrainingTraineeNotFound() {
        when(traineeDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> trainingService.createTraining("nonexistent", "Jane.Smith", "Morning Yoga", testDate, 60));

        assertEquals("Trainee not found: nonexistent", exception.getMessage());

        verify(traineeDao, times(1)).findByUsername("nonexistent");
        verify(trainerDao, never()).findByUsername(any());
        verify(trainingDao, never()).save(any(Training.class));
    }

    @Test
    void testCreateTrainingTrainerNotFound() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> trainingService.createTraining("John.Doe", "nonexistent", "Morning Yoga", testDate, 60));

        assertEquals("Trainer not found: nonexistent", exception.getMessage());

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("nonexistent");
        verify(trainingDao, never()).save(any(Training.class));
    }

    @Test
    void testCreateTrainingWithDifferentDuration() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));

        Training expectedTraining = new Training();
        expectedTraining.setId(2L);
        expectedTraining.setTrainee(testTrainee);
        expectedTraining.setTrainer(testTrainer);
        expectedTraining.setTrainingName("Evening Cardio");
        expectedTraining.setTrainingType(testTrainingType);
        expectedTraining.setTrainingDate(testDate);
        expectedTraining.setTrainingDuration(90);

        when(trainingDao.save(any(Training.class))).thenReturn(expectedTraining);

        Training result = trainingService.createTraining("John.Doe", "Jane.Smith", "Evening Cardio", testDate, 90);

        assertNotNull(result);
        assertEquals("Evening Cardio", result.getTrainingName());
        assertEquals(90, result.getTrainingDuration());

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("Jane.Smith");
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void testCreateTrainingWithDifferentDate() {
        LocalDate differentDate = LocalDate.of(2024, 6, 15);

        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));

        Training expectedTraining = new Training();
        expectedTraining.setId(3L);
        expectedTraining.setTrainee(testTrainee);
        expectedTraining.setTrainer(testTrainer);
        expectedTraining.setTrainingName("Weekend Training");
        expectedTraining.setTrainingType(testTrainingType);
        expectedTraining.setTrainingDate(differentDate);
        expectedTraining.setTrainingDuration(45);

        when(trainingDao.save(any(Training.class))).thenReturn(expectedTraining);

        Training result = trainingService.createTraining("John.Doe", "Jane.Smith", "Weekend Training", differentDate, 45);

        assertNotNull(result);
        assertEquals(differentDate, result.getTrainingDate());
        assertEquals(45, result.getTrainingDuration());

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("Jane.Smith");
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void testGetTrainingTypesSuccess() {
        TrainingType tt = new TrainingType(1L, "YOGA");
        List<TrainingType> expected = List.of(tt);
        when(trainingTypeDao.findAll()).thenReturn(expected);

        List<TrainingType> result = trainingService.getTrainingTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("YOGA", result.get(0).getTrainingTypeName());

        verify(trainingTypeDao, times(1)).findAll();
    }

    @Test
    void testGetTrainingTypesEmpty() {
        when(trainingTypeDao.findAll()).thenReturn(Collections.emptyList());

        List<TrainingType> result = trainingService.getTrainingTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(trainingTypeDao, times(1)).findAll();
    }
}