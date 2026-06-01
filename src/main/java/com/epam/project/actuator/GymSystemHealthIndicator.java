package com.epam.project.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GymSystemHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        boolean isGymSystemHealthy = true;

        if (isGymSystemHealthy) {
            return Health.up()
                    .withDetail("Gym Business Logic", "Running smoothly")
                    .withDetail("Active Trainers Available", 5)
                    .build();
        } else {
            return Health.down()
                    .withDetail("Gym Business Logic", "Service is degraded or unavailable")
                    .build();
        }
    }
}