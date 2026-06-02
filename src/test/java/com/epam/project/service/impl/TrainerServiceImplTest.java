package com.epam.project.service.impl;

import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.dao.UserDao;
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
public class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private User testUser;
    private TrainingType testTrainingType;
    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Jane", "Smith", "Jane.Smith", "password456", true);
        testTrainingType = new TrainingType(1L, "YOGA");
        testTrainer = new Trainer(1L, testTrainingType, testUser, new ArrayList<>(), null);
    }

    // ============== createProfile() Tests ==============

    @Test
    void testCreateProfileSuccess() {
        when(userDao.findUsernamesByPrefix("Jane.Smith")).thenReturn(Collections.emptyList());
        when(trainingTypeDao.findByTypeName("YOGA")).thenReturn(Optional.of(testTrainingType));
        when(trainerDao.save(any(Trainer.class))).thenReturn(testTrainer);

        Trainer result = trainerService.createProfile("Jane", "Smith", "YOGA");

        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUser().getUsername());
        assertEquals("Jane", result.getUser().getFirstName());
        assertEquals("Smith", result.getUser().getLastName());
        assertEquals(testTrainingType, result.getSpecialization());

        verify(userDao, times(1)).findUsernamesByPrefix("Jane.Smith");
        verify(trainingTypeDao, times(1)).findByTypeName("YOGA");
        verify(trainerDao, times(1)).save(any(Trainer.class));
        verify(validationService, times(2)).validate(any());
    }

    @Test
    void testCreateProfileWithExistingUsernames() {
        when(userDao.findUsernamesByPrefix("Jane.Smith")).thenReturn(List.of("Jane.Smith"));
        when(trainingTypeDao.findByTypeName("YOGA")).thenReturn(Optional.of(testTrainingType));
        Trainer expectedTrainer = new Trainer(2L, testTrainingType, new User(2L, "Jane", "Smith", "Jane.Smith1", "password456", true), new ArrayList<>(), null);
        when(trainerDao.save(any(Trainer.class))).thenReturn(expectedTrainer);

        Trainer result = trainerService.createProfile("Jane", "Smith", "YOGA");

        assertNotNull(result);
        assertEquals("Jane.Smith1", result.getUser().getUsername());

        verify(userDao, times(1)).findUsernamesByPrefix("Jane.Smith");
        verify(trainingTypeDao, times(1)).findByTypeName("YOGA");
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void testCreateProfileTrainingTypeNotFound() {
        when(userDao.findUsernamesByPrefix("Jane.Smith")).thenReturn(Collections.emptyList());
        when(trainingTypeDao.findByTypeName("INVALID_TYPE")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainerService.createProfile("Jane", "Smith", "INVALID_TYPE"));

        assertEquals("Training Type not found: INVALID_TYPE", exception.getMessage());

        verify(userDao, times(1)).findUsernamesByPrefix("Jane.Smith");
        verify(trainingTypeDao, times(1)).findByTypeName("INVALID_TYPE");
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    // ============== selectProfile() Tests ==============

    @Test
    void testSelectProfileSuccess() {
        when(trainerDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));

        Trainer result = trainerService.selectProfile("Jane.Smith");

        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUser().getUsername());
        assertEquals("Jane", result.getUser().getFirstName());
        assertEquals("Smith", result.getUser().getLastName());

        verify(trainerDao, times(1)).findByUsername("Jane.Smith");
    }

    @Test
    void testSelectProfileNotFound() {
        when(trainerDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainerService.selectProfile("nonexistent"));

        assertEquals("Trainer with username nonexistent not found", exception.getMessage());

        verify(trainerDao, times(1)).findByUsername("nonexistent");
    }

    // ============== updateProfile() Tests ==============

    @Test
    void testUpdateProfileSuccess() {
        when(trainerDao.save(testTrainer)).thenReturn(testTrainer);

        Trainer result = trainerService.updateProfile(testTrainer);

        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUser().getUsername());
        assertEquals(testTrainingType, result.getSpecialization());

        verify(trainerDao, times(1)).save(testTrainer);
        verify(validationService, times(1)).validate(testTrainer.getUser());
        verify(validationService, times(1)).validate(testTrainer);
    }

    // ============== getUnassignedActiveTrainers() Tests ==============

    @Test
    void testGetUnassignedActiveTrainersSuccess() {
        List<Trainer> expectedTrainers = List.of(testTrainer);
        when(trainerDao.findUnassignedActiveTrainers("John.Doe")).thenReturn(expectedTrainers);

        List<Trainer> result = trainerService.getUnassignedActiveTrainers("John.Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane.Smith", result.get(0).getUser().getUsername());

        verify(trainerDao, times(1)).findUnassignedActiveTrainers("John.Doe");
    }

    @Test
    void testGetUnassignedActiveTrainersEmptyList() {
        when(trainerDao.findUnassignedActiveTrainers("John.Doe")).thenReturn(Collections.emptyList());

        List<Trainer> result = trainerService.getUnassignedActiveTrainers("John.Doe");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(trainerDao, times(1)).findUnassignedActiveTrainers("John.Doe");
    }

    // ============== getTrainerTrainingsList() Tests ==============

    @Test
    void testGetTrainerTrainingsListSuccess() {
        Training t = new Training();
        t.setTrainingName("Session A");
        t.setTrainingDate(LocalDate.of(2024, 5, 10));
        t.setTrainer(testTrainer);

        List<Training> expected = List.of(t);
        when(trainingDao.findTrainerTrainingsByCriteria("Jane.Smith", null, null, "John.Doe")).thenReturn(expected);

        List<Training> result = trainerService.getTrainerTrainingsList("Jane.Smith", null, null, "John.Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Session A", result.get(0).getTrainingName());

        verify(trainingDao, times(1)).findTrainerTrainingsByCriteria("Jane.Smith", null, null, "John.Doe");
    }

    @Test
    void testGetTrainerTrainingsListEmpty() {
        when(trainingDao.findTrainerTrainingsByCriteria("Jane.Smith", null, null, null)).thenReturn(Collections.emptyList());

        List<Training> result = trainerService.getTrainerTrainingsList("Jane.Smith", null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(trainingDao, times(1)).findTrainerTrainingsByCriteria("Jane.Smith", null, null, null);
    }
}