package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationEventPublisher.class */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent applicationEvent);

    void publishEvent(Object obj);
}
