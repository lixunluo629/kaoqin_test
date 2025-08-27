package org.springframework.boot.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JacksonJsonParser.class */
public class JacksonJsonParser implements JsonParser {
    private static final TypeReference<?> MAP_TYPE = new MapTypeReference();
    private static final TypeReference<?> LIST_TYPE = new ListTypeReference();
    private ObjectMapper objectMapper;

    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) {
        try {
            return (Map) getObjectMapper().readValue(json, MAP_TYPE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse JSON", ex);
        }
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) {
        try {
            return (List) getObjectMapper().readValue(json, LIST_TYPE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse JSON", ex);
        }
    }

    private ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
        return this.objectMapper;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JacksonJsonParser$MapTypeReference.class */
    private static class MapTypeReference extends TypeReference<Map<String, Object>> {
        private MapTypeReference() {
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JacksonJsonParser$ListTypeReference.class */
    private static class ListTypeReference extends TypeReference<List<Object>> {
        private ListTypeReference() {
        }
    }
}
