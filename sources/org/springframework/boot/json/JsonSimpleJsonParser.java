package org.springframework.boot.json;

import java.util.List;
import java.util.Map;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JsonSimpleJsonParser.class */
public class JsonSimpleJsonParser implements JsonParser {
    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) {
        try {
            return (Map) new JSONParser().parse(json);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Cannot parse JSON", ex);
        }
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) {
        try {
            return (List) new JSONParser().parse(json);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Cannot parse JSON", ex);
        }
    }
}
