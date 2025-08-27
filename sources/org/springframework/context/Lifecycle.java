package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/Lifecycle.class */
public interface Lifecycle {
    void start();

    void stop();

    boolean isRunning();
}
