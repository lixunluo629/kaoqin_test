package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/auth/In.class */
public enum In {
    HEADER,
    QUERY;

    private static Map<String, In> names = new HashMap();

    static {
        names.put("header", HEADER);
        names.put("query", QUERY);
    }

    @JsonCreator
    public static In forValue(String value) {
        return names.get(value.toLowerCase());
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, In> entry : names.entrySet()) {
            if (entry.getValue() == this) {
                return entry.getKey();
            }
        }
        return null;
    }
}
