package org.springframework.scheduling.quartz;

import org.springframework.core.NestedRuntimeException;
import org.springframework.util.MethodInvoker;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/quartz/JobMethodInvocationFailedException.class */
public class JobMethodInvocationFailedException extends NestedRuntimeException {
    public JobMethodInvocationFailedException(MethodInvoker methodInvoker, Throwable cause) {
        super("Invocation of method '" + methodInvoker.getTargetMethod() + "' on target class [" + methodInvoker.getTargetClass() + "] failed", cause);
    }
}
