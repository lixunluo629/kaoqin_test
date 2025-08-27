package org.aspectj.weaver.ast;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/And.class */
public class And extends Test {
    Test left;
    Test right;

    public And(Test left, Test right) {
        this.left = left;
        this.right = right;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public String toString() {
        return "(" + this.left + " && " + this.right + ")";
    }

    public boolean equals(Object other) {
        if (other instanceof And) {
            And o = (And) other;
            return o.left.equals(this.left) && o.right.equals(this.right);
        }
        return false;
    }

    public Test getLeft() {
        return this.left;
    }

    public Test getRight() {
        return this.right;
    }
}
