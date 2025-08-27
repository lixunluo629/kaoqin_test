package org.springframework.data.repository.init;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/Jackson2ResourceReader.class */
public class Jackson2ResourceReader implements ResourceReader {
    private static final String DEFAULT_TYPE_KEY = "_class";
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    private final ObjectMapper mapper;
    private String typeKey;

    static {
        DEFAULT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Jackson2ResourceReader() {
        this(DEFAULT_MAPPER);
    }

    public Jackson2ResourceReader(ObjectMapper mapper) {
        this.typeKey = "_class";
        this.mapper = mapper == null ? DEFAULT_MAPPER : mapper;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    @Override // org.springframework.data.repository.init.ResourceReader
    public Object readFrom(Resource resource, ClassLoader classLoader) throws Exception {
        InputStream stream = resource.getInputStream();
        JsonNode node = this.mapper.reader(JsonNode.class).readTree(stream);
        if (node.isArray()) {
            Iterator<JsonNode> elements = node.elements();
            List<Object> result = new ArrayList<>();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                result.add(readSingle(element, classLoader));
            }
            return result;
        }
        return readSingle(node, classLoader);
    }

    private Object readSingle(JsonNode node, ClassLoader classLoader) throws IOException, IllegalArgumentException {
        JsonNode typeNode = node.findValue(this.typeKey);
        String typeName = typeNode == null ? null : typeNode.asText();
        Class<?> type = ClassUtils.resolveClassName(typeName, classLoader);
        return this.mapper.reader(type).readValue(node);
    }
}
