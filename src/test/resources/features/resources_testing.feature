@active
Feature: Resources testing CRUD

  # Test Case 2
  @smoke
  Scenario: Get the list of resources
    Given there are at least 1 registered resource on the system
    When I send a GET request to view all the resources
    Then the response of resource should have a status code of 200
    And validates the response with resources list JSON schema
    And deletes the created resources