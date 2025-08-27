package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ContextStoppedEvent.class */
public class ContextStoppedEvent extends ApplicationContextEvent {
    public ContextStoppedEvent(ApplicationContext source) {
        super(source);
    }
}
