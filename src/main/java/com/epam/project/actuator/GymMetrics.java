package com.epam.project.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class GymMetrics {

    private final Counter trainingCreationCounter;

    public GymMetrics(MeterRegistry meterRegistry) {
        this.trainingCreationCounter = Counter.builder("gym_training_created_total")
                .description("Total number of trainings created in the system")
                .register(meterRegistry);
    }

    public void incrementTrainingCount() {
        trainingCreationCounter.increment();
    }
}