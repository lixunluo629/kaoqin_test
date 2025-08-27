package org.junit.internal.runners.statements;

import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/* loaded from: junit-4.12.jar:org/junit/internal/runners/statements/RunBefores.class */
public class RunBefores extends Statement {
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> befores;

    public RunBefores(Statement next, List<FrameworkMethod> befores, Object target) {
        this.next = next;
        this.befores = befores;
        this.target = target;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Throwable {
        for (FrameworkMethod before : this.befores) {
            before.invokeExplosively(this.target, new Object[0]);
        }
        this.next.evaluate();
    }
}
