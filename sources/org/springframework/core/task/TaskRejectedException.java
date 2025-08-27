package org.springframework.core.task;

import java.util.concurrent.RejectedExecutionException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/task/TaskRejectedException.class */
public class TaskRejectedException extends RejectedExecutionException {
    public TaskRejectedException(String msg) {
        super(msg);
    }

    public TaskRejectedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
