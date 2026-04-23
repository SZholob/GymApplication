package com.epam.project.service.impl;

import com.epam.project.dao.TrainingDao;
import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Date testDate;
    private Training testTraining;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        testTraining = new Training(1L, 1L, 2L, "Morning Yoga", TrainingType.YOGA, testDate, 60);
    }

    @Test
    void testCreateTrainingSuccess() {

        when(trainingDao.save(any(Training.class))).thenReturn(testTraining);

        Training result = trainingService.createTraining(1L, 2L, "Morning Yoga", TrainingType.YOGA, testDate, 60);

        assertNotNull(result);
        assertEquals(TrainingType.YOGA, result.getTrainingType());

        verify(trainingDao, times(1)).save(any(Training.class));
    }


    @Test
    void testSelectTrainingSuccess() {
        when(trainingDao.findById(1L)).thenReturn(testTraining);

        Training result = trainingService.selectTraining(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(trainingDao, times(1)).findById(1L);
    }


    @Test
    void testSelectTrainingNotFound() {
        when(trainingDao.findById(999L)).thenReturn(null);

        Training result = trainingService.selectTraining(999L);

        assertNull(result);
        verify(trainingDao, times(1)).findById(999L);
    }
}