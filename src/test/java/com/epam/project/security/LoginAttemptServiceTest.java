package com.epam.project.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginAttemptServiceTest {

    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    void testLoginSucceededClearsAttempts() {
        loginAttemptService.loginFailed("user1");
        loginAttemptService.loginSucceeded("user1");
        assertFalse(loginAttemptService.isBlocked("user1"));
    }

    @Test
    void testIsBlockedAfterThreeFails() {
        loginAttemptService.loginFailed("user2");
        loginAttemptService.loginFailed("user2");
        assertFalse(loginAttemptService.isBlocked("user2"));

        loginAttemptService.loginFailed("user2");
        assertTrue(loginAttemptService.isBlocked("user2"));
    }
}