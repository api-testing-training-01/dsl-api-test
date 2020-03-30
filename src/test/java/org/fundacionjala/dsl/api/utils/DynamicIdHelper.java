package org.fundacionjala.dsl.api.utils;

import io.restassured.response.Response;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DynamicIdHelper {

    private static final String DATA_MATCHER_REGEX = "(?<=\\{)(.*?)(?=\\})";
    private static final String URL_DELIMITER = "/";

    private DynamicIdHelper() {
        // Default constructor for utility class.
    }

    public static String buildEndpoint(final Map<String, Response> context, final String endPoint) {
        String[] endPointSplit = endPoint.split(URL_DELIMITER);
        for (int i = 0; i < endPointSplit.length; i++) {
            Pattern pattern = Pattern.compile(DATA_MATCHER_REGEX);
            Matcher matcher = pattern.matcher(endPointSplit[i]);
            if (matcher.find()) {
                endPointSplit[i] = getElementResponse(context, matcher.group(1));
            }
        }
        return String.join(URL_DELIMITER, endPointSplit);
    }

    private static String getElementResponse(final Map<String, Response> context, final String element) {
        String[] elementSplit = element.split("\\.");
        Response response = context.get(elementSplit[0]);
        return response.jsonPath().getString(elementSplit[1]);
    }
}