package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/SchedulingTaskExecutor.class */
public interface SchedulingTaskExecutor extends AsyncTaskExecutor {
    boolean prefersShortLivedTasks();
}
