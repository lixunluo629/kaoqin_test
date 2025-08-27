package org.springframework.boot.json;

import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JsonParserFactory.class */
public abstract class JsonParserFactory {
    public static JsonParser getJsonParser() {
        if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
            return new JacksonJsonParser();
        }
        if (ClassUtils.isPresent("com.google.gson.Gson", null)) {
            return new GsonJsonParser();
        }
        if (ClassUtils.isPresent("org.yaml.snakeyaml.Yaml", null)) {
            return new YamlJsonParser();
        }
        if (ClassUtils.isPresent("org.json.simple.JSONObject", null)) {
            return new JsonSimpleJsonParser();
        }
        if (ClassUtils.isPresent("org.json.JSONObject", null)) {
            return new JsonJsonParser();
        }
        return new BasicJsonParser();
    }
}
