package com.epam.project.service;

import com.epam.project.dto.ActionType;
import com.epam.project.dto.TrainerWorkloadResponse;
import com.epam.project.dto.WorkloadRequest;
import com.epam.project.model.Month;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.model.Year;
import com.epam.project.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadService workloadService;

    private WorkloadRequest addRequest;
    private WorkloadRequest deleteRequest;
    private TrainerWorkload existingWorkload;

    @BeforeEach
    void setUp() {

        addRequest = new WorkloadRequest(
                "John.Doe", "John", "Doe", true,
                LocalDate.of(2026, 6, 15), 60, ActionType.ADD);

        deleteRequest = new WorkloadRequest(
                "John.Doe", "John", "Doe", true,
                LocalDate.of(2026, 6, 15), 30, ActionType.DELETE);


        List<Month> months = new ArrayList<>();
        months.add(new Month(6, 100));

        List<Year> years = new ArrayList<>();
        years.add(new Year(2026, months));

        existingWorkload = TrainerWorkload.builder()
                .id("mongo-id-123")
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .years(years)
                .build();
    }

    @Test
    void testUpdateWorkload_WhenTrainerNotFound_CreatesNewDocument() {
        when(repository.findByUsername("John.Doe")).thenReturn(Optional.empty());

        workloadService.updateWorkload(addRequest);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository, times(1)).save(captor.capture());

        TrainerWorkload savedDocument = captor.getValue();
        assertEquals("John.Doe", savedDocument.getUsername());
        assertEquals(1, savedDocument.getYears().size());
        assertEquals(2026, savedDocument.getYears().get(0).getYear());
        assertEquals(1, savedDocument.getYears().get(0).getMonths().size());
        assertEquals(60, savedDocument.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration());
    }

    @Test
    void testUpdateWorkload_WhenTrainerExists_AddsDurationToExistingMonth() {
        when(repository.findByUsername("John.Doe")).thenReturn(Optional.of(existingWorkload));

        workloadService.updateWorkload(addRequest);

        verify(repository, times(1)).save(existingWorkload);

        Integer updatedDuration = existingWorkload.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration();
        assertEquals(160, updatedDuration);
    }

    @Test
    void testUpdateWorkload_WhenActionIsDelete_SubtractsDuration() {
        when(repository.findByUsername("John.Doe")).thenReturn(Optional.of(existingWorkload));

        workloadService.updateWorkload(deleteRequest);

        verify(repository, times(1)).save(existingWorkload);

        Integer updatedDuration = existingWorkload.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration();
        assertEquals(70, updatedDuration);
    }

    @Test
    void testUpdateWorkload_WhenDeleteExceedsExistingHours_SetsToZero() {
        WorkloadRequest hugeDeleteRequest = new WorkloadRequest(
                "John.Doe", "John", "Doe", true,
                LocalDate.of(2026, 6, 15), 200, ActionType.DELETE);

        when(repository.findByUsername("John.Doe")).thenReturn(Optional.of(existingWorkload));

        workloadService.updateWorkload(hugeDeleteRequest);

        verify(repository, times(1)).save(existingWorkload);

        Integer updatedDuration = existingWorkload.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration();
        assertEquals(0, updatedDuration);
    }


    @Test
    void testGetTrainerWorkload_WhenTrainerExists_ReturnsMappedDto() {
        when(repository.findByUsername("John.Doe")).thenReturn(Optional.of(existingWorkload));

        TrainerWorkloadResponse response = workloadService.getTrainerWorkload("John.Doe");

        assertNotNull(response);
        assertEquals("John.Doe", response.username());
        assertEquals("John", response.firstName());
        assertEquals(1, response.years().size());

        com.epam.project.dto.YearWorkload yearDto = response.years().get(0);
        assertEquals(2026, yearDto.year());

        com.epam.project.dto.MonthWorkload monthDto = yearDto.months().get(0);
        assertEquals(6, monthDto.month());
        assertEquals(100, monthDto.trainingSummaryDuration());
    }

    @Test
    void testGetTrainerWorkload_WhenTrainerNotFound_ReturnsEmptyResponse() {
        when(repository.findByUsername("Unknown.Trainer")).thenReturn(Optional.empty());

        TrainerWorkloadResponse response = workloadService.getTrainerWorkload("Unknown.Trainer");

        assertNotNull(response);
        assertEquals("Unknown.Trainer", response.username());
        assertTrue(response.years().isEmpty());
    }
}