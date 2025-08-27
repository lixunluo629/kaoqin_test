package org.springframework.http.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.class */
public class MappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    private String jsonPrefix;

    public MappingJackson2HttpMessageConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = prefixJson ? ")]}', " : null;
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
        String jsonpFunction = object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null;
        if (jsonpFunction != null) {
            generator.writeRaw("/**/");
            generator.writeRaw(jsonpFunction + "(");
        }
    }

    @Override // org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction = object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null;
        if (jsonpFunction != null) {
            generator.writeRaw(");");
        }
    }
}
