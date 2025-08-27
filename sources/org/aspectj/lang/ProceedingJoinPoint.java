package org.aspectj.lang;

import org.aspectj.runtime.internal.AroundClosure;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/ProceedingJoinPoint.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/ProceedingJoinPoint.class */
public interface ProceedingJoinPoint extends JoinPoint {
    void set$AroundClosure(AroundClosure aroundClosure);

    Object proceed() throws Throwable;

    Object proceed(Object[] objArr) throws Throwable;
}
