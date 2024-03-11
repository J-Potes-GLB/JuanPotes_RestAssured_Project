@active
Feature: Client testing CRUD

  # Test Case 1
  @smoke
  Scenario: Get the list of clients
    Given there are at least 3 registered clients on the system
    When I send a GET request to view all the clients
    Then the response should have a status code of 200
    And validates the response with client list JSON schema

  # Test case 3
  @smoke
  Scenario: Create a new client
    Given I have a client with the following details:
      | Name | LastName | Country  | City   | Email           | Phone       |
      | Juan | Potes    | Colombia | Bogota | testJP@test.com | 123-456-789 |
    When I send a POST request to create a client
    Then the response should have a status code of 201
    And the response details should be the same as the client
    And validates the response with the client JSON schema
    And deletes the created clients