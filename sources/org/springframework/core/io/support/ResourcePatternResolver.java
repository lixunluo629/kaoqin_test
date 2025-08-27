package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/support/ResourcePatternResolver.class */
public interface ResourcePatternResolver extends ResourceLoader {
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource[] getResources(String str) throws IOException;
}
