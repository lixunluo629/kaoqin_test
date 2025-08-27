package org.springframework.http;

import java.util.HashMap;
import java.util.Map;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/HttpMethod.class */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private static final Map<String, HttpMethod> mappings = new HashMap(16);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    public static HttpMethod resolve(String method) {
        if (method != null) {
            return mappings.get(method);
        }
        return null;
    }

    public boolean matches(String method) {
        return this == resolve(method);
    }
}
