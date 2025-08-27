package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/event/ApplicationPreparedEvent.class */
public class ApplicationPreparedEvent extends SpringApplicationEvent {
    private final ConfigurableApplicationContext context;

    public ApplicationPreparedEvent(SpringApplication application, String[] args, ConfigurableApplicationContext context) {
        super(application, args);
        this.context = context;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return this.context;
    }
}
