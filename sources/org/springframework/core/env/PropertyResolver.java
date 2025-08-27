package org.springframework.core.env;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/PropertyResolver.class */
public interface PropertyResolver {
    boolean containsProperty(String str);

    String getProperty(String str);

    String getProperty(String str, String str2);

    <T> T getProperty(String str, Class<T> cls);

    <T> T getProperty(String str, Class<T> cls, T t);

    @Deprecated
    <T> Class<T> getPropertyAsClass(String str, Class<T> cls);

    String getRequiredProperty(String str) throws IllegalStateException;

    <T> T getRequiredProperty(String str, Class<T> cls) throws IllegalStateException;

    String resolvePlaceholders(String str);

    String resolveRequiredPlaceholders(String str) throws IllegalArgumentException;
}
