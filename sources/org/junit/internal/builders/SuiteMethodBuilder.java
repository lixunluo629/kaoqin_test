package org.junit.internal.builders;

import junit.runner.BaseTestRunner;
import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/* loaded from: junit-4.12.jar:org/junit/internal/builders/SuiteMethodBuilder.class */
public class SuiteMethodBuilder extends RunnerBuilder {
    @Override // org.junit.runners.model.RunnerBuilder
    public Runner runnerForClass(Class<?> each) throws Throwable {
        if (hasSuiteMethod(each)) {
            return new SuiteMethod(each);
        }
        return null;
    }

    public boolean hasSuiteMethod(Class<?> testClass) throws NoSuchMethodException, SecurityException {
        try {
            testClass.getMethod(BaseTestRunner.SUITE_METHODNAME, new Class[0]);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
