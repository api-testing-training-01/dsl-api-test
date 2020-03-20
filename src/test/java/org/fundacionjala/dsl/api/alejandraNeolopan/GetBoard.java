package org.fundacionjala.dsl.api.alejandraNeolopan;
import io.restassured.response.Response;
import org.fundacionjala.dsl.api.EnvReader;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

public class GetBoard {
    @Test
    public void getBoard() {
        final int oKStatus = 200;
        final int limit = 1000;
        String url = "https://api.trello.com";
        String token = EnvReader.getInstance().getApiToken();
        String key = EnvReader.getInstance().getApiKey();
        String idBoard = "";
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .post(url + "/1/boards/?name=NewBoard&key={key}&token={token}", key, token);
        idBoard = response.then()
                .assertThat()
                .extract()
                .path("id");

String value = "addAttachmentToCard%2CaddChecklistToCard%2CaddMemberToBoard%2CaddMemberToCard"
        + "%2CaddToOrganizationBoard%2CcommentCard%2CconvertToCardFromCheckItem%2CcopyBoard%2CcopyCard"
        + "%2CcopyCommentCard%2CcreateBoard%2CcreateCard%2CcreateList%2CdeleteAttachmentFromCard%2CdeleteCard%2C"
        + "disablePlugin%2CdisablePowerUp%2CemailCard%2CenablePlugin%2CenablePowerUp%2CmakeAdminOfBoard%2C"
        + "makeNormalMemberOfBoard%2CmakeObserverOfBoard%2CmoveCardFromBoard%2CmoveCardToBoard%2CmoveListFromBoard"
        + "%2CmoveListToBoard%2CremoveChecklistFromCard%2CremoveFromOrganizationBoard%2CremoveMemberFromCard%2C"
        + "unconfirmedBoardInvitation%2CunconfirmedOrganizationInvitation%2CupdateBoard%2CupdateCard%3Aclosed%2C"
        +         "updateCard%3Adue%2CupdateCard%3AidList%2CupdateCheckItemStateOnCard%2CupdateList%3Aclosed";
String field = "closed%2CcreationMethod%2CdateLastActivity%2CdateLastView%2CdatePluginDisable%2CenterpriseOwned%2C"
        + "id%2CidOrganization%2Cname%2Cprefs%2CshortLink%2CshortUrl%2Curl%2Cdesc%2CdescData%2CidTags%2Cinvitations%2C"
        + "invited%2ClabelNames%2Climits%2Cmemberships%2CpowerUps%2Csubscribed";

String cardField = "badges%2Cclosed%2Cdesc%2CidAttachmentCover%2CidBoard%2CidList%2CidMembers%2CisTemplate%2Clabels"
        + "%2Cname%2Cpos%2CshortLink%2Curl";
      response = given()
                .header("Content-Type", "application/json")
              .queryParam("lists", "open")
              .queryParam("actions", value)
              .queryParam("actions_display", "true")
              .queryParam("actions_limit", "50")
              .queryParam("cards", "visible")
              .queryParam("card_attachments", "true")
              .queryParam("card_fields", cardField)
              .queryParam("fields", field)
              .queryParam("labels", "all")
              .queryParam("labels_limit", limit)
              .queryParam("members", "all")
              .queryParam("memberships", "all")
              .queryParam("organization", "true")
              .queryParam("organization_memberships", "all")
                .when()
                .get(url + "/1/boards/" + idBoard + "?key={key}&token={token}", key, token);
      System.out.println(idBoard);
        int actualStatusCode = response.getStatusCode();
        String actualBody = response.getBody().asString();
        assertEquals(oKStatus, actualStatusCode);
        String path = "src/test/resources/alejandraNeolopan/getSchema.json";
        response.then().assertThat().body(matchesJsonSchema(new File(path)));

    }
}
