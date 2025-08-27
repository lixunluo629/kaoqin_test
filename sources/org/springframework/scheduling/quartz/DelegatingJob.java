package org.springframework.scheduling.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/quartz/DelegatingJob.class */
public class DelegatingJob implements Job {
    private final Runnable delegate;

    public DelegatingJob(Runnable delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }

    public final Runnable getDelegate() {
        return this.delegate;
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.delegate.run();
    }
}
