package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/ValNode.class */
public class ValNode extends Expression {
    protected double val;

    public ValNode(double d) {
        this.val = 0.0d;
        this.val = d;
    }

    @Override // com.graphbuilder.math.Expression
    public double eval(VarMap v, FuncMap f) {
        return this.val;
    }

    public double getValue() {
        return this.val;
    }

    public void setValue(double d) {
        this.val = d;
    }
}
