package org.aspectj.internal.lang.reflect;

import org.aspectj.lang.reflect.PerClause;
import org.aspectj.lang.reflect.PerClauseKind;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/PerClauseImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/PerClauseImpl.class */
public class PerClauseImpl implements PerClause {
    private final PerClauseKind kind;

    protected PerClauseImpl(PerClauseKind kind) {
        this.kind = kind;
    }

    @Override // org.aspectj.lang.reflect.PerClause
    public PerClauseKind getKind() {
        return this.kind;
    }

    public String toString() {
        return "issingleton()";
    }
}
