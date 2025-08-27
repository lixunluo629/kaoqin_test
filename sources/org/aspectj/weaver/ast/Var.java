package org.aspectj.weaver.ast;

import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Var.class */
public class Var extends Expr {
    public static final Var[] NONE = new Var[0];
    ResolvedType variableType;

    public Var(ResolvedType variableType) {
        this.variableType = variableType;
    }

    @Override // org.aspectj.weaver.ast.Expr
    public ResolvedType getType() {
        return this.variableType;
    }

    public String toString() {
        return "(Var " + this.variableType + ")";
    }

    @Override // org.aspectj.weaver.ast.Expr
    public void accept(IExprVisitor v) {
        v.visit(this);
    }

    public Var getAccessorForValue(ResolvedType formalType, String formalName) {
        throw new IllegalStateException("Only makes sense for annotation variables");
    }
}
