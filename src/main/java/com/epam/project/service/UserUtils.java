package com.epam.project.service;

import java.security.SecureRandom;
import java.util.Set;

public class UserUtils {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    public static String generateUsername(String firstName, String lastName, Set<String> existingUsernames) {
        String base = firstName + "." + lastName;
        String username = base;
        int count = 1;
        while (existingUsernames.contains(username)) {
            username = base + count;
            count++;
        }
        return username;
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(index));
        }
        return password.toString();
    }
}
