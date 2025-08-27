package org.springframework.boot.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;
import org.springframework.beans.PropertyAccessor;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/GsonJsonParser.class */
public class GsonJsonParser implements JsonParser {
    private static final TypeToken<?> MAP_TYPE = new MapTypeToken();
    private static final TypeToken<?> LIST_TYPE = new ListTypeToken();
    private Gson gson = new GsonBuilder().create();

    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) {
        if (json != null) {
            String json2 = json.trim();
            if (json2.startsWith("{")) {
                return (Map) this.gson.fromJson(json2, MAP_TYPE.getType());
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) {
        if (json != null) {
            String json2 = json.trim();
            if (json2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                return (List) this.gson.fromJson(json2, LIST_TYPE.getType());
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/GsonJsonParser$MapTypeToken.class */
    private static final class MapTypeToken extends TypeToken<Map<String, Object>> {
        private MapTypeToken() {
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/GsonJsonParser$ListTypeToken.class */
    private static final class ListTypeToken extends TypeToken<List<Object>> {
        private ListTypeToken() {
        }
    }
}
