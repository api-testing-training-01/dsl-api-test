package org.fundacionjala.dsl.api.diego;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class TrelloTest {

    private static final String PROJECTS_ENDPOINT_URL = "https://api.trello.com/1";
    private static final int OK_STATUS_CODE = 200;

    public RequestSpecification requestSpecification() {
        return given()
                .baseUri(PROJECTS_ENDPOINT_URL)
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey());
    }

    @Test
    public void testaddBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"New Board with Java\",\"desc\":\"This is a test with java and rest assure\"}")
                .post("/boards");
        int actualStatusCode = response.getStatusCode();
        File postBoardSchemaFile = new File("src/test/resources/diego-resources/postBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        response.then().assertThat().body(matchesJsonSchema(postBoardSchemaFile));
    }

    @Test
    public void testgetBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"New Board with Java\",\"desc\":\"This is a test with java and rest assure\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responseGetBoard = given()
                .spec(requestSpecification())
                .get("/boards/" + boardId);
        int actualStatusCode = responseGetBoard.getStatusCode();
        File getBoardSchemaFile = new File("src/test/resources/diego-resources/getBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseGetBoard.then().assertThat().body(matchesJsonSchema(getBoardSchemaFile));
    }

    @Test
    public void testupdateBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"JAVA\",\"desc\":\"This is a demo\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responsePutBoard = given()
                .spec(requestSpecification())
                .queryParam("name", "JAVA - UPDATED")
                .put("/boards/" + boardId);
        int actualStatusCode = responsePutBoard.getStatusCode();
        File putBoardSchemaFile = new File("src/test/resources/diego-resources/putBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responsePutBoard.then().assertThat().body(matchesJsonSchema(putBoardSchemaFile));
    }

    @Test
    public void testdeleteBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"XXX\",\"desc\":\"This is a demo\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responseDeleteBoard = given()
                .spec(requestSpecification())
                .delete("/boards/" + boardId);
        int actualStatusCode = responseDeleteBoard.getStatusCode();
        File deleteBoardSchemaFile = new File("src/test/resources/diego-resources/deleteBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseDeleteBoard.then().assertThat().body(matchesJsonSchema(deleteBoardSchemaFile));
    }
}
