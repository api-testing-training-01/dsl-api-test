package org.fundacionjala.dsl.api.jacky;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class JackyAssignment04 {
    private static final String TRELLO_ENDPOINT_URL = "https://api.trello.com/1/boards";
    private static final int OK_STATUS_CODE = 200;

    public RequestSpecification setParameters() {
        return given()
                .baseUri(TRELLO_ENDPOINT_URL)
                .contentType("application/json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken());
    }
    public Response createBoard() {
        Response response = given()
                .spec(setParameters())
                .when()
                .body("{\"name\":\"New Board Created\",\"desc\":\"new board created from intellij\"}")
                .post()
                .andReturn();
        return response;
    }
    public String getIDBoard(final Response board) {
        JsonPath jsonPath = new JsonPath(board.getBody().asString());
        String idBoard = jsonPath.getString("id");
        return idBoard;
    }

    @Test
    public void postBoard() {
        Response responsePost = createBoard();

        int actualStatusCode = responsePost.getStatusCode();
        String body = responsePost.getBody().asString();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File schemaFile = new File("src/test/resources/jacky/postSchema.json");
        responsePost.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Test
    public void getBoard() {
        Response boardCreated = createBoard();
        Response responseGet = given()
                .spec(setParameters())
                .get("/" + getIDBoard(boardCreated));
        int actualStatusCode = responseGet.getStatusCode();
        File getSchemaFile = new File("src/test/resources/jacky/getSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseGet.then().assertThat().body(matchesJsonSchema(getSchemaFile));
    }

    @Test
    public void deleteBoard() {
        Response boardCreated = createBoard();
        Response responseDelete = given()
                .spec(setParameters())
                .delete("/" + getIDBoard(boardCreated));
        int actualStatusCode = responseDelete.getStatusCode();
        File deleteSchemaFile = new File("src/test/resources/jacky/deleteSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseDelete.then().assertThat().body(matchesJsonSchema(deleteSchemaFile));
    }

    @Test
    public void putBoard() {
        Response boardCreated = createBoard();
        Response responseDelete = given()
                .spec(setParameters())
                .queryParam("desc", "Description for this board was updated")
                .put("/" + getIDBoard(boardCreated));
        int actualStatusCode = responseDelete.getStatusCode();
        File putSchemaFile = new File("src/test/resources/jacky/putSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseDelete.then().assertThat().body(matchesJsonSchema(putSchemaFile));

    }
}
