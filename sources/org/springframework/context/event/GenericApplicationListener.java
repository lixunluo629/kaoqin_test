package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/GenericApplicationListener.class */
public interface GenericApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
    boolean supportsEventType(ResolvableType resolvableType);

    boolean supportsSourceType(Class<?> cls);
}
