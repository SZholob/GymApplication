package com.epam.project.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.jms.ConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class CucumberSpringConfiguration {

    @MockBean
    private JmsTemplate jmsTemplate;

    @TestConfiguration
    static class MockJmsConfig {

        @Bean(name = "jmsListenerContainerFactory")
        public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
            JmsListenerContainerFactory<MessageListenerContainer> factory = mock(JmsListenerContainerFactory.class);
            MessageListenerContainer container = mock(MessageListenerContainer.class);

            when(factory.createListenerContainer(any(JmsListenerEndpoint.class))).thenReturn(container);

            return factory;
        }
    }
}