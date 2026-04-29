package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.enums.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        testTrainer = new Trainer(1L, "Jane", "Smith", "Jane.Smith", "password456", true, TrainingType.YOGA);
    }

    @Test
    void testCreateProfileSuccess() {
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());
        when(traineeDao.findAll()).thenReturn(Collections.emptyList());

        when(trainerDao.save(any(Trainer.class))).thenReturn(testTrainer);

        Trainer result = trainerService.createProfile("Jane", "Smith", TrainingType.YOGA);

        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals(TrainingType.YOGA, result.getSpecialization());

        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void testCreateProfileWithExistingUsernames() {

        Trainee existingTrainee = new Trainee(1L, "Jane", "Smith", "Jane.Smith", "pass", true, null, "Kyiv");


        when(trainerDao.findAll()).thenReturn(Collections.emptyList());
        when(traineeDao.findAll()).thenReturn(List.of(existingTrainee));

        Trainer expectedTrainer = new Trainer(2L, "Jane", "Smith", "Jane.Smith1", "pass", true, TrainingType.YOGA);
        when(trainerDao.save(any(Trainer.class))).thenReturn(expectedTrainer);

        Trainer result = trainerService.createProfile("Jane", "Smith", TrainingType.YOGA);

        assertNotNull(result);

        assertEquals("Jane.Smith1", result.getUsername());
    }


    @Test
    void testSelectProfileSuccess() {
        when(trainerDao.findById(1L)).thenReturn(testTrainer);

        Trainer result = trainerService.selectProfile(1L);

        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        verify(trainerDao, times(1)).findById(1L);
    }

    @Test
    void testSelectProfileNotFound() {
        when(trainerDao.findById(999L)).thenReturn(null);

        Trainer result = trainerService.selectProfile(999L);

        assertNull(result);
        verify(trainerDao, times(1)).findById(999L);
    }

    @Test
    void testUpdateProfileSuccess() {
        when(trainerDao.save(testTrainer)).thenReturn(testTrainer);

        Trainer result = trainerService.updateProfile(testTrainer);

        assertNotNull(result);
        assertEquals(TrainingType.YOGA, result.getSpecialization());
        verify(trainerDao, times(1)).save(testTrainer);
    }
}