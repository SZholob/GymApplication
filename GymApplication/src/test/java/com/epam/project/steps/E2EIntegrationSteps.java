package com.epam.project.steps;

import com.epam.project.dto.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class E2EIntegrationSteps {

    private final RestTemplate restTemplate = new RestTemplate();

    private String monolithUrl;
    private String workloadUrl;

    private String trainerUsername;
    private String traineeUsername;
    private String jwtToken;

    private ResponseEntity<String> trainingResponse;

    @Given("the monolith is running on {string} and workload service on {string}")
    public void setup_urls(String monolith, String workload) {
        this.monolithUrl = monolith;
        this.workloadUrl = workload;
    }

    @Given("I register a new trainer and trainee to get a valid JWT token")
    public void register_users() {

        TrainerRegistrationRequest trainerReq = new TrainerRegistrationRequest("EndToEnd", "Trainer", "YOGA");
        ResponseEntity<RegistrationResponse> trainerRes = restTemplate.postForEntity(
                monolithUrl + "/api/auth/trainer/register", trainerReq, RegistrationResponse.class);

        this.trainerUsername = trainerRes.getBody().username();
        this.jwtToken = trainerRes.getBody().token();


        TraineeRegistrationRequest traineeReq = new TraineeRegistrationRequest("EndToEnd", "Trainee", null, null);
        ResponseEntity<RegistrationResponse> traineeRes = restTemplate.postForEntity(
                monolithUrl + "/api/auth/trainee/register", traineeReq, RegistrationResponse.class);

        this.traineeUsername = traineeRes.getBody().username();
    }

    @When("I send a POST request to create a {int}-minute training session for them")
    public void create_training(int duration) {
        AddTrainingRequest request = new AddTrainingRequest(
                traineeUsername, trainerUsername, "E2E Integration Test", LocalDate.now(), duration
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<AddTrainingRequest> entity = new HttpEntity<>(request, headers);

        trainingResponse = restTemplate.postForEntity(
                monolithUrl + "/api/trainings", entity, String.class);
    }

    @Then("the monolith should return a successful response")
    public void verify_monolith_response() {
        assertTrue(trainingResponse.getStatusCode().is2xxSuccessful(),
                "Моноліт мав повернути статус 200 OK");
    }

    @Then("within {int} seconds, the workload service should show exactly {int} minutes for this trainer")
    public void verify_workload_service(int timeoutSeconds, int expectedDuration) throws InterruptedException {
        int actualDuration = 0;
        boolean isUpdated = false;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        for (int i = 0; i < timeoutSeconds; i++) {
            try {
                ResponseEntity<TrainerWorkloadResponse> response = restTemplate.exchange(
                        workloadUrl + "/api/workloads/" + trainerUsername,
                        org.springframework.http.HttpMethod.GET,
                        entity,
                        TrainerWorkloadResponse.class
                );

                TrainerWorkloadResponse body = response.getBody();
                if (body != null && body.years() != null && !body.years().isEmpty()) {
                    actualDuration = body.years().get(0).months().get(0).trainingSummaryDuration();

                    if (actualDuration == expectedDuration) {
                        isUpdated = true;
                        break;
                    }
                }
            } catch (Exception e) {

                System.out.println(" Очікування мікросервісу... (" + e.getMessage() + ")");
            }

            Thread.sleep(1000);
        }

        assertTrue(isUpdated, "Мікросервіс навантажень не оновив години! Очікувалося: "
                + expectedDuration + ", але було: " + actualDuration);
    }
}

record TrainerWorkloadResponse(
        String username,
        String firstName,
        String lastName,
        Boolean isActive,
        java.util.List<YearWorkload> years
) {
}

record YearWorkload(
        Integer year,
        java.util.List<MonthWorkload> months
) {
}

record MonthWorkload(
        Integer month,
        Integer trainingSummaryDuration
) {
}
