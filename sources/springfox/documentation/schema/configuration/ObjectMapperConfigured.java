package springfox.documentation.schema.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEvent;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/configuration/ObjectMapperConfigured.class */
public class ObjectMapperConfigured extends ApplicationEvent {
    private final ObjectMapper objectMapper;

    public ObjectMapperConfigured(Object source, ObjectMapper objectMapper) {
        super(source);
        this.source = source;
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
}
