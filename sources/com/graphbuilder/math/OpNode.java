package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/OpNode.class */
public abstract class OpNode extends Expression {
    protected Expression leftChild = null;
    protected Expression rightChild = null;

    public abstract String getSymbol();

    public OpNode(Expression leftChild, Expression rightChild) {
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }

    public void setLeftChild(Expression x) {
        checkBeforeAccept(x);
        if (this.leftChild != null) {
            this.leftChild.parent = null;
        }
        x.parent = this;
        this.leftChild = x;
    }

    public void setRightChild(Expression x) {
        checkBeforeAccept(x);
        if (this.rightChild != null) {
            this.rightChild.parent = null;
        }
        x.parent = this;
        this.rightChild = x;
    }

    public Expression getLeftChild() {
        return this.leftChild;
    }

    public Expression getRightChild() {
        return this.rightChild;
    }
}
