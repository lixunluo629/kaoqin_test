package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/* loaded from: junit-4.12.jar:org/junit/internal/runners/statements/RunAfters.class */
public class RunAfters extends Statement {
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> afters;

    public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {
        this.next = next;
        this.afters = afters;
        this.target = target;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Exception {
        List<Throwable> errors = new ArrayList<>();
        try {
            try {
                this.next.evaluate();
                for (FrameworkMethod each : this.afters) {
                    try {
                        each.invokeExplosively(this.target, new Object[0]);
                    } catch (Throwable e) {
                        errors.add(e);
                    }
                }
            } catch (Throwable th) {
                for (FrameworkMethod each2 : this.afters) {
                    try {
                        each2.invokeExplosively(this.target, new Object[0]);
                    } catch (Throwable e2) {
                        errors.add(e2);
                    }
                }
                throw th;
            }
        } catch (Throwable e3) {
            errors.add(e3);
            for (FrameworkMethod each3 : this.afters) {
                try {
                    each3.invokeExplosively(this.target, new Object[0]);
                } catch (Throwable e4) {
                    errors.add(e4);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
