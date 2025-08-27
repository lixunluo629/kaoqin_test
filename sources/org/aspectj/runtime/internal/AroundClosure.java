package org.aspectj.runtime.internal;

import org.aspectj.lang.ProceedingJoinPoint;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/AroundClosure.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/AroundClosure.class */
public abstract class AroundClosure {
    protected Object[] state;
    protected int bitflags = 1048576;
    protected Object[] preInitializationState;

    public abstract Object run(Object[] objArr) throws Throwable;

    public AroundClosure() {
    }

    public AroundClosure(Object[] state) {
        this.state = state;
    }

    public int getFlags() {
        return this.bitflags;
    }

    public Object[] getState() {
        return this.state;
    }

    public Object[] getPreInitializationState() {
        return this.preInitializationState;
    }

    public ProceedingJoinPoint linkClosureAndJoinPoint() {
        ProceedingJoinPoint jp = (ProceedingJoinPoint) this.state[this.state.length - 1];
        jp.set$AroundClosure(this);
        return jp;
    }

    public ProceedingJoinPoint linkClosureAndJoinPoint(int flags) {
        ProceedingJoinPoint jp = (ProceedingJoinPoint) this.state[this.state.length - 1];
        jp.set$AroundClosure(this);
        this.bitflags = flags;
        return jp;
    }
}
