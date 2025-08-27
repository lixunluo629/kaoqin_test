package org.springframework.boot.json;

import java.util.List;
import java.util.Map;
import org.springframework.beans.PropertyAccessor;
import org.yaml.snakeyaml.Yaml;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/YamlJsonParser.class */
public class YamlJsonParser implements JsonParser {
    @Override // org.springframework.boot.json.JsonParser
    public Map<String, Object> parseMap(String json) {
        if (json != null) {
            String json2 = json.trim();
            if (json2.startsWith("{")) {
                return (Map) new Yaml().loadAs(json2, Map.class);
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }

    @Override // org.springframework.boot.json.JsonParser
    public List<Object> parseList(String json) {
        if (json != null) {
            String json2 = json.trim();
            if (json2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                return (List) new Yaml().loadAs(json2, List.class);
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }
}
