package com.epam.project.controller;

import com.epam.project.dto.ChangePasswordRequest;
import com.epam.project.exception.GlobalExceptionHandler;
import com.epam.project.service.AuthenticationService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void changePasswordSuccess() throws Exception {
        when(authenticationService.authenticate("John.Doe","old")).thenReturn("mocked-jwt-token");

        ChangePasswordRequest req = new ChangePasswordRequest("John.Doe","old","newpwd");

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));

        verify(userService, times(1)).changePassword("John.Doe","newpwd");
    }

    @Test
    void changePasswordUnauthorized() throws Exception {
        when(authenticationService.authenticate("John.Doe","old")).thenThrow(new BadCredentialsException("Invalid old password"));

        ChangePasswordRequest req = new ChangePasswordRequest("John.Doe","old","newpwd");

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid old password"));

        verify(userService, never()).changePassword(anyString(), anyString());
    }
}

