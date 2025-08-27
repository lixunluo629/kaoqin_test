package org.springframework.aop.target;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/target/ThreadLocalTargetSourceStats.class */
public interface ThreadLocalTargetSourceStats {
    int getInvocationCount();

    int getHitCount();

    int getObjectCount();
}
