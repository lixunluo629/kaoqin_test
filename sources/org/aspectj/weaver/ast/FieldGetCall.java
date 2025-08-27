package org.aspectj.weaver.ast;

import org.aspectj.weaver.Member;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/FieldGetCall.class */
public class FieldGetCall extends Test {
    private final Member field;
    private final Member method;
    private final Expr[] args;

    public FieldGetCall(Member f, Member m, Expr[] args) {
        this.field = f;
        this.method = m;
        this.args = args;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public Expr[] getArgs() {
        return this.args;
    }

    public Member getMethod() {
        return this.method;
    }

    public Member getField() {
        return this.field;
    }
}
