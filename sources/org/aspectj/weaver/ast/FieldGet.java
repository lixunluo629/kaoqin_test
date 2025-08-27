package org.aspectj.weaver.ast;

import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/FieldGet.class */
public class FieldGet extends Expr {
    Member field;
    ResolvedType resolvedType;

    public FieldGet(Member field, ResolvedType resolvedType) {
        this.field = field;
        this.resolvedType = resolvedType;
    }

    @Override // org.aspectj.weaver.ast.Expr
    public ResolvedType getType() {
        return this.resolvedType;
    }

    public String toString() {
        return "(FieldGet " + this.field + ")";
    }

    @Override // org.aspectj.weaver.ast.Expr
    public void accept(IExprVisitor v) {
        v.visit(this);
    }

    public Member getField() {
        return this.field;
    }
}
