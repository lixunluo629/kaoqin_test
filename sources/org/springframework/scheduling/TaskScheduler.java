package org.springframework.scheduling;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/TaskScheduler.class */
public interface TaskScheduler {
    ScheduledFuture<?> schedule(Runnable runnable, Trigger trigger);

    ScheduledFuture<?> schedule(Runnable runnable, Date date);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, Date date, long j);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, Date date, long j);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j);
}
