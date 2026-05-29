package com.epam.project.controller;

import com.epam.project.dto.AddTrainingRequest;
import com.epam.project.exception.GlobalExceptionHandler;
import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;
import com.epam.project.model.Trainer;
import com.epam.project.model.Trainee;
import com.epam.project.model.User;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.service.TrainingService;
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
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingController trainingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Training testTraining;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        User traineeUser = new User(1L, "John","Doe","John.Doe","pwd",true);
        Trainee trainee = new Trainee(1L, LocalDate.of(1990,1,1), "Kyiv", traineeUser, new ArrayList<>(), null);

        User trainerUser = new User(2L, "Jane","Smith","Jane.Smith","pwd2",true);
        Trainer trainer = new Trainer(2L, new TrainingType(1L,"YOGA"), trainerUser, new ArrayList<>(), null);

        testTraining = new Training();
        testTraining.setTrainingName("Morning Yoga");
        testTraining.setTrainingDate(LocalDate.of(2024,5,10));
        testTraining.setTrainingType(new TrainingType(1L,"YOGA"));
        testTraining.setTrainingDuration(60);
        testTraining.setTrainer(trainer);
        testTraining.setTrainee(trainee);
    }

    @Test
    void addTrainingSuccess() throws Exception {
        AddTrainingRequest req = new AddTrainingRequest("John.Doe","Jane.Smith","Morning Yoga",LocalDate.of(2024,5,10),60);

        mockMvc.perform(post("/api/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));

        verify(trainingService, times(1)).createTraining(anyString(), anyString(), anyString(), any(), anyInt());
    }

    @Test
    void getTraineeTrainingsSuccess() throws Exception {
        when(traineeService.getTraineeTrainingsList("John.Doe", null, null, null, null)).thenReturn(List.of(testTraining));

        mockMvc.perform(get("/api/trainings/trainee/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Morning Yoga"));

        verify(traineeService, times(1)).getTraineeTrainingsList("John.Doe", null, null, null, null);
    }

    @Test
    void getTrainerTrainingsSuccess() throws Exception {
        when(trainerService.getTrainerTrainingsList("Jane.Smith", null, null, null)).thenReturn(List.of(testTraining));

        mockMvc.perform(get("/api/trainings/trainer/Jane.Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Morning Yoga"));

        verify(trainerService, times(1)).getTrainerTrainingsList("Jane.Smith", null, null, null);
    }

    @Test
    void getTrainingTypesSuccess() throws Exception {
        when(trainingService.getTrainingTypes()).thenReturn(List.of(new TrainingType(1L, "YOGA")));

        mockMvc.perform(get("/api/trainings/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeName").value("YOGA"));

        verify(trainingService, times(1)).getTrainingTypes();
    }
}

