package org.fundacionjala.dsl.api.demo;

import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class PivotalDemoTest {

    private static final String PROJECTS_ENDPOINT_URL = "https://www.pivotaltracker.com/services/v5/projects";
    private static final int OK_STATUS_CODE = 200;

    @Test
    public void getProjects() {
        Response response = given()
                .header("X-TrackerToken", EnvReader.getInstance().getApiToken())
                .header("Content-Type", "application/json")
                .when()
                .get(PROJECTS_ENDPOINT_URL);
        int actualStatusCode = response.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaFile = new File("src/test/resources/demo/getProjects.json");
        response.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Test
    public void postProject() {
        Response response = given()
                .header("X-TrackerToken", EnvReader.getInstance().getApiToken())
                .header("Content-Type", "application/json")
                .when()
                .body("{\"name\":\"Executioner\"}")
                .post(PROJECTS_ENDPOINT_URL);
        int actualStatusCode = response.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaFile = new File("src/test/resources/demo/postSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaFile));
    }
}
