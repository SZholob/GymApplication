package com.epam.project.controller;

import com.epam.project.dto.UpdateTraineeRequest;
import com.epam.project.exception.GlobalExceptionHandler;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.TrainingType;
import com.epam.project.model.User;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeController traineeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Trainee testTrainee;
    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        User user = new User(1L, "John","Doe","John.Doe","pwd",true);
        testTrainee = new Trainee(1L, LocalDate.of(1990,1,1), "Kyiv", user, new ArrayList<>(), null);

        User trainerUser = new User(2L, "Jane","Smith","Jane.Smith","pwd2",true);
        testTrainer = new Trainer(2L, new TrainingType(1L,"YOGA"), trainerUser, new ArrayList<>(), null);
    }

    @Test
    void getProfileSuccess() throws Exception {
        when(traineeService.selectProfile("John.Doe")).thenReturn(testTrainee);

        mockMvc.perform(get("/api/trainees/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(traineeService, times(1)).selectProfile("John.Doe");
    }

    @Test
    void updateProfileSuccess() throws Exception {
        when(traineeService.selectProfile("John.Doe")).thenReturn(testTrainee);
        when(traineeService.updateProfile(any(Trainee.class))).thenReturn(testTrainee);

        UpdateTraineeRequest req = new UpdateTraineeRequest("John.Doe","John","Doe",LocalDate.of(1990,1,1),"Kyiv",true);

        mockMvc.perform(put("/api/trainees/John.Doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(traineeService, times(1)).selectProfile("John.Doe");
        verify(traineeService, times(1)).updateProfile(any());
    }

    @Test
    void updateProfileUsernameMismatch() throws Exception {
        UpdateTraineeRequest req = new UpdateTraineeRequest("Other","John","Doe",LocalDate.of(1990,1,1),"Kyiv",true);

        mockMvc.perform(put("/api/trainees/John.Doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username mismatch"));
    }

    @Test
    void deleteProfileSuccess() throws Exception {
        mockMvc.perform(delete("/api/trainees/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));

        verify(traineeService, times(1)).deleteProfile("John.Doe");
    }

    @Test
    void getUnassignedTrainersSuccess() throws Exception {
        when(trainerService.getUnassignedActiveTrainers("John.Doe")).thenReturn(List.of(testTrainer));

        mockMvc.perform(get("/api/trainees/John.Doe/trainers/unassigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Jane.Smith"));

        verify(trainerService, times(1)).getUnassignedActiveTrainers("John.Doe");
    }

    @Test
    void updateTrainersListSuccess() throws Exception {
        doNothing().when(traineeService).updateTraineeTrainersList(eq("John.Doe"), anyList());
        when(traineeService.selectProfile("John.Doe")).thenReturn(testTrainee);

        // assign trainer to response
        testTrainee.getTrainers().add(testTrainer);

        List<String> list = List.of("Jane.Smith");

        mockMvc.perform(put("/api/trainees/John.Doe/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Jane.Smith"));

        verify(traineeService, times(1)).updateTraineeTrainersList(eq("John.Doe"), anyList());
        verify(traineeService, times(1)).selectProfile("John.Doe");
    }
}



