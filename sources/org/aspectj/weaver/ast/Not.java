package org.aspectj.weaver.ast;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Not.class */
public class Not extends Test {
    Test test;

    public Not(Test test) {
        this.test = test;
    }

    @Override // org.aspectj.weaver.ast.Test
    public void accept(ITestVisitor v) {
        v.visit(this);
    }

    public Test getBody() {
        return this.test;
    }

    public String toString() {
        return "!" + this.test;
    }

    public boolean equals(Object other) {
        if (other instanceof Not) {
            Not o = (Not) other;
            return o.test.equals(this.test);
        }
        return false;
    }

    public int hashCode() {
        return this.test.hashCode();
    }
}
