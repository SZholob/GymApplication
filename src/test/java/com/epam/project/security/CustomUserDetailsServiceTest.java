package com.epam.project.security;

import com.epam.project.dao.UserDao;
import com.epam.project.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UserDao userDao;
    @Mock private LoginAttemptService loginAttemptService;

    @InjectMocks private CustomUserDetailsService userDetailsService;

    @Test
    void testLoadUserSuccess() {
        User dbUser = new User(1L, "John", "Doe", "John.Doe", "hashed", true);
        when(loginAttemptService.isBlocked("John.Doe")).thenReturn(false);
        when(userDao.findByUsername("John.Doe")).thenReturn(Optional.of(dbUser));

        UserDetails result = userDetailsService.loadUserByUsername("John.Doe");

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
    }

    @Test
    void testLoadUserBlockedThrowsException() {
        when(loginAttemptService.isBlocked("hacker")).thenReturn(true);
        assertThrows(LockedException.class, () -> userDetailsService.loadUserByUsername("hacker"));
    }

    @Test
    void testLoadUserNotFoundThrowsException() {
        when(loginAttemptService.isBlocked("ghost")).thenReturn(false);
        when(userDao.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("ghost"));
    }
}