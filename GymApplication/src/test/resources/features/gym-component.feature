Feature: Gym Monolith Component Testing
  As a front-end client
  I want to add a training session via REST API
  So that it is saved and the workload service is notified

  #
  Scenario: Successfully add a valid training
    Given a valid trainee "John.Doe" and trainer "Jane.Smith" exist
    When I send a POST request to add a training for "John.Doe" and "Jane.Smith" with duration 60
    Then the response status should be 200 OK
    And a message should be sent to the ActiveMQ queue "workload.queue"

  #
  Scenario: Fail to add a training due to missing duration
    Given a valid trainee "John.Doe" and trainer "Jane.Smith" exist
    When I send a POST request to add a training with missing duration
    Then the response status should be 400 Bad Request
    And no message should be sent to ActiveMQ