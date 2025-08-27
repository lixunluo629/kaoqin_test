package org.springframework.core.env;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/Environment.class */
public interface Environment extends PropertyResolver {
    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    boolean acceptsProfiles(String... strArr);
}
