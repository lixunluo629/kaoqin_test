package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;

@Deprecated
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/event/ApplicationStartedEvent.class */
public class ApplicationStartedEvent extends ApplicationStartingEvent {
    public ApplicationStartedEvent(SpringApplication application, String[] args) {
        super(application, args);
    }
}
