package org.fundacionjala.dsl.api.josev;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class TrelloApiTest {

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
                .body("{\"name\":\"trelloTestAutomation\"}")
                .post("/boards");
        int status = response.getStatusCode();
        File boardSchema = new File("src/test/resources/josev-resource/postBoard.json");
        assertEquals(OK_STATUS_CODE, status);
        response.then().assertThat().body(matchesJsonSchema(boardSchema));
    }

    @Test
    public void testgetBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"trelloTestAutomation\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responseGetBoard = given()
                .spec(requestSpecification())
                .get("/boards/" + boardId);
        int status = responseGetBoard.getStatusCode();
        File boardSchema = new File("src/test/resources/josev-resource/getBoard.json");
        assertEquals(OK_STATUS_CODE, status);
        responseGetBoard.then().assertThat().body(matchesJsonSchema(boardSchema));
    }

    @Test
    public void testupdateBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"JAVA\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responsePutBoard = given()
                .spec(requestSpecification())
                .queryParam("name", "trelloTestAutomation-UPDATED")
                .put("/boards/" + boardId);
        int status = responsePutBoard.getStatusCode();
        File boardSchema = new File("src/test/resources/josev-resource/putBoard.json");
        assertEquals(OK_STATUS_CODE, status);
        responsePutBoard.then().assertThat().body(matchesJsonSchema(boardSchema));
    }

    @Test
    public void testdeleteBoard() {
        Response response = given()
                .spec(requestSpecification())
                .body("{\"name\":\"trelloTestAutomation\"}")
                .post("/boards");
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String boardId = jsonPath.getString("id");

        Response responseDeleteBoard = given()
                .spec(requestSpecification())
                .delete("/boards/" + boardId);
        int status = responseDeleteBoard.getStatusCode();
        File boardSchema = new File("src/test/resources/josev-resource/deleteBoard.json");
        assertEquals(OK_STATUS_CODE, status);
        responseDeleteBoard.then().assertThat().body(matchesJsonSchema(boardSchema));
    }
}
