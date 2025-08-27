package com.moredian.onpremise.task.api;

/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/api/CustomSchedule.class */
public interface CustomSchedule<T> {
    boolean saveScheduled(String str);

    boolean saveScheduled(Runnable runnable, String str);

    boolean stopSchedule(String str);

    boolean stopAllSchedule();

    boolean initSchedulePool();

    boolean isCanceled();

    boolean isDone();
}
