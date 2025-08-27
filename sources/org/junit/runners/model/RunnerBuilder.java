package org.junit.runners.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;

/* loaded from: junit-4.12.jar:org/junit/runners/model/RunnerBuilder.class */
public abstract class RunnerBuilder {
    private final Set<Class<?>> parents = new HashSet();

    public abstract Runner runnerForClass(Class<?> cls) throws Throwable;

    public Runner safeRunnerForClass(Class<?> testClass) {
        try {
            return runnerForClass(testClass);
        } catch (Throwable e) {
            return new ErrorReportingRunner(testClass, e);
        }
    }

    Class<?> addParent(Class<?> parent) throws InitializationError {
        if (!this.parents.add(parent)) {
            throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
        }
        return parent;
    }

    void removeParent(Class<?> klass) {
        this.parents.remove(klass);
    }

    public List<Runner> runners(Class<?> parent, Class<?>[] children) throws InitializationError {
        addParent(parent);
        try {
            List<Runner> listRunners = runners(children);
            removeParent(parent);
            return listRunners;
        } catch (Throwable th) {
            removeParent(parent);
            throw th;
        }
    }

    public List<Runner> runners(Class<?> parent, List<Class<?>> children) throws InitializationError {
        return runners(parent, (Class<?>[]) children.toArray(new Class[0]));
    }

    private List<Runner> runners(Class<?>[] children) {
        ArrayList<Runner> runners = new ArrayList<>();
        for (Class<?> each : children) {
            Runner childRunner = safeRunnerForClass(each);
            if (childRunner != null) {
                runners.add(childRunner);
            }
        }
        return runners;
    }
}
