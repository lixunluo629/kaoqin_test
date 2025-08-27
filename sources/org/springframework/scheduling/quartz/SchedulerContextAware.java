package org.springframework.scheduling.quartz;

import org.quartz.SchedulerContext;
import org.springframework.beans.factory.Aware;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/quartz/SchedulerContextAware.class */
public interface SchedulerContextAware extends Aware {
    void setSchedulerContext(SchedulerContext schedulerContext);
}
