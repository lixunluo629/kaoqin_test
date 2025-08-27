package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/ApplicationEventMulticaster.class */
public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> applicationListener);

    void addApplicationListenerBean(String str);

    void removeApplicationListener(ApplicationListener<?> applicationListener);

    void removeApplicationListenerBean(String str);

    void removeAllListeners();

    void multicastEvent(ApplicationEvent applicationEvent);

    void multicastEvent(ApplicationEvent applicationEvent, ResolvableType resolvableType);
}
