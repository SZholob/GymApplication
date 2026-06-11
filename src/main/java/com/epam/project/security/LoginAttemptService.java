package com.epam.project.security;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 5;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
        lockCache.remove(username);
    }

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockCache.put(username, LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
        }
    }

    public boolean isBlocked(String username) {
        if (lockCache.containsKey(username)) {
            if (LocalDateTime.now().isAfter(lockCache.get(username))) {
                attemptsCache.remove(username);
                lockCache.remove(username);
                return false;
            }
            return true;
        }
        return false;
    }

    public int getAttempts(String username) {
        return attemptsCache.getOrDefault(username, 0);
    }
}
