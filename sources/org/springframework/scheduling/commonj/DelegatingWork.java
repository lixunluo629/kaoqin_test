package org.springframework.scheduling.commonj;

import commonj.work.Work;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/commonj/DelegatingWork.class */
public class DelegatingWork implements Work {
    private final Runnable delegate;

    public DelegatingWork(Runnable delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }

    public final Runnable getDelegate() {
        return this.delegate;
    }

    public void run() {
        this.delegate.run();
    }

    public boolean isDaemon() {
        return (this.delegate instanceof SchedulingAwareRunnable) && ((SchedulingAwareRunnable) this.delegate).isLongLived();
    }

    public void release() {
    }
}
