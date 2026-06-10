package com.epam.project.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "MySuperSecretKeyForGymApplicationWhichIsVeryLongAndSecure123!");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);

        userDetails = new User("testUser", "password", Collections.emptyList());
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}