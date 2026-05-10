package com.epam.project.service.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dao.UserDao;
import com.epam.project.model.Trainee;
import com.epam.project.model.User;
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
    private UserDao userDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private LocalDate testDate;
    private User testUser;
    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(1990, 5, 15);
        testUser = new User(1L, "John", "Doe", "John.Doe", "password123", true);
        testTrainee = new Trainee(1L, testDate, "Kyiv", testUser, new ArrayList<>(), null);
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
}