package org.fundacionjala.dsl.api.hooks;

import io.cucumber.java.After;
import org.fundacionjala.dsl.api.EnvReader;
import org.fundacionjala.dsl.api.utils.Helper;
import static io.restassured.RestAssured.given;

public class PrePostConditions {
    private Helper helper;

    public PrePostConditions(final Helper helper) {
        this.helper = helper;
    }

    @After(value = "@deleteBoardPostCond")
    public void deleteBoard() {
        for (String id : helper.getIds()) {
            given()
                    .header("Content-Type", "application/json")
                    .queryParam("key", EnvReader.getInstance().getApiKey())
                    .queryParam("token", EnvReader.getInstance().getApiToken())
                    .when()
                    .delete("https://api.trello.com/1/boards".concat(id));

        }
    }
}
