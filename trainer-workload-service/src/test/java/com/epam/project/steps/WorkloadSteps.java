package com.epam.project.steps;

import com.epam.project.dto.ActionType;
import com.epam.project.dto.WorkloadRequest;
import com.epam.project.listener.WorkloadMessageListener;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.repository.TrainerWorkloadRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkloadSteps {

    @Autowired
    private TrainerWorkloadRepository repository;

    @Autowired
    private WorkloadMessageListener workloadMessageListener;

    @Autowired
    private JmsTemplate jmsTemplate;


    @Given("the MongoDB database is empty")
    public void the_mongodb_database_is_empty() {
        repository.deleteAll();
    }

    @When("a valid message to ADD {int} minutes for {string} on {string} is received")
    public void a_valid_message_is_received(int duration, String username, String date) throws JMSException {
        WorkloadRequest request = new WorkloadRequest(
                username, "John", "Doe", true, LocalDate.parse(date), duration, ActionType.ADD
        );

        Message mockMessage = mock(Message.class);
        when(mockMessage.getStringProperty("X-Transaction-ID")).thenReturn("test-tx-123");

        workloadMessageListener.receiveWorkloadMessage(request, mockMessage);
    }

	@Given("the MongoDB database has a trainer {string} with {int} minutes in {int} month {int}")
	public void setup_existing_trainer(String username, int duration, int year, int month) {
		repository.deleteAll(); // Очищаємо перед тестом

		com.epam.project.model.Month monthObj = new com.epam.project.model.Month(month, duration);
		com.epam.project.model.Year yearObj = new com.epam.project.model.Year(year, new java.util.ArrayList<>(java.util.List.of(monthObj)));

		TrainerWorkload workload = TrainerWorkload.builder()
			.username(username)
			.firstName("Jane")
			.lastName("Smith")
			.isActive(true)
			.years(new java.util.ArrayList<>(java.util.List.of(yearObj)))
			.build();

		repository.save(workload);
	}

	@When("a valid message to DELETE {int} minutes for {string} on {string} is received")
	public void a_valid_delete_message_is_received(int duration, String username, String date) throws jakarta.jms.JMSException {
		WorkloadRequest request = new WorkloadRequest(
			username, "Jane", "Smith", true, LocalDate.parse(date), duration, ActionType.DELETE
		);

		Message mockMessage = mock(Message.class);
		when(mockMessage.getStringProperty("X-Transaction-ID")).thenReturn("test-tx-delete");
		workloadMessageListener.receiveWorkloadMessage(request, mockMessage);
	}

	@When("an invalid message with missing training date is received")
	public void invalid_message_missing_date() throws jakarta.jms.JMSException {
		// null замість дати
		WorkloadRequest request = new WorkloadRequest(
			"Jane.Smith", "Jane", "Smith", true, null, 60, ActionType.ADD
		);

		Message mockMessage = mock(Message.class);
		workloadMessageListener.receiveWorkloadMessage(request, mockMessage);
	}

    @When("an invalid message with missing username is received")
    public void an_invalid_message_is_received() throws JMSException {
        WorkloadRequest request = new WorkloadRequest(
                null, "John", "Doe", true, LocalDate.now(), 60, ActionType.ADD
        );

        Message mockMessage = mock(Message.class);
        workloadMessageListener.receiveWorkloadMessage(request, mockMessage);
    }

    @Then("a new document for {string} should be created in MongoDB")
    public void a_new_document_should_be_created(String username) {
        Optional<TrainerWorkload> workload = repository.findByUsername(username);
        assertTrue(workload.isPresent(), "The document should exist in MongoDB");
    }

    @Then("the total duration for {string} in {int} month {int} should be {int}")
    public void the_total_duration_should_be(String username, int year, int month, int expectedDuration) {
        TrainerWorkload workload = repository.findByUsername(username).get();

        int actualDuration = workload.getYears().stream()
                .filter(y -> y.getYear() == year)
                .flatMap(y -> y.getMonths().stream())
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .get()
                .getTrainingSummaryDuration();

        assertEquals(expectedDuration, actualDuration);
    }

    @Then("no document should be created in MongoDB")
    public void no_document_should_be_created() {
        assertEquals(0, repository.count(), "The database should remain empty.");
    }

    @Then("the message should be forwarded to the DLQ {string}")
    public void the_message_should_be_forwarded_to_dlq(String dlqName) {
        verify(jmsTemplate, times(1)).convertAndSend(eq(dlqName), any(WorkloadRequest.class));
    }
}