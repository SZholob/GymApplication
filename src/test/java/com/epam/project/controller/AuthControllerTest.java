package com.epam.project.controller;

import com.epam.project.dto.TraineeRegistrationRequest;
import com.epam.project.dto.TrainerRegistrationRequest;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.model.User;
import com.epam.project.service.AuthenticationService;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.exception.GlobalExceptionHandler;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Mock
    private com.epam.project.security.JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerTraineeSuccess() throws Exception {
        User user = new User(1L, "John", "Doe", "John.Doe", "pwd", true);
        Trainee trainee = new Trainee(1L, LocalDate.of(1990,1,1), "Kyiv", user, null, null);

        when(traineeService.createProfile(anyString(), anyString(), any(), anyString())).thenReturn(trainee);
        org.springframework.security.core.userdetails.UserDetails ud = new org.springframework.security.core.userdetails.User("John.Doe", "pwd", java.util.Collections.emptyList());
        when(userDetailsService.loadUserByUsername("John.Doe")).thenReturn(ud);
        when(jwtService.generateToken(ud)).thenReturn("token");

        TraineeRegistrationRequest req = new TraineeRegistrationRequest("John","Doe",LocalDate.of(1990,1,1),"Kyiv");

        mockMvc.perform(post("/api/auth/trainee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("pwd"));

        verify(traineeService, times(1)).createProfile(anyString(), anyString(), any(), anyString());
    }

    @Test
    void registerTrainerSuccess() throws Exception {
        User user = new User(2L, "Jane", "Smith", "Jane.Smith", "pwd2", true);
        Trainer trainer = new Trainer(2L, null, user, null, null);

        when(trainerService.createProfile(anyString(), anyString(), anyString())).thenReturn(trainer);
        org.springframework.security.core.userdetails.UserDetails ud = new org.springframework.security.core.userdetails.User("Jane.Smith", "pwd2", java.util.Collections.emptyList());
        when(userDetailsService.loadUserByUsername("Jane.Smith")).thenReturn(ud);
        when(jwtService.generateToken(ud)).thenReturn("token2");

        TrainerRegistrationRequest req = new TrainerRegistrationRequest("Jane","Smith","YOGA");

        mockMvc.perform(post("/api/auth/trainer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Jane.Smith"))
                .andExpect(jsonPath("$.password").value("pwd2"));

        verify(trainerService, times(1)).createProfile(anyString(), anyString(), anyString());
    }

    @Test
    void loginSuccessAndFail() throws Exception {
        when(authenticationService.authenticate("John.Doe","pwd")).thenReturn("mocked-jwt-token");
        when(authenticationService.authenticate("John.Doe","wrong")).thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(get("/api/auth/login").param("username","John.Doe").param("password","pwd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        mockMvc.perform(get("/api/auth/login").param("username","John.Doe").param("password","wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));

        verify(authenticationService, times(2)).authenticate(anyString(), anyString());
    }
}


