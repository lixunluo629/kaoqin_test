package org.springframework.boot.env;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/EnvironmentPostProcessor.class */
public interface EnvironmentPostProcessor {
    void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication);
}
