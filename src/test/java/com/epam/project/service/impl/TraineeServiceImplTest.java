package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.UserDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.User;
import com.epam.project.model.Trainer;
import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;
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
public class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private ValidationService validationService;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private LocalDate testDate;
    private User testUser;
    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Training testTraining;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(1990, 5, 15);
        testUser = new User(1L, "John", "Doe", "John.Doe", "password123", true);
        testTrainee = new Trainee(1L, testDate, "Kyiv", testUser, new ArrayList<>(), null);

        User trainerUser = new User(2L, "Jane", "Smith", "Jane.Smith", "password456", true);
        testTrainingType = new TrainingType(1L, "YOGA");
        testTrainer = new Trainer(2L, testTrainingType, trainerUser, new ArrayList<>(), null);

        testTraining = new Training();
        testTraining.setId(1L);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);
        testTraining.setTrainingName("Morning Yoga");
        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainingDate(LocalDate.of(2024, 5, 10));
        testTraining.setTrainingDuration(60);
    }

    @Test
    void testCreateProfileSuccess() {
        when(userDao.findUsernamesByPrefix("John.Doe")).thenReturn(Collections.emptyList());
        when(traineeDao.save(any(Trainee.class))).thenReturn(testTrainee);

        Trainee result = traineeService.createProfile("John", "Doe", testDate, "Kyiv");

        assertNotNull(result);
        assertEquals("John.Doe", result.getUser().getUsername());
        assertEquals("John", result.getUser().getFirstName());
        assertEquals("Doe", result.getUser().getLastName());
        assertEquals(testDate, result.getDateOfBirth());
        assertEquals("Kyiv", result.getAddress());

        verify(userDao, times(1)).findUsernamesByPrefix("John.Doe");
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void testCreateProfileWithExistingUsernames() {
        when(userDao.findUsernamesByPrefix("John.Doe")).thenReturn(List.of("John.Doe"));
        Trainee expectedTrainee = new Trainee(2L, testDate, "Kyiv", new User(2L, "John", "Doe", "John.Doe1", "password123", true), new ArrayList<>(), null);
        when(traineeDao.save(any(Trainee.class))).thenReturn(expectedTrainee);

        Trainee result = traineeService.createProfile("John", "Doe", testDate, "Kyiv");

        assertNotNull(result);
        assertEquals("John.Doe1", result.getUser().getUsername());

        verify(userDao, times(1)).findUsernamesByPrefix("John.Doe");
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void testSelectProfileSuccess() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));

        Trainee result = traineeService.selectProfile("John.Doe");

        assertNotNull(result);
        assertEquals("John.Doe", result.getUser().getUsername());
        assertEquals("John", result.getUser().getFirstName());
        assertEquals("Doe", result.getUser().getLastName());
        verify(traineeDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testSelectProfileNotFound() {
        when(traineeDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> traineeService.selectProfile("nonexistent"));

        assertEquals("Trainee with username nonexistent not found", exception.getMessage());
        verify(traineeDao, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testUpdateProfileSuccess() {
        when(traineeDao.save(testTrainee)).thenReturn(testTrainee);

        Trainee result = traineeService.updateProfile(testTrainee);

        assertNotNull(result);
        assertEquals("John.Doe", result.getUser().getUsername());
        verify(traineeDao, times(1)).save(testTrainee);
    }

    @Test
    void testDeleteProfileSuccess() {
        traineeService.deleteProfile("John.Doe");

        verify(traineeDao, times(1)).deleteByUsername("John.Doe");
    }

    // ============== updateTraineeTrainersList() Tests ==============

    @Test
    void testUpdateTraineeTrainersListSuccess() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));
        when(traineeDao.save(any(Trainee.class))).thenReturn(testTrainee);

        List<String> trainerUsernames = List.of("Jane.Smith");
        traineeService.updateTraineeTrainersList("John.Doe", trainerUsernames);

        assertEquals(1, testTrainee.getTrainers().size());
        assertEquals(testTrainer, testTrainee.getTrainers().get(0));

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("Jane.Smith");
        verify(traineeDao, times(1)).save(testTrainee);
    }

    @Test
    void testUpdateTraineeTrainersListTraineeNotFound() {
        when(traineeDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> traineeService.updateTraineeTrainersList("nonexistent", List.of("Jane.Smith")));

        assertEquals("Trainee with username nonexistent not found", exception.getMessage());

        verify(traineeDao, times(1)).findByUsername("nonexistent");
        verify(trainerDao, never()).findByUsername(any());
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeTrainersListTrainerNotFound() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(trainerDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> traineeService.updateTraineeTrainersList("John.Doe", List.of("nonexistent")));

        assertEquals("Trainer not found: nonexistent", exception.getMessage());

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, times(1)).findByUsername("nonexistent");
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeTrainersListEmptyList() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(traineeDao.save(any(Trainee.class))).thenReturn(testTrainee);

        traineeService.updateTraineeTrainersList("John.Doe", Collections.emptyList());

        assertTrue(testTrainee.getTrainers().isEmpty());

        verify(traineeDao, times(1)).findByUsername("John.Doe");
        verify(trainerDao, never()).findByUsername(any());
        verify(traineeDao, times(1)).save(testTrainee);
    }

    // ============== getTraineeTrainingsList() Tests ==============

    @Test
    void testGetTraineeTrainingsListSuccess() {
        List<Training> expectedTrainings = List.of(testTraining);
        when(trainingDao.findTraineeTrainingsByCriteria("John.Doe", null, null, null, null)).thenReturn(expectedTrainings);

        List<Training> result = traineeService.getTraineeTrainingsList("John.Doe", null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morning Yoga", result.get(0).getTrainingName());

        verify(trainingDao, times(1)).findTraineeTrainingsByCriteria("John.Doe", null, null, null, null);
    }

    @Test
    void testGetTraineeTrainingsListWithFilters() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        List<Training> expectedTrainings = List.of(testTraining);
        when(trainingDao.findTraineeTrainingsByCriteria("John.Doe", fromDate, toDate, "Jane.Smith", "YOGA")).thenReturn(expectedTrainings);

        List<Training> result = traineeService.getTraineeTrainingsList("John.Doe", fromDate, toDate, "Jane.Smith", "YOGA");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(trainingDao, times(1)).findTraineeTrainingsByCriteria("John.Doe", fromDate, toDate, "Jane.Smith", "YOGA");
    }

    @Test
    void testGetTraineeTrainingsListEmptyResult() {
        when(trainingDao.findTraineeTrainingsByCriteria("John.Doe", null, null, null, null)).thenReturn(Collections.emptyList());

        List<Training> result = traineeService.getTraineeTrainingsList("John.Doe", null, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(trainingDao, times(1)).findTraineeTrainingsByCriteria("John.Doe", null, null, null, null);
    }
}