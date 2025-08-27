package org.springframework.aop;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/TargetSource.class */
public interface TargetSource extends TargetClassAware {
    @Override // org.springframework.aop.TargetClassAware
    Class<?> getTargetClass();

    boolean isStatic();

    Object getTarget() throws Exception;

    void releaseTarget(Object obj) throws Exception;
}
