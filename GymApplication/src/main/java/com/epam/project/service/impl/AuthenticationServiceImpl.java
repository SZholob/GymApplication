package com.epam.project.service.impl;

import com.epam.project.security.CustomUserDetailsService;
import com.epam.project.security.JwtService;
import com.epam.project.security.LoginAttemptService;
import com.epam.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public String authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (authentication.isAuthenticated()) {
                loginAttemptService.loginSucceeded(username);
                logger.info("User '{}' successfully authenticated.", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                return jwtService.generateToken(userDetails);
            }
            throw new BadCredentialsException("Authentication failed");


        } catch (LockedException e) {

            throw e;
        } catch (AuthenticationException e) {

            loginAttemptService.loginFailed(username);
            int attempts = loginAttemptService.getAttempts(username);

            if (attempts >= 3) {
                logger.warn("User '{}' has been locked due to 3 failed attempts.", username);
                throw new LockedException("Account is temporarily locked due to 3 failed login attempts. Try again in 5 minutes.");
            } else {
                logger.warn("Failed login for '{}'. Attempt {}/3.", username, attempts);
                throw new BadCredentialsException("Invalid username or password. Attempt " + attempts + " of 3.");
            }
        }
    }
}