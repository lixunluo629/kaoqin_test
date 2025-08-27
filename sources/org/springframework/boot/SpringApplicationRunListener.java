package org.springframework.boot;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/SpringApplicationRunListener.class */
public interface SpringApplicationRunListener {
    void starting();

    void environmentPrepared(ConfigurableEnvironment configurableEnvironment);

    void contextPrepared(ConfigurableApplicationContext configurableApplicationContext);

    void contextLoaded(ConfigurableApplicationContext configurableApplicationContext);

    void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable th);
}
