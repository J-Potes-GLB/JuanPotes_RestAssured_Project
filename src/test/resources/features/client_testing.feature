@active
Feature: Client testing CRUD

  Scenario: Get the list of clients
    Given there are registered clients on the system 3
    When I send a GET request to view all the clients
    Then the response should have a status code of 200
    And validates the response with client list JSON schema
