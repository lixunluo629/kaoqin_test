package org.springframework.scheduling;

import java.util.Date;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/Trigger.class */
public interface Trigger {
    Date nextExecutionTime(TriggerContext triggerContext);
}
