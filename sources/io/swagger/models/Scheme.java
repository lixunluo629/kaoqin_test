package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/Scheme.class */
public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    WS("ws"),
    WSS("wss");

    private final String value;

    Scheme(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Scheme forValue(String value) {
        Scheme[] arr$ = values();
        for (Scheme item : arr$) {
            if (item.toValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return this.value;
    }
}
