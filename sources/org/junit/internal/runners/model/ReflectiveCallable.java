package org.junit.internal.runners.model;

import java.lang.reflect.InvocationTargetException;

/* loaded from: junit-4.12.jar:org/junit/internal/runners/model/ReflectiveCallable.class */
public abstract class ReflectiveCallable {
    protected abstract Object runReflectiveCall() throws Throwable;

    public Object run() throws Throwable {
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
