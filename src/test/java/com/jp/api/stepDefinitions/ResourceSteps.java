package com.jp.api.stepDefinitions;

import com.jp.api.models.Resource;
import com.jp.api.requests.ResourceRequest;
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

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resourceResponded;
    private Resource resourceGiven;
    private Resource lastResource;
    private List<String> createdResourcesIds = new ArrayList<String>();

    @Given("there are at least {int} registered resources on the system")
    public void thereAreRegisteredResourcesOnTheSystem(int minNumberOfResourcesRegistered){
        // Gets the response of the list of resources
        response = resourceRequest.getResources();
        List<Resource> resourcesRegistered = resourceRequest.getResourcesEntity(response);

        int size = resourcesRegistered.size();

        logger.info("Minimum number of registered resources required: " + minNumberOfResourcesRegistered);
        logger.info("Number of original registered resources: " + size);

        Response auxiliarResponse;
        Resource auxiliarResource;

        // If the number of resources is lower than the minimum number allowed, then
        // it creates temporary resources to complete the minimum number.
        if(size < minNumberOfResourcesRegistered){
            int resourcesToAdd = minNumberOfResourcesRegistered - size;
            for(int i = 0; i < resourcesToAdd; i++){
                // Get the response of the creation of a resource and save it
                auxiliarResponse = resourceRequest.createTestResource();
                auxiliarResource = resourceRequest.getResourceEntity(auxiliarResponse);

                // Save the id of the created resource on a list to delete that resource at the end
                createdResourcesIds.add(auxiliarResource.getId());
                logger.info("New resource of id '" + auxiliarResource.getId() + "' was created");
            }
        }
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        // Get and save the response of the list of resources
        response = resourceRequest.getResources();

        // Log all the resources
        logger.info("The GET request was sent");
        logger.info(response.print());
    }

    @Then("the response of resource should have a status code of {int}")
    public void theResponseOfResourceShouldHaveAStatusCodeOf(int expectedStatusCode) {
        // Verify that the expected status code is the same as the response
        logger.info("Expected Status code: " + expectedStatusCode + "    Response Status code: " + response.getStatusCode());
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }
    
    @And("validates the response with resources list JSON schema")
    public void validatesTheResponseWithResourcesListJSONSchema() {
        // Validate Schema
        Assert.assertTrue(resourceRequest.validateSchema(response, Constants.PATH_RESOURCE_LIST_SCHEMA));
        logger.info("The schema was validated successfully");
    }

    @And("deletes the created resources")
    public void deletesTheCreatedResources() {
        // Check that the list of Ids is not empty which means some resource was created in the test
        if(!createdResourcesIds.isEmpty()){
            logger.info("CLEAN UP OF CREATED RESOURCES");
            int size = createdResourcesIds.size();

            // The loop gets executed for every resource that was created in the test
            for(int i = 0; i < size; i++){
                // Delete first resource of the new resources and serialize response
                response = resourceRequest.deleteResource(createdResourcesIds.get(0));
                resourceResponded = resourceRequest.getResourceEntity(response);

                // remove the id of the already deleted client
                createdResourcesIds.remove(0);

                logger.info("Resource of id '"  + resourceResponded.getId() + "' was deleted successfully");
            }
        }
    }

    @And("I retrieve the details of the latest resource")
    public void iRetrieveTheDetailsOfTheLatestResource() {
        // Get the list of resources, save and serialize the responce on a list
        response = resourceRequest.getResources();
        List<Resource> resourcesRegistered = resourceRequest.getResourcesEntity(response);

        // Save the las resource on the list
        lastResource = resourcesRegistered.get(resourcesRegistered.size() - 1);

        logger.info("Original details: " + lastResource);
    }

    @When("I send a PUT request to update the latest resource with the following details:")
    public void iSendAPUTRequestToUpdateTheLatestResourceWithTheFollowingDetails(DataTable clientData) {
        // Save the parameter data from the gherkin as a Map
        Map<String, String> resourceDataMap = clientData.asMaps().get(0);

        // Save the data of the Map in a new Resource object
        resourceGiven = new Resource(resourceDataMap.get("name"),
                resourceDataMap.get("trademark"),
                Integer.parseInt(resourceDataMap.get("stock")),
                Double.parseDouble(resourceDataMap.get("price")),
                resourceDataMap.get("description"),
                resourceDataMap.get("tags"),
                Boolean.parseBoolean(resourceDataMap.get("active")),
                lastResource.getId());
        logger.info("Details to send: " + resourceGiven);

        // Send the PUT Request
        response = resourceRequest.updateResource(resourceGiven, lastResource.getId());
        logger.info("PUT request sent, resource updated");
    }

    @And("the response should have the same resource details that were sent")
    public void theResponseShouldHaveTheSameResourceDetailsThatWereSent() {
        // Save the de-serialized response on a Resource object
        resourceResponded = resourceRequest.getResourceEntity(response);
        logger.info("Details in response: " + resourceResponded);

        // verify all the actual data in the updated resource is the same as the expected
        Assert.assertEquals(resourceGiven.getId(), resourceResponded.getId());
        Assert.assertEquals(resourceGiven.getName(), resourceResponded.getName());
        Assert.assertEquals(resourceGiven.getTrademark(), resourceResponded.getTrademark());
        Assert.assertEquals(resourceGiven.getStock(), resourceResponded.getStock());
        Assert.assertEquals(resourceGiven.getPrice(), resourceResponded.getPrice(), Constants.DELTA);
        Assert.assertEquals(resourceGiven.getDescription(), resourceResponded.getDescription());
        Assert.assertEquals(resourceGiven.getTags(), resourceResponded.getTags());
        Assert.assertEquals(resourceGiven.getActive(), resourceResponded.getActive());

        logger.info("The resource details in the response are the same as the details sent in the PUT request");
    }

    @And("validates the response with resources JSON schema")
    public void validatesTheResponseWithResourcesJSONSchema() {
        // Validate Schema
        Assert.assertTrue(resourceRequest.validateSchema(response, Constants.PATH_RESOURCE_SCHEMA));
        logger.info("The schema was validated successfully");
    }
}
