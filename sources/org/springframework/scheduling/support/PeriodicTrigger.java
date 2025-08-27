package org.springframework.scheduling.support;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/support/PeriodicTrigger.class */
public class PeriodicTrigger implements Trigger {
    private final long period;
    private final TimeUnit timeUnit;
    private volatile long initialDelay;
    private volatile boolean fixedRate;

    public PeriodicTrigger(long period) {
        this(period, null);
    }

    public PeriodicTrigger(long period, TimeUnit timeUnit) {
        this.initialDelay = 0L;
        this.fixedRate = false;
        Assert.isTrue(period >= 0, "period must not be negative");
        this.timeUnit = timeUnit != null ? timeUnit : TimeUnit.MILLISECONDS;
        this.period = this.timeUnit.toMillis(period);
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = this.timeUnit.toMillis(initialDelay);
    }

    public void setFixedRate(boolean fixedRate) {
        this.fixedRate = fixedRate;
    }

    @Override // org.springframework.scheduling.Trigger
    public Date nextExecutionTime(TriggerContext triggerContext) {
        if (triggerContext.lastScheduledExecutionTime() == null) {
            return new Date(System.currentTimeMillis() + this.initialDelay);
        }
        if (this.fixedRate) {
            return new Date(triggerContext.lastScheduledExecutionTime().getTime() + this.period);
        }
        return new Date(triggerContext.lastCompletionTime().getTime() + this.period);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PeriodicTrigger)) {
            return false;
        }
        PeriodicTrigger other = (PeriodicTrigger) obj;
        return this.fixedRate == other.fixedRate && this.initialDelay == other.initialDelay && this.period == other.period;
    }

    public int hashCode() {
        return (this.fixedRate ? 17 : 29) + ((int) (37 * this.period)) + ((int) (41 * this.initialDelay));
    }
}
