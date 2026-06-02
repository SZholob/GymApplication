package com.epam.project.actuator;

import com.epam.project.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GymSystemHealthIndicator implements HealthIndicator {

    private final TrainerService trainerService;

    @Override
    public Health health() {
        try {
            long totalTrainers = trainerService.findAllTrainers().size();

            if (totalTrainers > 0) {
                return Health.up()
                        .withDetail("Gym Business Logic", "Running smoothly")
                        .withDetail("Total Trainers in DB", totalTrainers)
                        .build();
            } else {
                return Health.down()
                        .withDetail("Gym Business Logic", "No trainers found in the system")
                        .build();
            }
        } catch (Exception e) {
            return Health.down(e)
                    .withDetail("Gym Business Logic", "Database connection failed during health check")
                    .build();
        }
    }
}