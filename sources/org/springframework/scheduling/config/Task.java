package org.springframework.scheduling.config;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/config/Task.class */
public class Task {
    private final Runnable runnable;

    public Task(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }
}
