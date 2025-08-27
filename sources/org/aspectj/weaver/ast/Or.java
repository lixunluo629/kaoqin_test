package org.aspectj.weaver.ast;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Or.class */
public class Or extends Test {
    Test left;
    Test right;

    public Or(Test left, Test right) {
        this.left = left;
        this.right = right;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public String toString() {
        return "(" + this.left + " || " + this.right + ")";
    }

    public boolean equals(Object other) {
        if (other instanceof Or) {
            Or o = (Or) other;
            return o.left.equals(this.left) && o.right.equals(this.right);
        }
        return false;
    }

    public int hashCode() {
        int result = (37 * 19) + this.left.hashCode();
        return (37 * result) + this.right.hashCode();
    }

    public Test getLeft() {
        return this.left;
    }

    public Test getRight() {
        return this.right;
    }
}
