package org.junit.runners.model;

/* loaded from: junit-4.12.jar:org/junit/runners/model/RunnerScheduler.class */
public interface RunnerScheduler {
    void schedule(Runnable runnable);

    void finished();
}
