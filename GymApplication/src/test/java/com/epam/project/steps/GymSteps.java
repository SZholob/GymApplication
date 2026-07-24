package com.epam.project.steps;

import com.epam.project.actuator.GymMetrics;
import com.epam.project.dao.TraineeDao;
import com.epam.project.dao.TrainerDao;
import com.epam.project.dao.TrainingDao;
import com.epam.project.dto.AddTrainingRequest;
import com.epam.project.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class GymSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private GymMetrics gymMetrics;

    @Autowired
    private JmsTemplate jmsTemplate;

    private ResultActions resultActions;

    @Given("a valid trainee {string} and trainer {string} exist")
    public void setup_users(String traineeName, String trainerName) {


        User traineeUser = new User();
        traineeUser.setUsername(traineeName);

        Trainee dummyTrainee = new Trainee();
        dummyTrainee.setUser(traineeUser);
        dummyTrainee.setTrainers(new ArrayList<>());


        User trainerUser = new User();
        trainerUser.setUsername(trainerName);
        trainerUser.setFirstName("Jane");
        trainerUser.setLastName("Smith");
        trainerUser.setIsActive(true);

        TrainingType type = new TrainingType();
        type.setTrainingTypeName("Yoga");

        Trainer dummyTrainer = new Trainer();
        dummyTrainer.setUser(trainerUser);
        dummyTrainer.setSpecialization(type);
        dummyTrainer.setTrainees(new ArrayList<>());


        Training savedTraining = new Training();
        savedTraining.setTrainingType(type);


        when(traineeDao.findByUsername(traineeName)).thenReturn(Optional.of(dummyTrainee));
        when(trainerDao.findByUsername(trainerName)).thenReturn(Optional.of(dummyTrainer));
        when(trainingDao.save(any(Training.class))).thenReturn(savedTraining);
    }

    @When("I send a POST request to add a training for {string} and {string} with duration {int}")
    public void send_valid_request(String trainee, String trainer, int duration) throws Exception {
        AddTrainingRequest request = new AddTrainingRequest(
                trainee, trainer, "Morning Yoga", LocalDate.now(), duration
        );

        resultActions = mockMvc.perform(post("/api/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @When("I send a POST request to add a training with missing duration")
    public void send_invalid_request() throws Exception {

        AddTrainingRequest request = new AddTrainingRequest(
                "John.Doe", "Jane.Smith", "Morning Yoga", LocalDate.now(), null
        );

        resultActions = mockMvc.perform(post("/api/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("the response status should be {int} OK")
    public void verify_status_ok(int status) throws Exception {
        resultActions.andExpect(status().is(status));
    }

    @Then("the response status should be {int} Bad Request")
    public void verify_status_bad(int status) throws Exception {
        resultActions.andExpect(status().is(status));
    }

    @Then("a message should be sent to the ActiveMQ queue {string}")
    public void verify_message_sent(String queueName) {
        verify(jmsTemplate, times(1))
                .convertAndSend(eq(queueName), any(Object.class), any(MessagePostProcessor.class));
    }

    @Then("no message should be sent to ActiveMQ")
    public void verify_no_message_sent() {
        verify(jmsTemplate, never())
                .convertAndSend(anyString(), any(Object.class), any(MessagePostProcessor.class));
    }
}