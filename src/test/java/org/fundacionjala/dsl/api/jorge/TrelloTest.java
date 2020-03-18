package org.fundacionjala.dsl.api.jorge;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class TrelloTest {

    private static final int OK_STATUS_CODE = 200;
    private static final String BASE_URL = "https://api.trello.com/1/boards/";

    @Test
    public void getBoardTest() {
        Response response = giveTrelloHeader().put("https://api.trello.com/1/boards/r5IYAPyn?name=NewBoardfromIJGet");
        String path = getPath(response.getBody().jsonPath().getString("shortUrl"));

        response = giveTrelloHeader().get(path);

        assertEquals(OK_STATUS_CODE, response.getStatusCode());
        response.then().body(matchesJsonSchema(new File("src/test/resources/jorge/getSchema.json")));
    }

    @Test
    public void postBoardTest() {
        Response response = giveTrelloHeader().put("https://api.trello.com/1/boards/GSDLgZsG?name=NewBoardfromIJPost");
        String path = new StringBuilder().append(BASE_URL)
                                         .append("?")
                                         .append("name=")
                                         .append(response.getBody().jsonPath().getString("name")).toString();

        response = giveTrelloHeader().post(path);

        assertEquals(OK_STATUS_CODE, response.getStatusCode());
        response.then().body(matchesJsonSchema(new File("src/test/resources/jorge/postSchema.json")));
    }

    @Test
    public void putBoardTest() {
        Response response = giveTrelloHeader().put("https://api.trello.com/1/boards/5K8Jl1r1?name=NewBoardfromIJPut");

        assertEquals(OK_STATUS_CODE, response.getStatusCode());
        response.then().body(matchesJsonSchema(new File("src/test/resources/jorge/putSchema.json")));
    }

    @Test
    public void deleteBoardTest() {
        Response response = giveTrelloHeader().put("https://api.trello.com/1/boards/0EVI29JO?name=NewBoardfromIJDEL");
        String id = getId(response.getBody().jsonPath().getString("shortUrl"));
        String path = new StringBuilder().append(BASE_URL)
                                         .append(id).toString();

        response = giveTrelloHeader().delete(path);
        assertEquals(OK_STATUS_CODE, response.getStatusCode());
        response.then().body(matchesJsonSchema(new File("src/test/resources/jorge/deleteSchema.json")));
    }

    public static RequestSpecification giveTrelloHeader() {
        return given().contentType("Application/Json")
                .queryParam("key", EnvReader.getInstance().getApiKey())
                .queryParam("token", EnvReader.getInstance().getApiToken())
                .when();
    }

    public static String getId(final String url) {
        String[] urlArray = url.split("/");
        return urlArray[urlArray.length - 1];
    }

    public static String getPath(final String url) {
        return new StringBuilder().append(BASE_URL).append(getId(url)).append("/").toString();
    }
}
