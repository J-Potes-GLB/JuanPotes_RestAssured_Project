package com.jp.api.stepDefinitions;

import com.jp.api.models.Client;
import com.jp.api.requests.ClientRequest;
import com.jp.api.utils.Constants;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client clientResponded;
    private Client clientGiven;
    private List<String> createdClientsIds = new ArrayList<String>();

    @Given("there are at least {int} registered clients on the system")
    public void thereAreRegisteredClientsOnTheSystem(int minNumberOfClientsRegistered){
        response = clientRequest.getClients();
        List<Client> clientsRegistered = clientRequest.getClientsEntity(response);
        int size = clientsRegistered.size();
        logger.info("Minimum number of registered clients required: " + minNumberOfClientsRegistered);
        logger.info("Number of original registered clients: " + size);

        Response auxiliarResponse;
        Client auxiliarClient;
        if(size < minNumberOfClientsRegistered){
            int clientsToAdd = minNumberOfClientsRegistered - size;
            for(int i = 0; i < clientsToAdd; i++){
                auxiliarResponse = clientRequest.createTestClient();
                auxiliarClient = clientRequest.getClientEntity(auxiliarResponse);
                createdClientsIds.add(auxiliarClient.getId());
                logger.info("New client of id '" + auxiliarClient.getId() + "' was created");
            }
        }
    }

    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClients() {
        response = clientRequest.getClients();
        logger.info("The GET request was sent");
        logger.info(response.print());
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int expectedStatusCode) {
        logger.info("Expected Status code: " + expectedStatusCode + "    Response Status code: " + response.getStatusCode());
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @And("validates the response with client list JSON schema")
    public void validatesTheResponseWithClientListJSONSchema() {
        Assert.assertTrue(clientRequest.validateSchema(response, Constants.PATH_CLIENT_LIST_SCHEMA));
        logger.info("The schema was validated successfully");
    }

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);

        clientGiven = new Client(clientDataMap.get("Name"),
                clientDataMap.get("LastName"),
                clientDataMap.get("Country"),
                clientDataMap.get("City"),
                clientDataMap.get("Email"),
                clientDataMap.get("Phone"));

        logger.info("Client to add: " + clientGiven);
    }

    @When("I send a POST request to create a client")
    public void iSendAPOSTRequestToCreateAClient() {
        response = clientRequest.createClient(clientGiven);
        logger.info("New client created");

        clientResponded = clientRequest.getClientEntity(response);
        logger.info("Client in response: " + clientResponded);

        createdClientsIds.add(clientResponded.getId());
    }

    @And("the response details should be the same as the client")
    public void theResponseDetailsShouldBeTheSameAsTheClient() {
        Assert.assertEquals(clientGiven.getName(), clientResponded.getName());
        Assert.assertEquals(clientGiven.getLastName(), clientResponded.getLastName());
        Assert.assertEquals(clientGiven.getCountry(), clientResponded.getCountry());
        Assert.assertEquals(clientGiven.getCity(), clientResponded.getCity());
        Assert.assertEquals(clientGiven.getEmail(), clientResponded.getEmail());
        Assert.assertEquals(clientGiven.getPhone(), clientResponded.getPhone());

        logger.info("The client details in the response are the same as the details sent");
    }

    @And("validates the response with the client JSON schema")
    public void validatesTheResponseWithTheClientJSONSchema() {
        Assert.assertTrue(clientRequest.validateSchema(response, Constants.PATH_CLIENT_SCHEMA));
        logger.info("The schema was validated successfully");
    }


    @And("deletes the created clients")
    public void deletesTheCreatedClients() {
        if(!createdClientsIds.isEmpty()){
            logger.info("CLEAN UP OF CREATED CLIENTS");
            int size = createdClientsIds.size();
            for(int i = 0; i < size; i++){
                response = clientRequest.deleteClient(createdClientsIds.get(0));
                clientResponded = clientRequest.getClientEntity(response);

                createdClientsIds.remove(0);

                logger.info("Client of id '"  + clientResponded.getId() + "' was deleted successfully");
            }
        }
    }
}
