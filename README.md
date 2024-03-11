# JuanPotes_RestAssured_Project

This project is the final deliverable of the Test Automation Training from Globant.


## Description

In this API automation project the objective was to practice API testing using RestAssured and Cucumber.
The framework template was provided by the tutor *Manuel Muñoz*. It is a maven project with the respective dependencies of RestAssured, Cucumber, Lombok and JUnit.


## Test Cases

For this project, 4 test cases were implemented divided into 2 features, one for clients and the other one for resources. 
API base URL: "https://63b6dfe11907f863aa04ff81.mockapi.io"

### Scenario 1: Get the list of clients

**Given** there are at least 3 registered clients on the system.

**When** I send a GET request to view all the clients.

**Then** the response should have a status code of 200.

**And** validates the response with client list JSON schema.


### Scenario 2: Get the list of resources

**Given** there are at least 1 registered resources on the system

**When** I send a GET request to view all the resources

**Then** the response of resource should have a status code of 200

**And** validates the response with resources list JSON schema


### Scenario 3: Create a new client

**Given** I have a client with the following details:<br>
      | Name | LastName | Country  | City   | Email           | Phone       |<br>
      | Juan | Potes    | Colombia | Bogota | testJP@test.com | 123-456-789 |

**When** I send a POST request to create a client

**Then** the response should have a status code of 201

**And** the response details should be the same as the client

**And** validates the response with the client JSON schema


### Scenario 4: Update the last resource

**Given** there are at least 5 registered resources on the system

**And** I retrieve the details of the latest resource

**When** I send a PUT request to update the latest resource with the following details:<br>
      | name        | trademark | stock | price | description   | tags    | active |<br>
      | JP Resource | Globant   | 20    | 50.25 | Test resource | quality | false  |

**Then** the response of resource should have a status code of 200

**And** the response should have the same resource details that were sent

**And** validates the response with resources JSON schema


## How to run the project

After the repository is cloned, go to the src -> test -> java -> com.jp.api -> runner -> *TestRunner.java* and run the program.
If you get some issues with the execution, make sure the maven dependencies are fully installed running the command "mvn clean install" on the console.


## Additional notes

Some test cases had prerequisites like a minimum number of registered elements in the Given statements, in the cases where the elements alredy in the API were not enough, the program will create the records to satisfy the condition and execute the test, but every new record created in the test it's deleted at the end, in order to clean the execution, and to keep the tests reusable.
