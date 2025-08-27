package org.springframework.core.io;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/ResourceLoader.class */
public interface ResourceLoader {
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String str);

    ClassLoader getClassLoader();
}
