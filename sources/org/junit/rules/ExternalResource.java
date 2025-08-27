package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/* loaded from: junit-4.12.jar:org/junit/rules/ExternalResource.class */
public abstract class ExternalResource implements TestRule {
    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        return statement(base);
    }

    private Statement statement(final Statement base) {
        return new Statement() { // from class: org.junit.rules.ExternalResource.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                ExternalResource.this.before();
                try {
                    base.evaluate();
                    ExternalResource.this.after();
                } catch (Throwable th) {
                    ExternalResource.this.after();
                    throw th;
                }
            }
        };
    }

    protected void before() throws Throwable {
    }

    protected void after() {
    }
}
