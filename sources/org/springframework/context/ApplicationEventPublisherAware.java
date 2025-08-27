package org.springframework.context;

import org.springframework.beans.factory.Aware;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationEventPublisherAware.class */
public interface ApplicationEventPublisherAware extends Aware {
    void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher);
}
