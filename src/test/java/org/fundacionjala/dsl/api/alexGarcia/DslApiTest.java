package org.fundacionjala.dsl.api.alexGarcia;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ParamConfig.UpdateStrategy.REPLACE;
import static io.restassured.config.ParamConfig.paramConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class DslApiTest {
    private static final String PROJECTS_ENDPOINT_URL = "https://api.trello.com/1";
    private static final int OK_STATUS_CODE = 200;
    private static RequestSpecification initialValues = RestAssured.given();
    private static List<String> boardList = new ArrayList<String>();

    @Before
    public void setUp() {
          initialValues
                .baseUri(PROJECTS_ENDPOINT_URL)
                .contentType("Application/Json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken());
    }

    @AfterClass
    public static void teardown() {
        for (String boardId : boardList) {
            Response deleteResponse = initialValues
                    .delete("/boards/" + boardId);
            assertEquals(OK_STATUS_CODE, deleteResponse.statusCode());
        }
    }

    public Response createDefaultBoard() {
        return initialValues
                .config(config().paramConfig(paramConfig().queryParamsUpdateStrategy(REPLACE)))
                .queryParam("name", "ApiTesting1")
                .queryParam("desc", "Aprendiendo a usar RestAssured")
                .post("/boards");
    }

    @Test
    public void createBoard() {
        Response response = createDefaultBoard();
        int actualStatusCode = response.statusCode();
        assertEquals(OK_STATUS_CODE, actualStatusCode);
        String boardId = response.jsonPath().getJsonObject("id");
        boardList.add(boardId);
        File schemaFile = new File("src/test/resources/alexGarcia/postSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaFile));


    }

    @Test
    public void getBoardById() {
        Response postResponse = createDefaultBoard();
        assertEquals(OK_STATUS_CODE, postResponse.statusCode());
        String boardId = postResponse.jsonPath().getJsonObject("id");
        boardList.add(boardId);

        Response getResponse = initialValues.get("/boards/" + boardId);
        assertEquals(OK_STATUS_CODE, getResponse.statusCode());

        File schemaFile = new File("src/test/resources/alexGarcia/getSchema.json");
        getResponse.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Test
    public void updateBoardById() {
        Response postResponse = createDefaultBoard();
        assertEquals(OK_STATUS_CODE, postResponse.statusCode());
        String boardId = postResponse.jsonPath().getJsonObject("id");
        boardList.add(boardId);

        Response updateResponse = initialValues
                .queryParam("name", "UpdateApiTesting3")
                .queryParam("desc", "Aprendiendo a usar RestAssured")
                .put("/boards/" + boardId);
        assertEquals(OK_STATUS_CODE, updateResponse.statusCode());

        File schemaFile = new File("src/test/resources/alexGarcia/putSchema.json");
        updateResponse.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Test
    public void deleteBoardById() {
        Response postResponse = createDefaultBoard();
        assertEquals(OK_STATUS_CODE, postResponse.statusCode());
        String boardId = postResponse.jsonPath().getJsonObject("id");

        Response deleteResponse = initialValues
                .delete("/boards/" + boardId);
        assertEquals(OK_STATUS_CODE, deleteResponse.statusCode());

        File schemaFile = new File("src/test/resources/alexGarcia/deleteSchema.json");
        deleteResponse.then().assertThat().body(matchesJsonSchema(schemaFile));
    }
}
