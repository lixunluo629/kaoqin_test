package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ContextClosedEvent.class */
public class ContextClosedEvent extends ApplicationContextEvent {
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
}
