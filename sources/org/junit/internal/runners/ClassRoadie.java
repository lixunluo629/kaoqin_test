package org.junit.internal.runners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

@Deprecated
/* loaded from: junit-4.12.jar:org/junit/internal/runners/ClassRoadie.class */
public class ClassRoadie {
    private RunNotifier notifier;
    private TestClass testClass;
    private Description description;
    private final Runnable runnable;

    public ClassRoadie(RunNotifier notifier, TestClass testClass, Description description, Runnable runnable) {
        this.notifier = notifier;
        this.testClass = testClass;
        this.description = description;
        this.runnable = runnable;
    }

    protected void runUnprotected() {
        this.runnable.run();
    }

    protected void addFailure(Throwable targetException) {
        this.notifier.fireTestFailure(new Failure(this.description, targetException));
    }

    public void runProtected() {
        try {
            runBefores();
            runUnprotected();
            runAfters();
        } catch (FailedBefore e) {
            runAfters();
        } catch (Throwable th) {
            runAfters();
            throw th;
        }
    }

    private void runBefores() throws FailedBefore {
        try {
            try {
                List<Method> befores = this.testClass.getBefores();
                for (Method before : befores) {
                    before.invoke(null, new Object[0]);
                }
            } catch (AssumptionViolatedException e) {
                throw new FailedBefore();
            } catch (Throwable e2) {
                addFailure(e2);
                throw new FailedBefore();
            }
        } catch (InvocationTargetException e3) {
            throw e3.getTargetException();
        }
    }

    private void runAfters() {
        List<Method> afters = this.testClass.getAfters();
        for (Method after : afters) {
            try {
                after.invoke(null, new Object[0]);
            } catch (InvocationTargetException e) {
                addFailure(e.getTargetException());
            } catch (Throwable e2) {
                addFailure(e2);
            }
        }
    }
}
