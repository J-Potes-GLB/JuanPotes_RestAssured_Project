package com.jp.api.stepDefinitions;

import com.jp.api.models.Client;
import com.jp.api.models.Resource;
import com.jp.api.requests.ClientRequest;
import com.jp.api.requests.ResourceRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resourceResponded;
    private Resource resourceGiven;
    private List<String> createdResourcesIds = new ArrayList<String>();

    @Given("there are at least {int} registered resource on the system")
    public void thereAreRegisteredResourcesOnTheSystem(int minNumberOfResourcesRegistered){
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
    }
    
    @And("validates the response with resources list JSON schema")
    public void validatesTheResponseWithResourcesListJSONSchema() {
    }

    @And("deletes the created resources")
    public void deletesTheCreatedResources() {
    }
}
