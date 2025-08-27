package org.springframework.scheduling.config;

import org.springframework.scheduling.Trigger;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/config/TriggerTask.class */
public class TriggerTask extends Task {
    private final Trigger trigger;

    public TriggerTask(Runnable runnable, Trigger trigger) {
        super(runnable);
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return this.trigger;
    }
}
