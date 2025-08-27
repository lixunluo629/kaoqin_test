package org.springframework.core.task;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/task/TaskDecorator.class */
public interface TaskDecorator {
    Runnable decorate(Runnable runnable);
}
