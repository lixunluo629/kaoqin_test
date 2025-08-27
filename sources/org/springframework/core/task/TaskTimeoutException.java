package org.springframework.core.task;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/task/TaskTimeoutException.class */
public class TaskTimeoutException extends TaskRejectedException {
    public TaskTimeoutException(String msg) {
        super(msg);
    }

    public TaskTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
