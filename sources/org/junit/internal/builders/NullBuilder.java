package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/* loaded from: junit-4.12.jar:org/junit/internal/builders/NullBuilder.class */
public class NullBuilder extends RunnerBuilder {
    @Override // org.junit.runners.model.RunnerBuilder
    public Runner runnerForClass(Class<?> each) throws Throwable {
        return null;
    }
}
