package com.epam.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Set;

public class UserUtils {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public static String generateUsername(String firstName, String lastName, Set<String> existingUsernames) {
        String base = firstName + "." + lastName;
        if (!existingUsernames.contains(base)) {
            return base;
        }

        int maxSuffix = findMaxSuffix(base, existingUsernames);

        return base + (maxSuffix + 1);
    }

    private static int findMaxSuffix(String base, Set<String> existingUsernames) {
        int maxSuffix = 0;

        for (String username : existingUsernames) {
            if (username.startsWith(base)) {
                String suffix = username.substring(base.length());
                if (!suffix.isEmpty()) {
                    if (suffix.matches("\\d+")) {
                        int num = Integer.parseInt(suffix);
                        if (num > maxSuffix) {
                            maxSuffix = num;
                        }
                    } else {
                        logger.debug("Found non-numeric suffix '{}' for base '{}'. Ignoring in max suffix calculation.", suffix, base);
                    }
                }
            }
        }
        return maxSuffix;
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
