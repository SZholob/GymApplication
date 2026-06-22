package com.epam.project.service.impl;

import com.epam.project.security.CustomUserDetailsService;
import com.epam.project.security.JwtService;
import com.epam.project.security.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testAuthenticateSuccess_returnsJwtToken_and_recordsSuccess() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserDetails userDetails = new User("user", "pass", java.util.Collections.emptyList());
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        String token = authenticationService.authenticate("user", "pass");

        assertEquals("mocked-jwt-token", token);
        verify(loginAttemptService, times(1)).loginSucceeded("user");
        verify(loginAttemptService, never()).loginFailed(anyString());
    }

    @Test
    void testAuthenticate_badCredentials_incrementsAttempts_and_throws() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad creds"));
        when(loginAttemptService.getAttempts("user")).thenReturn(1);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate("user", "wrong"));

        assertTrue(ex.getMessage().contains("Invalid username or password") || ex.getMessage().contains("Bad creds"));
        verify(loginAttemptService, times(1)).loginFailed("user");
        verify(loginAttemptService, times(1)).getAttempts("user");
    }

    @Test
    void testAuthenticate_lockedAfterThreeAttempts_throwsLockedException() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad creds"));
        when(loginAttemptService.getAttempts("user")).thenReturn(3);

        LockedException ex = assertThrows(LockedException.class,
                () -> authenticationService.authenticate("user", "wrong"));

        assertTrue(ex.getMessage().toLowerCase().contains("locked"));
        verify(loginAttemptService, times(1)).loginFailed("user");
        verify(loginAttemptService, times(1)).getAttempts("user");
    }

    @Test
    void testAuthenticate_whenAuthenticationManagerThrowsLockedException_propagatesWithoutCallingLoginFailed() {
        when(authenticationManager.authenticate(any())).thenThrow(new LockedException("Temporarily locked"));

        LockedException ex = assertThrows(LockedException.class,
                () -> authenticationService.authenticate("user", "pass"));

        assertEquals("Temporarily locked", ex.getMessage());
        verify(loginAttemptService, never()).loginFailed(anyString());
    }
}
