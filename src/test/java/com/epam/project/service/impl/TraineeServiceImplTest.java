package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Date testDate;
    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        testTrainee = new Trainee(1L, "John", "Doe", "John.Doe", "password123", true, testDate, "Kyiv");
    }

    @Test
    void testCreateProfileSuccess() {
        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        when(traineeDao.save(any(Trainee.class))).thenReturn(testTrainee);

        Trainee result = traineeService.createProfile("John", "Doe", testDate, "Kyiv");

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());

        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void testCreateProfileWithExistingUsernames() {
        Trainee existingTrainee = new Trainee(1L, "John", "Doe", "John.Doe", "pass", true, testDate, "Kyiv");

        when(traineeDao.findAll()).thenReturn(List.of(existingTrainee));
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        Trainee expectedTrainee = new Trainee(2L, "John", "Doe", "John.Doe1", "pass", true, testDate, "Kyiv");
        when(traineeDao.save(any(Trainee.class))).thenReturn(expectedTrainee);

        Trainee result = traineeService.createProfile("John", "Doe", testDate, "Kyiv");

        assertNotNull(result);
        assertEquals("John.Doe1", result.getUsername());
    }

    @Test
    void testSelectProfileSuccess() {
        when(traineeDao.findById(1L)).thenReturn(testTrainee);

        Trainee result = traineeService.selectProfile(1L);

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        verify(traineeDao, times(1)).findById(1L);
    }


    @Test
    void testSelectProfileNotFound() {
        when(traineeDao.findById(999L)).thenReturn(null);

        Trainee result = traineeService.selectProfile(999L);

        assertNull(result);
        verify(traineeDao, times(1)).findById(999L);
    }


    @Test
    void testUpdateProfileSuccess() {

        when(traineeDao.save(testTrainee)).thenReturn(testTrainee);

        Trainee result = traineeService.updateProfile(testTrainee);

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        verify(traineeDao, times(1)).save(testTrainee);
    }

    @Test
    void testDeleteProfileSuccess() {

        traineeService.deleteProfile(1L);

        verify(traineeDao, times(1)).delete(1L);
    }
}