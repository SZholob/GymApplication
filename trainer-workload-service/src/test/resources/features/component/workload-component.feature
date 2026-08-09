Feature: Trainer Workload Component Testing
  As a workload microservice
  I want to process messages from ActiveMQ
  So that trainer statistics are correctly updated in MongoDB

  Scenario: Valid message successfully updates MongoDB
    Given the MongoDB database is empty
    When a valid message to ADD 60 minutes for "John.Doe" on "2026-08-20" is received
    Then a new document for "John.Doe" should be created in MongoDB
    And the total duration for "John.Doe" in 2026 month 8 should be 60

  Scenario: Invalid message is ignored and sent to DLQ
    Given the MongoDB database is empty
    When an invalid message with missing username is received
    Then no document should be created in MongoDB
    And the message should be forwarded to the DLQ "workload.dlq"

  Scenario: Successfully decrease workload (ActionType.DELETE)
    Given the MongoDB database has a trainer "Jane.Smith" with 100 minutes in 2026 month 8
    When a valid message to DELETE 40 minutes for "Jane.Smith" on "2026-08-20" is received
    Then the total duration for "Jane.Smith" in 2026 month 8 should be 60

  Scenario: Workload does not drop below zero when deleting more hours than exist
    Given the MongoDB database has a trainer "Jane.Smith" with 30 minutes in 2026 month 8
    When a valid message to DELETE 50 minutes for "Jane.Smith" on "2026-08-20" is received
    Then the total duration for "Jane.Smith" in 2026 month 8 should be 0

  Scenario: Invalid message with missing training date is sent to DLQ
    Given the MongoDB database is empty
    When an invalid message with missing training date is received
    Then no document should be created in MongoDB
    And the message should be forwarded to the DLQ "workload.dlq"