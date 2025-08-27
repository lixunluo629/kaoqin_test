package org.aspectj.weaver.ast;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Literal.class */
public final class Literal extends Test {
    boolean noTest;
    boolean val;
    public static final Literal TRUE = new Literal(true, false);
    public static final Literal FALSE = new Literal(false, false);

    private Literal(boolean val, boolean noTest) {
        this.val = val;
        this.noTest = noTest;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public String toString() {
        return this.noTest ? "NO_TEST" : this.val ? "TRUE" : "FALSE";
    }
}
