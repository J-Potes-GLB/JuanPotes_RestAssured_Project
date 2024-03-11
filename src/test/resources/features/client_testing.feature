@active
Feature: Client testing CRUD

  @smoke
  Scenario: Get the list of clients
    Given there are at least 3 registered clients on the system
    When I send a GET request to view all the clients
    Then the response should have a status code of 200
    And validates the response with client list JSON schema
