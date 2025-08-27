package org.springframework.scheduling;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/SchedulingAwareRunnable.class */
public interface SchedulingAwareRunnable extends Runnable {
    boolean isLongLived();
}
