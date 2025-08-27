package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/AddNode.class */
public class AddNode extends OpNode {
    public AddNode(Expression leftChild, Expression rightChild) {
        super(leftChild, rightChild);
    }

    @Override // com.graphbuilder.math.Expression
    public double eval(VarMap v, FuncMap f) {
        double a = this.leftChild.eval(v, f);
        double b = this.rightChild.eval(v, f);
        return a + b;
    }

    @Override // com.graphbuilder.math.OpNode
    public String getSymbol() {
        return "+";
    }
}
