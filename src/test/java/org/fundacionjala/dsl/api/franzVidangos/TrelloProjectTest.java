package org.fundacionjala.dsl.api.franzVidangos;

import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;
import static io.restassured.RestAssured.given;

public class TrelloProjectTest {

    @Test
    public void postTrello() {
        final int statusExpected = 200;
        Response response = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("name", "testAPI01FV")
                .when()
                .post("https://api.trello.com/1/boards");
        int actualStatusCode = response.getStatusCode();
        assertEquals(statusExpected, actualStatusCode);
        response.then().assertThat().
                body(matchesJsonSchema(new File("src\\test\\resources\\demo\\postSchema.json")));
    }

    @Test
    public void getTrello() {
        final int statusExpected = 200;
        Response postResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("name", "testAPI02FV")
                .when()
                .post("https://api.trello.com/1/boards");
        String boardId = postResponse.jsonPath().getString("id");

        String path = "https://api.trello.com/1/boards/" + boardId;
        Response getResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .when()
                .get(path);
        int actualStatusCode = getResponse.getStatusCode();
        assertEquals(statusExpected, actualStatusCode);
        getResponse.then().assertThat().
                body(matchesJsonSchema(new File("src\\test\\resources\\demo\\getSchema.json")));
    }

    @Test
    public void deleteTrello() {
        final int statusExpected = 200;
        Response postResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("name", "testAPI04FV")
                .when()
                .post("https://api.trello.com/1/boards");
        String boardId = postResponse.jsonPath().getString("id");

        String path = "https://api.trello.com/1/boards/" + boardId;
        Response deleteResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .when()
                .delete(path);
        int actualStatusCode = deleteResponse.getStatusCode();
        assertEquals(statusExpected, actualStatusCode);
        deleteResponse.then().assertThat().
                body(matchesJsonSchema(new File("src\\test\\resources\\demo\\deleteSchema.json")));
    }

    @Test
    public void putTrello() {
        final int statusExpected = 200;
        Response postResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("name", "testAPI04FV")
                .when()
                .post("https://api.trello.com/1/boards");
        String boardId = postResponse.jsonPath().getString("id");

        String path = "https://api.trello.com/1/boards/" + boardId;
        Response putResponse = given()
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("text", "success!")
                .when()
                .put(path);
        int actualStatusCode = putResponse.getStatusCode();
        assertEquals(statusExpected, actualStatusCode);
        putResponse.then().assertThat().
                body(matchesJsonSchema(new File("src\\test\\resources\\demo\\putSchema.json")));
    }
}
