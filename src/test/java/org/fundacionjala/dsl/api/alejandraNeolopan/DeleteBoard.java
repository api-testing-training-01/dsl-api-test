package org.fundacionjala.dsl.api.alejandraNeolopan;

import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class DeleteBoard {
    @Test
    public void deleteBoard() {
        String url = "https://api.trello.com";
        final int oKStatus = 200;
        String token = EnvReader.getInstance().getApiToken();
        String key = EnvReader.getInstance().getApiKey();
        String idBoard = "";
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .post(url + "/1/boards/?name=ToDelete&key={key}&token={token}", key, token);
        idBoard = response.then()
                .assertThat()
                .extract()
                .path("id");


        response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete(url + "/1/boards/" + idBoard + "?key={key}&token={token}", key, token);
        int actualStatusCode = response.getStatusCode();
        assertEquals(oKStatus, actualStatusCode);
        String path = "src/test/resources/alejandraNeolopan/deleteSchema.json";
        response.then().assertThat().body(matchesJsonSchema(new File(path)));
    }
}
