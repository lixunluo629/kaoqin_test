package org.springframework.boot.diagnostics;

import java.lang.Throwable;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/diagnostics/AbstractFailureAnalyzer.class */
public abstract class AbstractFailureAnalyzer<T extends Throwable> implements FailureAnalyzer {
    protected abstract FailureAnalysis analyze(Throwable th, T t);

    @Override // org.springframework.boot.diagnostics.FailureAnalyzer
    public FailureAnalysis analyze(Throwable th) {
        Throwable thFindCause = findCause(th, getCauseType());
        if (thFindCause != null) {
            return analyze(th, thFindCause);
        }
        return null;
    }

    protected Class<? extends T> getCauseType() {
        return (Class<? extends T>) ResolvableType.forClass(AbstractFailureAnalyzer.class, getClass()).resolveGeneric(new int[0]);
    }

    protected final <E extends Throwable> T findCause(Throwable th, Class<E> cls) {
        while (th != null) {
            if (cls.isInstance(th)) {
                return (T) th;
            }
            th = th.getCause();
        }
        return null;
    }
}
