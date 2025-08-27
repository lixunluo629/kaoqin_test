package com.graphbuilder.math;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/VarNode.class */
public class VarNode extends TermNode {
    public VarNode(String name, boolean negate) {
        super(name, negate);
    }

    @Override // com.graphbuilder.math.Expression
    public double eval(VarMap v, FuncMap f) {
        double val = v.getValue(this.name);
        if (this.negate) {
            val = -val;
        }
        return val;
    }
}
