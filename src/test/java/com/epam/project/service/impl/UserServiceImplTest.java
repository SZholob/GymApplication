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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", "John.Doe", "password123", true);
    }

    // ============== changePassword() Tests ==============

    @Test
    void testChangePasswordSuccess() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        userService.changePassword("John.Doe", "newPassword456");

        assertEquals("newPassword456", testUser.getPassword());

        verify(userDao, times(1)).findByUsername("John.Doe");
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void testChangePasswordUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userService.changePassword("nonexistent", "newPassword456"));

        assertEquals("User not found: nonexistent", exception.getMessage());

        verify(userDao, times(1)).findByUsername("nonexistent");
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testChangePasswordToSamePassword() {
        String samePassword = "password123";
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        userService.changePassword("John.Doe", samePassword);

        assertEquals(samePassword, testUser.getPassword());

        verify(userDao, times(1)).findByUsername("John.Doe");
        verify(userDao, times(1)).save(testUser);
    }

    // ============== toggleActivation() Tests ==============

    @Test
    void testToggleActivationFromActiveToInactive() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        userService.toggleActivation("John.Doe");

        assertFalse(testUser.getIsActive());

        verify(userDao, times(1)).findByUsername("John.Doe");
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void testToggleActivationFromInactiveToActive() {
        testUser.setIsActive(false);
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        userService.toggleActivation("John.Doe");

        assertTrue(testUser.getIsActive());

        verify(userDao, times(1)).findByUsername("John.Doe");
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void testToggleActivationUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userService.toggleActivation("nonexistent"));

        assertEquals("User not found: nonexistent", exception.getMessage());

        verify(userDao, times(1)).findByUsername("nonexistent");
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testToggleActivationMultipleTimes() {
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        // First toggle: active -> inactive
        userService.toggleActivation("John.Doe");
        assertFalse(testUser.getIsActive());

        // Second toggle: inactive -> active
        userService.toggleActivation("John.Doe");
        assertTrue(testUser.getIsActive());

        // Third toggle: active -> inactive
        userService.toggleActivation("John.Doe");
        assertFalse(testUser.getIsActive());

        verify(userDao, times(3)).findByUsername("John.Doe");
        verify(userDao, times(3)).save(testUser);
    }
}
