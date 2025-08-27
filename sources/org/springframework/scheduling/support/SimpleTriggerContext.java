package org.springframework.scheduling.support;

import java.util.Date;
import org.springframework.scheduling.TriggerContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/support/SimpleTriggerContext.class */
public class SimpleTriggerContext implements TriggerContext {
    private volatile Date lastScheduledExecutionTime;
    private volatile Date lastActualExecutionTime;
    private volatile Date lastCompletionTime;

    public SimpleTriggerContext() {
    }

    public SimpleTriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
        this.lastScheduledExecutionTime = lastScheduledExecutionTime;
        this.lastActualExecutionTime = lastActualExecutionTime;
        this.lastCompletionTime = lastCompletionTime;
    }

    public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
        this.lastScheduledExecutionTime = lastScheduledExecutionTime;
        this.lastActualExecutionTime = lastActualExecutionTime;
        this.lastCompletionTime = lastCompletionTime;
    }

    @Override // org.springframework.scheduling.TriggerContext
    public Date lastScheduledExecutionTime() {
        return this.lastScheduledExecutionTime;
    }

    @Override // org.springframework.scheduling.TriggerContext
    public Date lastActualExecutionTime() {
        return this.lastActualExecutionTime;
    }

    @Override // org.springframework.scheduling.TriggerContext
    public Date lastCompletionTime() {
        return this.lastCompletionTime;
    }
}
