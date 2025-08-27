package org.aspectj.weaver.ast;

import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/CallExpr.class */
public class CallExpr extends Expr {
    private final Member method;
    private final Expr[] args;
    private final ResolvedType returnType;

    public CallExpr(Member m, Expr[] args, ResolvedType returnType) {
        this.method = m;
        this.args = args;
        this.returnType = returnType;
    }

    @Override // org.aspectj.weaver.ast.Expr
    public void accept(IExprVisitor v) {
        v.visit(this);
    }

    public Expr[] getArgs() {
        return this.args;
    }

    public Member getMethod() {
        return this.method;
    }

    @Override // org.aspectj.weaver.ast.Expr
    public ResolvedType getType() {
        return this.returnType;
    }
}
