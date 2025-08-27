package org.aspectj.weaver.ast;

import org.aspectj.weaver.Member;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Call.class */
public class Call extends Test {
    private final Member method;
    private final Expr[] args;

    public Call(Member m, Expr[] args) {
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
}
