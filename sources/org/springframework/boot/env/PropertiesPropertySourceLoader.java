package org.springframework.boot.env;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/PropertiesPropertySourceLoader.class */
public class PropertiesPropertySourceLoader implements PropertySourceLoader {
    @Override // org.springframework.boot.env.PropertySourceLoader
    public String[] getFileExtensions() {
        return new String[]{"properties", "xml"};
    }

    @Override // org.springframework.boot.env.PropertySourceLoader
    public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
        if (profile == null) {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            if (!properties.isEmpty()) {
                return new PropertiesPropertySource(name, properties);
            }
            return null;
        }
        return null;
    }
}
