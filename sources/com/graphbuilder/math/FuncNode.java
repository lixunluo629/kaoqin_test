package com.graphbuilder.math;

import com.graphbuilder.struc.Bag;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/FuncNode.class */
public class FuncNode extends TermNode {
    private Bag bag;
    private double[] of;

    public FuncNode(String name, boolean negate) {
        super(name, negate);
        this.bag = new Bag(1);
        this.of = new double[1];
    }

    public void add(Expression x) {
        insert(x, this.bag.size());
    }

    public void insert(Expression x, int i) {
        checkBeforeAccept(x);
        int oldCap = this.bag.getCapacity();
        this.bag.insert(x, i);
        int newCap = this.bag.getCapacity();
        if (oldCap != newCap) {
            this.of = new double[newCap];
        }
        x.parent = this;
    }

    public void remove(Expression x) {
        int size = this.bag.size();
        this.bag.remove(x);
        if (size != this.bag.size()) {
            x.parent = null;
        }
    }

    public int numChildren() {
        return this.bag.size();
    }

    public Expression child(int i) {
        return (Expression) this.bag.get(i);
    }

    @Override // com.graphbuilder.math.Expression
    public double eval(VarMap v, FuncMap f) {
        int numParam = this.bag.size();
        for (int i = 0; i < numParam; i++) {
            this.of[i] = child(i).eval(v, f);
        }
        double result = f.getFunction(this.name, numParam).of(this.of, numParam);
        if (this.negate) {
            result = -result;
        }
        return result;
    }
}
