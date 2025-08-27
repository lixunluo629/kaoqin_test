package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

/* loaded from: junit-4.12.jar:org/junit/internal/runners/statements/Fail.class */
public class Fail extends Statement {
    private final Throwable error;

    public Fail(Throwable e) {
        this.error = e;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Throwable {
        throw this.error;
    }
}
