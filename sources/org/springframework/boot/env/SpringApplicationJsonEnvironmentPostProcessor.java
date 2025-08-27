package org.springframework.boot.env;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.StandardServletEnvironment;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/SpringApplicationJsonEnvironmentPostProcessor.class */
public class SpringApplicationJsonEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String SERVLET_ENVIRONMENT_CLASS = "org.springframework.web.context.support.StandardServletEnvironment";
    public static final int DEFAULT_ORDER = -2147483643;
    private static final Log logger = LogFactory.getLog(SpringApplicationJsonEnvironmentPostProcessor.class);
    private int order = DEFAULT_ORDER;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.boot.env.EnvironmentPostProcessor
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String json = environment.resolvePlaceholders("${spring.application.json:${SPRING_APPLICATION_JSON:}}");
        if (StringUtils.hasText(json)) {
            processJson(environment, json);
        }
    }

    private void processJson(ConfigurableEnvironment environment, String json) {
        try {
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = parser.parseMap(json);
            if (!map.isEmpty()) {
                addJsonPropertySource(environment, new MapPropertySource("spring.application.json", flatten(map)));
            }
        } catch (Exception ex) {
            logger.warn("Cannot parse JSON for spring.application.json: " + json, ex);
        }
    }

    private Map<String, Object> flatten(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>();
        flatten(null, result, map);
        return result;
    }

    private void flatten(String prefix, Map<String, Object> result, Map<String, Object> map) {
        String prefix2 = prefix != null ? prefix + "." : "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            extract(prefix2 + entry.getKey(), result, entry.getValue());
        }
    }

    private void extract(String name, Map<String, Object> result, Object value) {
        if (value instanceof Map) {
            flatten(name, result, (Map) value);
            return;
        }
        if (value instanceof Collection) {
            int index = 0;
            for (Object object : (Collection) value) {
                extract(name + PropertyAccessor.PROPERTY_KEY_PREFIX + index + "]", result, object);
                index++;
            }
            return;
        }
        result.put(name, value);
    }

    private void addJsonPropertySource(ConfigurableEnvironment environment, PropertySource<?> source) {
        MutablePropertySources sources = environment.getPropertySources();
        String name = findPropertySource(sources);
        if (sources.contains(name)) {
            sources.addBefore(name, source);
        } else {
            sources.addFirst(source);
        }
    }

    private String findPropertySource(MutablePropertySources sources) {
        if (ClassUtils.isPresent(SERVLET_ENVIRONMENT_CLASS, null) && sources.contains(StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME)) {
            return StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME;
        }
        return "systemProperties";
    }
}
