package com.epam.project.listener;

import com.epam.project.dto.WorkloadRequest;
import com.epam.project.service.TrainerWorkloadService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadMessageListener {

    private final TrainerWorkloadService workloadService;
    private final JmsTemplate jmsTemplate;
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId";
    private static final String DLQ_QUEUE_NAME = "workload.dlq";


    @JmsListener(destination = "workload.queue")
    public void receiveWorkloadMessage(WorkloadRequest request, Message message) {
        try {

            String txId = message.getStringProperty("X-Transaction-ID");
            if (txId != null) {
                MDC.put(MDC_TRANSACTION_ID_KEY, txId);
            }

            if (request.trainerUsername() == null || request.trainerUsername().isBlank() ||
                    request.actionType() == null ||
                    request.trainingDate() == null ||
                    request.trainingDuration() == null) {

                log.error("An invalid message was received (required data is missing). Move to DLQ: {}", DLQ_QUEUE_NAME);


                jmsTemplate.convertAndSend(DLQ_QUEUE_NAME, request);
                return;
            }

            log.info("Received message from ActiveMQ: Load update for {}", request.trainerUsername());

            workloadService.updateWorkload(request);

        } catch (JMSException e) {
            log.error("Error reading message properties: {}", e.getMessage());
        } finally {

            MDC.remove(MDC_TRANSACTION_ID_KEY);
        }
    }
}