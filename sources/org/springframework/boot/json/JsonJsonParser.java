package org.springframework.boot.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JsonJsonParser.class */
public class JsonJsonParser implements JsonParser {
    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) throws JSONException {
        Map<String, Object> map = new LinkedHashMap<>();
        putAll(map, new JSONObject(json));
        return map;
    }

    private void putAll(Map<String, Object> map, JSONObject object) throws JSONException {
        for (Object key : object.keySet()) {
            String name = key.toString();
            Object value = object.get(name);
            if (value instanceof JSONObject) {
                Map<String, Object> nested = new LinkedHashMap<>();
                putAll(nested, (JSONObject) value);
                value = nested;
            }
            if (value instanceof JSONArray) {
                List<Object> nested2 = new ArrayList<>();
                addAll(nested2, (JSONArray) value);
                value = nested2;
            }
            map.put(name, value);
        }
    }

    private void addAll(List<Object> list, JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                Map<String, Object> nested = new LinkedHashMap<>();
                putAll(nested, (JSONObject) value);
                value = nested;
            }
            if (value instanceof JSONArray) {
                List<Object> nested2 = new ArrayList<>();
                addAll(nested2, (JSONArray) value);
                value = nested2;
            }
            list.add(value);
        }
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) throws JSONException {
        List<Object> nested = new ArrayList<>();
        addAll(nested, new JSONArray(json));
        return nested;
    }
}
