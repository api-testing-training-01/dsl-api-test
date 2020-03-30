package org.fundacionjala.dsl.api.stepsdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.fundacionjala.dsl.api.utils.DynamicIdHelper;
import org.fundacionjala.dsl.api.utils.Helper;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.testng.Assert.assertEquals;

public class MyStepdefs {
    private Response response;
    private Helper helper;

    public MyStepdefs() {
    }

    public MyStepdefs(final Helper helper) {
        this.helper = helper;
    }

    @Given("I send a POST request to {string}")
    public void iSendAPOSTRequestTo(final String endpoint) {
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("name", "Testing")
                .when()
                .post(endpoint);
    }

    @Given("I store response as {string}")
    public void iStoreResponseAs(final String key) {
        helper.addResponse(key, response);
    }

    @When("I send a POST request to {string} with body")
    public void iSendAPOSTRequestTo(final String endpoint, final String body) {
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when()
                .body(body)
                .post(endpoint);
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(final String endpoint) {
        String completeEndpoint = DynamicIdHelper.buildEndpoint(helper.getResponses(), endpoint);
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when()
                .get(completeEndpoint);
    }

    @When("I send a PUT request to {string} with body")
    public void iSendAPUTRequestTo(final String endpoint, final String body) {
        String completeEndpoint = DynamicIdHelper.buildEndpoint(helper.getResponses(), endpoint);
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("name", "CUCUMBER EDIT")
                .when()
                .body(body)
                .put(completeEndpoint);
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(final String endpoint) {
        String completeEndpoint = DynamicIdHelper.buildEndpoint(helper.getResponses(), endpoint);
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when()
                .delete(completeEndpoint);
    }

    @Then("Response status code should be {int}")
    public void responseStatusCodeShouldBe(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, expectedStatusCode);
        if (!response.getBody().asString().isEmpty()) {
            String id = response.jsonPath().getString("id");
            helper.addNewId(id);
        }
    }

    @And("Response body should match with {string} json schema")
    public void responseBodyShouldMatchWithJsonSchema(final String pathSchema) {
        File schemaPost = new File(pathSchema);
        response.then().assertThat().body(matchesJsonSchema(schemaPost));
    }

    @And("Response should contain the following data")
    public void responseShouldContainTheFollowingData(final Map<String, String> expectedData) {
        for (String key : expectedData.keySet()) {
            assertEquals(response.jsonPath().getString(key), expectedData.get(key),
                    String.format("The '%s' field does not match with expected value.", key));
        }
    }


}
