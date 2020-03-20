package org.fundacionjala.dsl.api.JoseEguivar;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class TrelloTest {
    private static final String BOARD_ENDPOINT_URL = "https://api.trello.com/1/boards";
    private static final int OK_STATUS_CODE = 200;

    public Response newBoard() {
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("name", "API")
                .when()
                .post(BOARD_ENDPOINT_URL);
        return response;
    }

    public String idBoard(final Response id) {
        JsonPath body = new JsonPath(id.getBody().asString());
        String idBoard = body.getString("id");
        return idBoard;
    }

    @Test
    public void postBoard() {
        Response post = newBoard();
        int actualStatusCode = post.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaPost = new File("src/test/resources/TrelloFiles/PostSchema.json");
        post.then().assertThat().body(matchesJsonSchema(schemaPost));
    }

    @Test
    public void getBoard() {
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when()
                .get(BOARD_ENDPOINT_URL + "/" + idBoard(newBoard()));
        int actualStatusCode = response.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaGet = new File("src/test/resources/TrelloFiles/GetSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaGet));
    }

    @Test
    public void putBoard() {
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("name", "API EDIT")
                .when()
                .put(BOARD_ENDPOINT_URL + "/" + idBoard(newBoard()));
        int actualStatusCode = response.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaPut = new File("src/test/resources/TrelloFiles/PutSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaPut));
    }

    @Test
    public void deleteBoard() {
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when()
                .delete(BOARD_ENDPOINT_URL + "/" + idBoard(newBoard()));
        int actualStatusCode = response.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaPut = new File("src/test/resources/TrelloFiles/DeleteSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaPut));
    }


}
