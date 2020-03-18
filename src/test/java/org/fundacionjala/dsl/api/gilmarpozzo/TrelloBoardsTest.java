package org.fundacionjala.dsl.api.gilmarpozzo;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class TrelloBoardsTest {

    private static final String PROJECTS_ENDPOINT_URL = "https://api.trello.com";
    private static final int OK_STATUS_CODE = 200;
    private static final String SCHEMA_RESOURCES = "src/test/resources/gilmarpozzo/";

    private String boardId;

    public String getBoardId() {
        return boardId;
    }

    public String setBoardId(final String id) {
        boardId = id;
        return boardId;
    }

    public RequestSpecification requestHeader() {
        return given()
                .baseUri(PROJECTS_ENDPOINT_URL)
                .contentType("Application/Json")
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .queryParam("key", EnvReader.getInstance().getApiKey());
    }

    public Response addBoardAndSetId(final String name) {
        Response response = given()
                .spec(requestHeader())
                .body("{\"name\":\"" + name + "\",\"desc\":\"testing... boards\"}")
                .post("/1/boards");
        JsonPath bodyResponse = new JsonPath(response.getBody().asString());
        String boardId = bodyResponse.getString("id");
        setBoardId(boardId);
        return response;
    }

    public Response removeBoard(final String id) {
        return given()
                .spec(requestHeader())
                .delete("/1/boards/" + id);
    }

    @Test
    public void postBoard() {
        Response response = addBoardAndSetId("GP TESTING BOARD");
        int actualStatusCode = response.getStatusCode();
        File postBoardSchemaFile = new File(SCHEMA_RESOURCES + "postBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        response.then().assertThat().body(matchesJsonSchema(postBoardSchemaFile));
        removeBoard(getBoardId());
    }


    @Test
    public void getBoard() {
        addBoardAndSetId("Testing GET Board");
        Response responseGetBoard = given()
                .spec(requestHeader())
                .get("/1/boards/" + getBoardId());
        int actualStatusCode = responseGetBoard.getStatusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        File getBoardSchemaFile = new File(SCHEMA_RESOURCES + "getBoardSchema.json");
        responseGetBoard.then().assertThat().body(matchesJsonSchema(getBoardSchemaFile));
        removeBoard(getBoardId());
    }

    @Test
    public void putBoard() {
        addBoardAndSetId("Testing PUT Board");
        Response responsePutBoard = given()
                .spec(requestHeader())
                .queryParam("name", "JAVA - UPDATED")
                .put("/1/boards/" + getBoardId());
        int actualStatusCode = responsePutBoard.getStatusCode();
        File putBoardSchemaFile = new File(SCHEMA_RESOURCES + "putBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responsePutBoard.then().assertThat().body(matchesJsonSchema(putBoardSchemaFile));
        removeBoard(getBoardId());
    }

    @Test
    public void deleteBoard() {
        addBoardAndSetId("Testing DELETE Board");
        Response responseDeleteBoard = removeBoard(getBoardId());
        int actualStatusCode = responseDeleteBoard.getStatusCode();
        File deleteBoardSchemaFile = new File(SCHEMA_RESOURCES + "deleteBoardSchema.json");
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        responseDeleteBoard.then().assertThat().body(matchesJsonSchema(deleteBoardSchemaFile));
        removeBoard(getBoardId());
    }
}
