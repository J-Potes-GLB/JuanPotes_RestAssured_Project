package com.jp.api.stepDefinitions;

import com.jp.api.models.Resource;
import com.jp.api.requests.ResourceRequest;
import com.jp.api.utils.Constants;
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

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resourceResponded;
    private Resource resourceGiven;
    private List<String> createdResourcesIds = new ArrayList<String>();

    @Given("there are at least {int} registered resource on the system")
    public void thereAreRegisteredResourcesOnTheSystem(int minNumberOfResourcesRegistered){
        response = resourceRequest.getResources();
        List<Resource> resourcesRegistered = resourceRequest.getResourcesEntity(response);
        int size = resourcesRegistered.size();
        logger.info("Minimum number of registered resources required: " + minNumberOfResourcesRegistered);
        logger.info("Number of original registered resources: " + size);

        Response auxiliarResponse;
        Resource auxiliarResource;
        if(size < minNumberOfResourcesRegistered){
            int resourcesToAdd = minNumberOfResourcesRegistered - size;
            for(int i = 0; i < resourcesToAdd; i++){
                auxiliarResponse = resourceRequest.createTestResource();
                auxiliarResource = resourceRequest.getResourceEntity(auxiliarResponse);
                createdResourcesIds.add(auxiliarResource.getId());
                logger.info("New resource of id '" + auxiliarResource.getId() + "' was created");
            }
        }
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
        logger.info("The GET request was sent");
        logger.info(response.print());
    }

    @Then("the response of resource should have a status code of {int}")
    public void theResponseOfResourceShouldHaveAStatusCodeOf(int expectedStatusCode) {
        logger.info("Expected Status code: " + expectedStatusCode + "    Response Status code: " + response.getStatusCode());
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }
    
    @And("validates the response with resources list JSON schema")
    public void validatesTheResponseWithResourcesListJSONSchema() {
        Assert.assertTrue(resourceRequest.validateSchema(response, Constants.PATH_RESOURCE_LIST_SCHEMA));
        logger.info("The schema was validated successfully");
    }

    @And("deletes the created resources")
    public void deletesTheCreatedResources() {
        if(!createdResourcesIds.isEmpty()){
            logger.info("CLEAN UP OF CREATED RESOURCES");
            int size = createdResourcesIds.size();
            for(int i = 0; i < size; i++){
                response = resourceRequest.deleteResource(createdResourcesIds.get(0));
                resourceResponded = resourceRequest.getResourceEntity(response);

                createdResourcesIds.remove(0);

                logger.info("Resource of id '"  + resourceResponded.getId() + "' was deleted successfully");
            }
        }
    }
}
