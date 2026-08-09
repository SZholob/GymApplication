package com.epam.project.steps;

import com.epam.project.actuator.GymMetrics;
import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class CucumberSpringConfiguration {
    @MockBean
    private TraineeDao traineeDao;

    @MockBean
    private TrainerDao trainerDao;

    @MockBean
    private TrainingDao trainingDao;

    @MockBean
    private GymMetrics gymMetrics;

    @MockBean
    private JmsTemplate jmsTemplate;
}