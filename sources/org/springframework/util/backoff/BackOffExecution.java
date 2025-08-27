package org.springframework.util.backoff;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/backoff/BackOffExecution.class */
public interface BackOffExecution {
    public static final long STOP = -1;

    long nextBackOff();
}
