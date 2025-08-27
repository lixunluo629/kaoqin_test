package org.springframework.scheduling.config;

import java.util.concurrent.ScheduledFuture;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/config/ScheduledTask.class */
public final class ScheduledTask {
    volatile ScheduledFuture<?> future;

    ScheduledTask() {
    }

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
