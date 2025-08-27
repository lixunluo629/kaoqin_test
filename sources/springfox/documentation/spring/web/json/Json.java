package springfox.documentation.spring.web.json;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/json/Json.class */
public class Json {
    private final String value;

    public Json(String value) {
        this.value = value;
    }

    @JsonValue
    @JsonRawValue
    public String value() {
        return this.value;
    }
}
