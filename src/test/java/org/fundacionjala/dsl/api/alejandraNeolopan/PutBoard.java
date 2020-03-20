package org.fundacionjala.dsl.api.alejandraNeolopan;

import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class PutBoard {
    @Test
    public void deleteBoard() {
        final int oKStatus = 200;
        String url = "https://api.trello.com";
        String token = EnvReader.getInstance().getApiToken();
        String key = EnvReader.getInstance().getApiKey();
        String idBoard = "";
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("name", "New%2CBoard")
                .queryParam("defaultLabels", "true")
                .queryParam("defaultLists", "true")
                .queryParam("desc", "Created%20by%20API")
                .queryParam("keepFromSource", "none")
                .queryParam("prefs_permissionLevel", "private")
                .queryParam("prefs_voting", "disabled")
                .queryParam("prefs_comments", "members")
                .queryParam("prefs_invitations", "members")
                .queryParam("prefs_selfJoin", "true")
                .queryParam("prefs_cardCovers", "true")
                .queryParam("prefs_background", "blue")
                .queryParam("prefs_cardAging", "regular")
                .when()
                .post(url + "/1/boards/?key={key}&token={token}", key, token);
        idBoard = response.then()
                .assertThat()
                .extract()
                .path("id");

        System.out.println(idBoard);
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("name", "Updated%20name")
                .queryParam("desc", "By%20APICode")
                .when()
                .delete(url + "/1/boards/" + idBoard + "?key={key}&token={token}", key, token);
        int actualStatusCode = response.getStatusCode();
        String actualBody = response.getBody().asString();
        assertEquals(oKStatus, actualStatusCode);
        String path = "src/test/resources/alejandraNeolopan/putSchema.json";
        response.then().assertThat().body(matchesJsonSchema(new File(path)));
    }
}
