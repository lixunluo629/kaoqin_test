package org.springframework.context.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationListener;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/EventListenerFactory.class */
public interface EventListenerFactory {
    boolean supportsMethod(Method method);

    ApplicationListener<?> createApplicationListener(String str, Class<?> cls, Method method);
}
