package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/SmartApplicationListener.class */
public interface SmartApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
    boolean supportsEventType(Class<? extends ApplicationEvent> cls);

    boolean supportsSourceType(Class<?> cls);
}
