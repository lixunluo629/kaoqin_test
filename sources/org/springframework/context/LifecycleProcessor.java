package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/LifecycleProcessor.class */
public interface LifecycleProcessor extends Lifecycle {
    void onRefresh();

    void onClose();
}
