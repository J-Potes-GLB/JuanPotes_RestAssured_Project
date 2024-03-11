package com.jp.api.stepDefinitions;

import com.jp.api.models.Client;
import com.jp.api.requests.ClientRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client client;

    @Given("there are at least {int} registered clients on the system")
    public void thereAreRegisteredClientsOnTheSystem(int numberOfClientsRegistered){
        logger.info("there are at least " + numberOfClientsRegistered + " registered clients in the system");
    }

    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClients() {
        logger.info("I send a GET request to view all the clients");
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int expectedStatusCode) {
        logger.info("the response should have a status code of " + expectedStatusCode);
    }

    @And("validates the response with client list JSON schema")
    public void validatesTheResponseWithClientListJSONSchema() {
        logger.info("validates the response with client list JSON schema");
    }
}
