package com.epam.project.interceptor;

import com.epam.project.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        if (uri.contains("/api/auth/trainee/register") ||
                uri.contains("/api/auth/trainer/register") ||
                uri.contains("/api/auth/login") ||
                uri.contains("/swagger") ||
                uri.contains("/v3/api-docs")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring(6);
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);

                String[] values = credentials.split(":", 2);
                String username = values[0];
                String password = values[1];

                if (authenticationService.authenticate(username, password)) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("Error decoding Basic Auth header", e);
            }
        }

        logger.warn("Authentication failed for URI: {}", uri);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: Invalid username or password");
        return false;
    }
}