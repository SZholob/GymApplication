package com.epam.project.client;

import com.epam.project.dto.WorkloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkloadFeignClientFallback implements WorkloadFeignClient {

    @Override
    public void updateWorkload(String transactionId, WorkloadRequest request) {
        log.error("⚡ CIRCUIT BREAKER ВІДКРИТО! Мікросервіс навантаження недоступний.");
        log.error("⚡ Дані для {} не відправлено. План Б. TxID: {}", request.trainerUsername(), transactionId);
    }
}