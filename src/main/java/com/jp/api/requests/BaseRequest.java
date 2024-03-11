package com.jp.api.requests;

import com.jp.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {
    /**
     * This is a function to get a response from an endpoint using rest assured
     * @param endpoint api url (optional inclusion of id)
     * @param headers a map of headers
     * @return Response
     */
    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .get(endpoint);
    }

    /**
     * This is a function to create a new element with a specific body using rest assured
     * @param endpoint api url
     * @param headers a map of headers
     * @param body model object
     * @return Response
     */
    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * This is a function to create a generic new element using rest assured
     * @param endpoint api url
     * @param headers a map of headers
     * @return Response
     */
    protected Response requestPost(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .post(endpoint);
    }

    /**
     * This is a function to update an element sending a specific body using rest assured
     * @param endpoint api url with the id of the element to update
     * @param headers a map of headers
     * @param body model object
     * @return Response
     */
    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * This is a function to delete an element sending a specific body using rest assured
     * @param endpoint api url with the id of the element to delete
     * @param headers a map of headers
     * @return Response
     */
    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .delete(endpoint);
    }

    /**
     * This is a function that creates and returns the base headers for the requests
     * @return Map with the headers
     */
    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }

    /**
     * This function compares the schema of a response with a schema defined in the project
     * @param response the response to compare
     * @param schemaPath the path from source root of the schema
     * @return
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true; // Return true if the assertion passes
        } catch (AssertionError e) {
            // Assertion failed, return false
            return false;
        }
    }
}
