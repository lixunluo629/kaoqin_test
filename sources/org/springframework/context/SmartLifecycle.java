package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/SmartLifecycle.class */
public interface SmartLifecycle extends Lifecycle, Phased {
    boolean isAutoStartup();

    void stop(Runnable runnable);
}
