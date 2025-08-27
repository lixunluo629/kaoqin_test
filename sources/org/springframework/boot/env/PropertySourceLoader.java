package org.springframework.boot.env;

import java.io.IOException;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/PropertySourceLoader.class */
public interface PropertySourceLoader {
    String[] getFileExtensions();

    PropertySource<?> load(String str, Resource resource, String str2) throws IOException;
}
