package org.fundacionjala.dsl.api.utils;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    private List<String> ids;
    private Map<String, Response> responses;

    public Helper() {
        ids = new ArrayList<>();
        responses = new HashMap<>();
    }

    public void addNewId(final String id) {
        ids.add(id);
    }

    public List<String> getIds() {
        return ids;
    }

    public void addResponse(final String key, final Response response) {
        responses.put(key, response);
    }

    public Map<String, Response> getResponses() {
        return responses;
    }
}
