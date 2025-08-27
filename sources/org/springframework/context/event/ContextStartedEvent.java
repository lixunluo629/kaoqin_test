package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ContextStartedEvent.class */
public class ContextStartedEvent extends ApplicationContextEvent {
    public ContextStartedEvent(ApplicationContext source) {
        super(source);
    }
}
