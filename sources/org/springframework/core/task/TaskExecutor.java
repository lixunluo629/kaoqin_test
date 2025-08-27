package org.springframework.core.task;

import java.util.concurrent.Executor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/task/TaskExecutor.class */
public interface TaskExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);
}
