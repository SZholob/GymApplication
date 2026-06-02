package com.epam.project.controller;

import com.epam.project.dto.UpdateTrainerRequest;
import com.epam.project.exception.GlobalExceptionHandler;
import com.epam.project.model.Trainer;
import com.epam.project.model.TrainingType;
import com.epam.project.model.User;
import com.epam.project.service.TrainerService;
import com.epam.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerController trainerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        User user = new User(2L, "Jane","Smith","Jane.Smith","pwd2",true);
        testTrainer = new Trainer(2L, new TrainingType(1L,"YOGA"), user, new ArrayList<>(), null);
    }

    @Test
    void getProfileSuccess() throws Exception {
        when(trainerService.selectProfile("Jane.Smith")).thenReturn(testTrainer);

        mockMvc.perform(get("/api/trainers/Jane.Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));

        verify(trainerService, times(1)).selectProfile("Jane.Smith");
    }

    @Test
    void updateProfileSuccess() throws Exception {
        when(trainerService.selectProfile("Jane.Smith")).thenReturn(testTrainer);
        when(trainerService.updateProfile(any(Trainer.class))).thenReturn(testTrainer);

        UpdateTrainerRequest req = new UpdateTrainerRequest("Jane.Smith","Jane","Smith","YOGA",true);

        mockMvc.perform(put("/api/trainers/Jane.Smith")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));

        verify(trainerService, times(2)).selectProfile("Jane.Smith");
        verify(trainerService, times(1)).updateProfile(any());
    }

    @Test
    void toggleActivationSuccess() throws Exception {
        mockMvc.perform(patch("/api/trainers/Jane.Smith/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));

        verify(userService, times(1)).toggleActivation("Jane.Smith");
    }
}

