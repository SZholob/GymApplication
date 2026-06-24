package com.epam.project.servise;

import com.epam.project.dto.ActionType;
import com.epam.project.dto.TrainerWorkloadResponse;
import com.epam.project.dto.WorkloadRequest;
import com.epam.project.dto.YearWorkload;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.repository.TrainerWorkloadRepository;
import com.epam.project.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadService service;

    private WorkloadRequest testAddRequest;
    private WorkloadRequest testDeleteRequest;
    private TrainerWorkload testWorkload;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 5, 15);

        testAddRequest = new WorkloadRequest(
                "jane.smith",
                "Jane",
                "Smith",
                true,
                testDate,
                60,
                ActionType.ADD
        );

        testDeleteRequest = new WorkloadRequest(
                "jane.smith",
                "Jane",
                "Smith",
                true,
                testDate,
                60,
                ActionType.DELETE
        );

        testWorkload = new TrainerWorkload();
        testWorkload.setId(1L);
        testWorkload.setUsername("jane.smith");
        testWorkload.setFirstName("Jane");
        testWorkload.setLastName("Smith");
        testWorkload.setIsActive(true);
        testWorkload.setYear(2024);
        testWorkload.setMonth(5);
        testWorkload.setTrainingSummaryDuration(120);
    }

    // ============== updateWorkload() - ADD Action Tests ==============

    @Test
    void testUpdateWorkloadAddNewRecord() {
        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.empty());
        when(repository.save(any(TrainerWorkload.class))).thenReturn(testWorkload);

        service.updateWorkload(testAddRequest);

        verify(repository, times(1)).findByUsernameAndYearAndMonth("jane.smith", 2024, 5);
        verify(repository, times(1)).save(argThat(workload ->
                workload.getUsername().equals("jane.smith") &&
                        workload.getFirstName().equals("Jane") &&
                        workload.getLastName().equals("Smith") &&
                        workload.getTrainingSummaryDuration() == 60 &&
                        workload.getYear() == 2024 &&
                        workload.getMonth() == 5 &&
                        workload.getIsActive() == true
        ));
    }

    @Test
    void testUpdateWorkloadAddToExistingRecord() {
        TrainerWorkload existingWorkload = new TrainerWorkload();
        existingWorkload.setId(1L);
        existingWorkload.setUsername("jane.smith");
        existingWorkload.setFirstName("Jane");
        existingWorkload.setLastName("Smith");
        existingWorkload.setIsActive(true);
        existingWorkload.setYear(2024);
        existingWorkload.setMonth(5);
        existingWorkload.setTrainingSummaryDuration(120);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(existingWorkload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(existingWorkload);

        service.updateWorkload(testAddRequest);

        verify(repository, times(1)).findByUsernameAndYearAndMonth("jane.smith", 2024, 5);
        verify(repository, times(1)).save(argThat(workload ->
                workload.getTrainingSummaryDuration() == 180  // 120 + 60
        ));
    }

    @Test
    void testUpdateWorkloadAddMultipleTimes() {
        TrainerWorkload workload = new TrainerWorkload();
        workload.setId(1L);
        workload.setUsername("jane.smith");
        workload.setFirstName("Jane");
        workload.setLastName("Smith");
        workload.setIsActive(true);
        workload.setYear(2024);
        workload.setMonth(5);
        workload.setTrainingSummaryDuration(120);

        when(repository.findByUsernameAndYearAndMonth(anyString(), anyInt(), anyInt()))
                .thenReturn(Optional.of(workload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(workload);

        service.updateWorkload(testAddRequest);
        service.updateWorkload(testAddRequest);
        service.updateWorkload(testAddRequest);

        verify(repository, times(3)).save(any(TrainerWorkload.class));
    }

    @Test
    void testUpdateWorkloadAddWithDifferentDurations() {
        TrainerWorkload workload = new TrainerWorkload();
        workload.setId(1L);
        workload.setUsername("jane.smith");
        workload.setTrainingSummaryDuration(30);
        workload.setYear(2024);
        workload.setMonth(5);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(workload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(workload);

        WorkloadRequest request90min = new WorkloadRequest(
                "jane.smith", "Jane", "Smith", true,
                testDate, 90, ActionType.ADD
        );

        service.updateWorkload(request90min);

        verify(repository, times(1)).save(argThat(w ->
                w.getTrainingSummaryDuration() == 120  // 30 + 90
        ));
    }

    // ============== updateWorkload() - DELETE Action Tests ==============

    @Test
    void testUpdateWorkloadDeleteExistingRecord() {
        TrainerWorkload existingWorkload = new TrainerWorkload();
        existingWorkload.setId(1L);
        existingWorkload.setUsername("jane.smith");
        existingWorkload.setTrainingSummaryDuration(120);
        existingWorkload.setYear(2024);
        existingWorkload.setMonth(5);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(existingWorkload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(existingWorkload);

        service.updateWorkload(testDeleteRequest);

        verify(repository, times(1)).findByUsernameAndYearAndMonth("jane.smith", 2024, 5);
        verify(repository, times(1)).save(argThat(workload ->
                workload.getTrainingSummaryDuration() == 60  // 120 - 60
        ));
    }

    @Test
    void testUpdateWorkloadDeleteFromNonExistentRecord() {
        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.empty());

        service.updateWorkload(testDeleteRequest);

        verify(repository, times(1)).findByUsernameAndYearAndMonth("jane.smith", 2024, 5);
        verify(repository, never()).save(any(TrainerWorkload.class));
    }

    @Test
    void testUpdateWorkloadDeleteMoreThanExists() {
        TrainerWorkload existingWorkload = new TrainerWorkload();
        existingWorkload.setId(1L);
        existingWorkload.setUsername("jane.smith");
        existingWorkload.setTrainingSummaryDuration(30);
        existingWorkload.setYear(2024);
        existingWorkload.setMonth(5);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(existingWorkload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(existingWorkload);

        service.updateWorkload(testDeleteRequest);  // Trying to delete 60 from 30

        verify(repository, times(1)).save(argThat(workload ->
                workload.getTrainingSummaryDuration() == 0  // Math.max(0, 30-60) = 0
        ));
    }

    @Test
    void testUpdateWorkloadDeleteZeroDuration() {
        TrainerWorkload existingWorkload = new TrainerWorkload();
        existingWorkload.setId(1L);
        existingWorkload.setUsername("jane.smith");
        existingWorkload.setTrainingSummaryDuration(60);
        existingWorkload.setYear(2024);
        existingWorkload.setMonth(5);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(existingWorkload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(existingWorkload);

        service.updateWorkload(testDeleteRequest);

        verify(repository, times(1)).save(argThat(workload ->
                workload.getTrainingSummaryDuration() == 0
        ));
    }

    // ============== getTrainerWorkload() Tests ==============

    @Test
    void testGetTrainerWorkloadSuccess() {
        TrainerWorkload workload1 = new TrainerWorkload();
        workload1.setId(1L);
        workload1.setUsername("jane.smith");
        workload1.setFirstName("Jane");
        workload1.setLastName("Smith");
        workload1.setIsActive(true);
        workload1.setYear(2024);
        workload1.setMonth(1);
        workload1.setTrainingSummaryDuration(100);

        TrainerWorkload workload2 = new TrainerWorkload();
        workload2.setId(2L);
        workload2.setUsername("jane.smith");
        workload2.setFirstName("Jane");
        workload2.setLastName("Smith");
        workload2.setIsActive(true);
        workload2.setYear(2024);
        workload2.setMonth(5);
        workload2.setTrainingSummaryDuration(120);

        when(repository.findAllByUsername("jane.smith"))
                .thenReturn(List.of(workload1, workload2));

        TrainerWorkloadResponse response = service.getTrainerWorkload("jane.smith");

        assertNotNull(response);
        assertEquals("jane.smith", response.username());
        assertEquals("Jane", response.firstName());
        assertEquals("Smith", response.lastName());
        assertTrue(response.isActive());
        assertFalse(response.years().isEmpty());
        assertEquals(1, response.years().size());
        assertEquals(2024, response.years().get(0).year());
        assertEquals(2, response.years().get(0).months().size());

        verify(repository, times(1)).findAllByUsername("jane.smith");
    }

    @Test
    void testGetTrainerWorkloadEmpty() {
        when(repository.findAllByUsername("unknown.trainer"))
                .thenReturn(new ArrayList<>());

        TrainerWorkloadResponse response = service.getTrainerWorkload("unknown.trainer");

        assertNotNull(response);
        assertEquals("unknown.trainer", response.username());
        assertEquals("", response.firstName());
        assertEquals("", response.lastName());
        assertFalse(response.isActive());
        assertTrue(response.years().isEmpty());

        verify(repository, times(1)).findAllByUsername("unknown.trainer");
    }

    @Test
    void testGetTrainerWorkloadMultipleYears() {
        TrainerWorkload workload2023 = new TrainerWorkload();
        workload2023.setId(1L);
        workload2023.setUsername("jane.smith");
        workload2023.setFirstName("Jane");
        workload2023.setLastName("Smith");
        workload2023.setIsActive(true);
        workload2023.setYear(2023);
        workload2023.setMonth(10);
        workload2023.setTrainingSummaryDuration(50);

        TrainerWorkload workload2024_1 = new TrainerWorkload();
        workload2024_1.setId(2L);
        workload2024_1.setUsername("jane.smith");
        workload2024_1.setFirstName("Jane");
        workload2024_1.setLastName("Smith");
        workload2024_1.setIsActive(true);
        workload2024_1.setYear(2024);
        workload2024_1.setMonth(1);
        workload2024_1.setTrainingSummaryDuration(100);

        TrainerWorkload workload2024_2 = new TrainerWorkload();
        workload2024_2.setId(3L);
        workload2024_2.setUsername("jane.smith");
        workload2024_2.setFirstName("Jane");
        workload2024_2.setLastName("Smith");
        workload2024_2.setIsActive(true);
        workload2024_2.setYear(2024);
        workload2024_2.setMonth(5);
        workload2024_2.setTrainingSummaryDuration(120);

        when(repository.findAllByUsername("jane.smith"))
                .thenReturn(List.of(workload2023, workload2024_1, workload2024_2));

        TrainerWorkloadResponse response = service.getTrainerWorkload("jane.smith");

        assertNotNull(response);
        assertEquals(2, response.years().size());

        Optional<YearWorkload> year2023 = response.years().stream()
                .filter(y -> y.year() == 2023).findFirst();
        assertTrue(year2023.isPresent());
        assertEquals(1, year2023.get().months().size());

        Optional<YearWorkload> year2024 = response.years().stream()
                .filter(y -> y.year() == 2024).findFirst();
        assertTrue(year2024.isPresent());
        assertEquals(2, year2024.get().months().size());

        verify(repository, times(1)).findAllByUsername("jane.smith");
    }

    @Test
    void testGetTrainerWorkloadSingleMonth() {
        TrainerWorkload workload = new TrainerWorkload();
        workload.setId(1L);
        workload.setUsername("john.doe");
        workload.setFirstName("John");
        workload.setLastName("Doe");
        workload.setIsActive(false);
        workload.setYear(2024);
        workload.setMonth(3);
        workload.setTrainingSummaryDuration(75);

        when(repository.findAllByUsername("john.doe"))
                .thenReturn(List.of(workload));

        TrainerWorkloadResponse response = service.getTrainerWorkload("john.doe");

        assertNotNull(response);
        assertEquals("john.doe", response.username());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertFalse(response.isActive());
        assertEquals(1, response.years().size());
        assertEquals(2024, response.years().get(0).year());
        assertEquals(1, response.years().get(0).months().size());
        assertEquals(3, response.years().get(0).months().get(0).month());
        assertEquals(75, response.years().get(0).months().get(0).trainingSummaryDuration());

        verify(repository, times(1)).findAllByUsername("john.doe");
    }

    @Test
    void testGetTrainerWorkloadMultipleMonthsSameYear() {
        List<TrainerWorkload> workloads = new ArrayList<>();
        for (int month = 1; month <= 3; month++) {
            TrainerWorkload workload = new TrainerWorkload();
            workload.setId((long) month);
            workload.setUsername("trainer.test");
            workload.setFirstName("Trainer");
            workload.setLastName("Test");
            workload.setIsActive(true);
            workload.setYear(2024);
            workload.setMonth(month);
            workload.setTrainingSummaryDuration(60 * month);
            workloads.add(workload);
        }

        when(repository.findAllByUsername("trainer.test"))
                .thenReturn(workloads);

        TrainerWorkloadResponse response = service.getTrainerWorkload("trainer.test");

        assertNotNull(response);
        assertEquals(1, response.years().size());
        assertEquals(3, response.years().get(0).months().size());
        assertEquals(2024, response.years().get(0).year());

        verify(repository, times(1)).findAllByUsername("trainer.test");
    }

    // ============== Edge Cases and Integration Tests ==============

    @Test
    void testUpdateWorkloadAddWithInactiveTrainer() {
        WorkloadRequest inactiveRequest = new WorkloadRequest(
                "inactive.trainer",
                "Inactive",
                "Trainer",
                false,  // isActive = false
                testDate,
                45,
                ActionType.ADD
        );

        when(repository.findByUsernameAndYearAndMonth("inactive.trainer", 2024, 5))
                .thenReturn(Optional.empty());
        when(repository.save(any(TrainerWorkload.class))).thenReturn(testWorkload);

        service.updateWorkload(inactiveRequest);

        verify(repository, times(1)).save(argThat(workload ->
                workload.getIsActive() == false
        ));
    }

    @Test
    void testUpdateWorkloadWithDifferentMonths() {
        TrainerWorkload workloadMay = new TrainerWorkload();
        workloadMay.setUsername("jane.smith");
        workloadMay.setYear(2024);
        workloadMay.setMonth(5);
        workloadMay.setTrainingSummaryDuration(60);

        TrainerWorkload workloadJune = new TrainerWorkload();
        workloadJune.setUsername("jane.smith");
        workloadJune.setYear(2024);
        workloadJune.setMonth(6);
        workloadJune.setTrainingSummaryDuration(90);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(workloadMay));
        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 6))
                .thenReturn(Optional.of(workloadJune));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(workloadMay);

        WorkloadRequest addMay = new WorkloadRequest("jane.smith", "Jane", "Smith", true,
                LocalDate.of(2024, 5, 10), 30, ActionType.ADD);
        WorkloadRequest addJune = new WorkloadRequest("jane.smith", "Jane", "Smith", true,
                LocalDate.of(2024, 6, 10), 30, ActionType.ADD);

        service.updateWorkload(addMay);
        service.updateWorkload(addJune);

        verify(repository, times(2)).save(any(TrainerWorkload.class));
    }

    @Test
    void testUpdateWorkloadSequentialAddAndDelete() {
        TrainerWorkload workload = new TrainerWorkload();
        workload.setUsername("jane.smith");
        workload.setYear(2024);
        workload.setMonth(5);
        workload.setTrainingSummaryDuration(100);

        when(repository.findByUsernameAndYearAndMonth("jane.smith", 2024, 5))
                .thenReturn(Optional.of(workload));
        when(repository.save(any(TrainerWorkload.class))).thenReturn(workload);

        WorkloadRequest add30 = new WorkloadRequest("jane.smith", "Jane", "Smith", true,
                testDate, 30, ActionType.ADD);
        WorkloadRequest delete40 = new WorkloadRequest("jane.smith", "Jane", "Smith", true,
                testDate, 40, ActionType.DELETE);

        service.updateWorkload(add30);
        service.updateWorkload(delete40);

        verify(repository, times(2)).save(any(TrainerWorkload.class));
    }
}