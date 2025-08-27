package org.springframework.scheduling;

import java.util.Date;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/TriggerContext.class */
public interface TriggerContext {
    Date lastScheduledExecutionTime();

    Date lastActualExecutionTime();

    Date lastCompletionTime();
}
