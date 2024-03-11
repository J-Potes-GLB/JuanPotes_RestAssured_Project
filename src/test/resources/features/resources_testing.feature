@active
Feature: Resources testing CRUD

  # Test Case 2
  @smoke
  Scenario: Get the list of resources
    Given there are at least 1 registered resources on the system
    When I send a GET request to view all the resources
    Then the response of resource should have a status code of 200
    And validates the response with resources list JSON schema
    And deletes the created resources

  # Test Case 4
  @smoke
  Scenario: Update the last resource
    Given there are at least 5 registered resources on the system
    And I retrieve the details of the latest resource
    When I send a PUT request to update the latest resource with the following details:
      | name        | trademark | stock | price | description   | tags    | active |
      | JP Resource | Globant   | 20    | 50.25 | Test resource | quality | false  |
    Then the response of resource should have a status code of 200
    And the response should have the same resource details that were sent
    And validates the response with resources JSON schema
    And deletes the created resources