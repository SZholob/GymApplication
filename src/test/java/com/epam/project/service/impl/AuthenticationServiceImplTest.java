package com.epam.project.service.impl;

import com.epam.project.dao.UserDao;
import com.epam.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", "John.Doe", "password123", true);
    }

    // ============== authenticate() Tests ==============

    @Test
    void testAuthenticateSuccess() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        boolean result = authenticationService.authenticate("John.Doe", "password123");

        assertTrue(result);

        verify(userDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        boolean result = authenticationService.authenticate("nonexistent", "password123");

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testAuthenticateWrongPassword() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        boolean result = authenticationService.authenticate("John.Doe", "wrongpassword");

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateEmptyPassword() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        boolean result = authenticationService.authenticate("John.Doe", "");

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateNullPassword() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        boolean result = authenticationService.authenticate("John.Doe", null);

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateCaseSensitiveUsername() {
        when(userDao.findByUsername("john.doe")).thenReturn(Optional.empty());

        boolean result = authenticationService.authenticate("john.doe", "password123");

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("john.doe");
    }

    @Test
    void testAuthenticateCaseSensitivePassword() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        boolean result = authenticationService.authenticate("John.Doe", "Password123");

        assertFalse(result);

        verify(userDao, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateMultipleAttempts() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));

        // First attempt - correct password
        boolean result1 = authenticationService.authenticate("John.Doe", "password123");
        assertTrue(result1);

        // Second attempt - wrong password
        boolean result2 = authenticationService.authenticate("John.Doe", "wrongpassword");
        assertFalse(result2);

        // Third attempt - correct password again
        boolean result3 = authenticationService.authenticate("John.Doe", "password123");
        assertTrue(result3);

        verify(userDao, times(3)).findByUsername("John.Doe");
    }


    @Test
    void testAuthenticateWithDifferentUser() {
        User differentUser = new User(2L, "Jane", "Smith", "Jane.Smith", "password456", true);
        when(userDao.findByUsername("Jane.Smith")).thenReturn(Optional.of(differentUser));

        boolean result = authenticationService.authenticate("Jane.Smith", "password456");

        assertTrue(result);

        verify(userDao, times(1)).findByUsername("Jane.Smith");
    }
}
