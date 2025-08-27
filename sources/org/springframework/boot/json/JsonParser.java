package org.springframework.boot.json;

import java.util.List;
import java.util.Map;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/json/JsonParser.class */
public interface JsonParser {
    Map<String, Object> parseMap(String str);

    List<Object> parseList(String str);
}
