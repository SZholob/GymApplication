package com.epam.project.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GymMetrics {

    private final MeterRegistry meterRegistry;
    private final Timer trainingCreationTimer;

    public GymMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.trainingCreationTimer = Timer.builder("gym_training_creation_time")
                .description("Time taken to process and save a new training")
                .register(meterRegistry);

    }


    public void incrementTrainingCount(String trainingType) {
        meterRegistry.counter("gym_trainings_created_total", "type", trainingType).increment();
    }


    public void recordTrainingCreationTime(long durationInMilliseconds) {
        trainingCreationTimer.record(durationInMilliseconds, TimeUnit.MILLISECONDS);
    }
}