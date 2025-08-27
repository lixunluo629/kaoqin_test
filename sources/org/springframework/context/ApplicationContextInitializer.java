package org.springframework.context;

import org.springframework.context.ConfigurableApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationContextInitializer.class */
public interface ApplicationContextInitializer<C extends ConfigurableApplicationContext> {
    void initialize(C c);
}
