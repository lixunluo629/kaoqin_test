package org.springframework.context;

import java.util.EventListener;
import org.springframework.context.ApplicationEvent;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationListener.class */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E e);
}
