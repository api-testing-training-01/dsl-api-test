package org.fundacionjala.dsl.api;

import java.util.Objects;

public final class EnvReader {

    private static final String PROP_FILE = "gradle.properties";
    private static final String API_TOKEN = "api.token";
    private static final String API_KEY = "api.key";
    private static EnvReader instance;

    private PropReader propReader;

    private EnvReader() {
        propReader = new PropReader(PROP_FILE);
    }

    public static synchronized EnvReader getInstance() {
        if (Objects.isNull(instance)) {
            instance = new EnvReader();
        }
        return instance;
    }

    public String getApiToken() {
        return propReader.getEnv(API_TOKEN);
    }

    public String getApiKey() {
        return propReader.getEnv(API_KEY);
    }
}
