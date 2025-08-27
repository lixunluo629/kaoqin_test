package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Request;
import org.junit.runner.Runner;

/* loaded from: junit-4.12.jar:org/junit/internal/requests/ClassRequest.class */
public class ClassRequest extends Request {
    private final Object runnerLock;
    private final Class<?> fTestClass;
    private final boolean canUseSuiteMethod;
    private volatile Runner runner;

    public ClassRequest(Class<?> testClass, boolean canUseSuiteMethod) {
        this.runnerLock = new Object();
        this.fTestClass = testClass;
        this.canUseSuiteMethod = canUseSuiteMethod;
    }

    public ClassRequest(Class<?> testClass) {
        this(testClass, true);
    }

    @Override // org.junit.runner.Request
    public Runner getRunner() {
        if (this.runner == null) {
            synchronized (this.runnerLock) {
                if (this.runner == null) {
                    this.runner = new AllDefaultPossibilitiesBuilder(this.canUseSuiteMethod).safeRunnerForClass(this.fTestClass);
                }
            }
        }
        return this.runner;
    }
}
