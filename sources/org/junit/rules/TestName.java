package org.junit.rules;

import org.junit.runner.Description;

/* loaded from: junit-4.12.jar:org/junit/rules/TestName.class */
public class TestName extends TestWatcher {
    private String name;

    @Override // org.junit.rules.TestWatcher
    protected void starting(Description d) {
        this.name = d.getMethodName();
    }

    public String getMethodName() {
        return this.name;
    }
}
