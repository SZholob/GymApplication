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

  Scenario: Fail to add training when duration is negative
    Given a valid trainee "John.Doe" and trainer "Jane.Smith" exist
    When I send a POST request to add a training for "John.Doe" and "Jane.Smith" with negative duration -50
    Then the response status should be 400 Bad Request
    And no message should be sent to ActiveMQ

  Scenario: Fail to access secured endpoint without JWT token
    Given a valid trainee "John.Doe" and trainer "Jane.Smith" exist
    When I send a GET request to retrieve trainer "Jane.Smith" without a token
    Then the response status should be 401 Unauthorized

  Scenario: Fail to authenticate with incorrect password
    Given a valid trainee "John.Doe" and trainer "Jane.Smith" exist
    When I try to login as "John.Doe" with wrong password "wrongPass"
    Then the response status should be 401 Unauthorized