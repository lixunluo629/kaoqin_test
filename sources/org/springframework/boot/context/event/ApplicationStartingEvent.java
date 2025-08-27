package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/event/ApplicationStartingEvent.class */
public class ApplicationStartingEvent extends SpringApplicationEvent {
    public ApplicationStartingEvent(SpringApplication application, String[] args) {
        super(application, args);
    }
}
