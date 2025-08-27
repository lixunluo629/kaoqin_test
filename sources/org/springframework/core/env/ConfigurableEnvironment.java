package org.springframework.core.env;

import java.util.Map;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/ConfigurableEnvironment.class */
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
    void setActiveProfiles(String... strArr);

    void addActiveProfile(String str);

    void setDefaultProfiles(String... strArr);

    MutablePropertySources getPropertySources();

    Map<String, Object> getSystemProperties();

    Map<String, Object> getSystemEnvironment();

    void merge(ConfigurableEnvironment configurableEnvironment);
}
