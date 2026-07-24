Feature: End-to-End Microservices Integration
  As a client application
  I want to create a training session in the monolith
  So that the ActiveMQ message is sent and the workload microservice automatically updates the database

  Scenario: Successfully process training across microservices
    Given the monolith is running on "http://localhost:8080" and workload service on "http://localhost:8081"
    And I register a new trainer and trainee to get a valid JWT token
    When I send a POST request to create a 90-minute training session for them
    Then the monolith should return a successful response
    And within 10 seconds, the workload service should show exactly 90 minutes for this trainer


  Scenario: Fail to process training due to invalid data (Negative Scenario)
    Given the monolith is running on "http://localhost:8080" and workload service on "http://localhost:8081"
    And I register a new trainer and trainee to get a valid JWT token
    When I send a POST request with invalid trainee username "Ghost.User"
    Then the monolith should return an error response
    And the workload service should not have any records for this trainer