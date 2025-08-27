package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ContextRefreshedEvent.class */
public class ContextRefreshedEvent extends ApplicationContextEvent {
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
}
