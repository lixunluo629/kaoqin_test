package springfox.documentation.spring.web.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/json/JsonSerializer.class */
public class JsonSerializer {
    private ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer(List<JacksonModuleRegistrar> modules) {
        for (JacksonModuleRegistrar each : modules) {
            each.maybeRegisterModule(this.objectMapper);
        }
    }

    public Json toJson(Object toSerialize) {
        try {
            return new Json(this.objectMapper.writeValueAsString(toSerialize));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not write JSON", e);
        }
    }
}
