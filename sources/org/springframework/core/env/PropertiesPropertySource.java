package org.springframework.core.env;

import java.util.Map;
import java.util.Properties;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/PropertiesPropertySource.class */
public class PropertiesPropertySource extends MapPropertySource {
    public PropertiesPropertySource(String name, Properties source) {
        super(name, source);
    }

    protected PropertiesPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
}
