package com.epam.project.client;

import com.epam.project.dto.WorkloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkloadFeignClientFallback implements WorkloadFeignClient {

    @Override
    public void updateWorkload(WorkloadRequest request) {

        log.error("============= CIRCUIT BREAKER OPEN! Load microservice unavailable.");
        log.error("============= Training data for {} temporarily not sent. Plan B activated.", request.trainerUsername());

    }
}