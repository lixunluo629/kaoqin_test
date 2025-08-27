package com.graphbuilder.math;

import com.graphbuilder.struc.Bag;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/math/Expression.class */
public abstract class Expression {
    protected Expression parent = null;

    public abstract double eval(VarMap varMap, FuncMap funcMap);

    public boolean isDescendent(Expression x) {
        Expression expression = this;
        while (true) {
            Expression y = expression;
            if (y != null) {
                if (y == x) {
                    return true;
                }
                expression = y.parent;
            } else {
                return false;
            }
        }
    }

    public Expression getParent() {
        return this.parent;
    }

    protected void checkBeforeAccept(Expression x) {
        if (x == null) {
            throw new IllegalArgumentException("expression cannot be null");
        }
        if (x.parent != null) {
            throw new IllegalArgumentException("expression must be removed parent");
        }
        if (isDescendent(x)) {
            throw new IllegalArgumentException("cyclic reference");
        }
    }

    public String[] getVariableNames() {
        return getTermNames(true);
    }

    public String[] getFunctionNames() {
        return getTermNames(false);
    }

    private String[] getTermNames(boolean varNames) {
        Bag b = new Bag();
        getTermNames(this, b, varNames);
        String[] arr = new String[b.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (String) b.get(i);
        }
        return arr;
    }

    private static void getTermNames(Expression x, Bag b, boolean varNames) {
        if (x instanceof OpNode) {
            OpNode o = (OpNode) x;
            getTermNames(o.leftChild, b, varNames);
            getTermNames(o.rightChild, b, varNames);
            return;
        }
        if (x instanceof VarNode) {
            if (varNames) {
                VarNode v = (VarNode) x;
                if (!b.contains(v.name)) {
                    b.add(v.name);
                    return;
                }
                return;
            }
            return;
        }
        if (x instanceof FuncNode) {
            FuncNode f = (FuncNode) x;
            if (!varNames && !b.contains(f.name)) {
                b.add(f.name);
            }
            for (int i = 0; i < f.numChildren(); i++) {
                getTermNames(f.child(i), b, varNames);
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(this, sb);
        return sb.toString();
    }

    private static void toString(Expression x, StringBuffer sb) {
        if (x instanceof OpNode) {
            OpNode o = (OpNode) x;
            sb.append("(");
            toString(o.leftChild, sb);
            sb.append(o.getSymbol());
            toString(o.rightChild, sb);
            sb.append(")");
            return;
        }
        if (x instanceof TermNode) {
            TermNode t = (TermNode) x;
            if (t.getNegate()) {
                sb.append("(");
                sb.append("-");
            }
            sb.append(t.getName());
            if (t instanceof FuncNode) {
                FuncNode f = (FuncNode) t;
                sb.append("(");
                if (f.numChildren() > 0) {
                    toString(f.child(0), sb);
                }
                for (int i = 1; i < f.numChildren(); i++) {
                    sb.append(", ");
                    toString(f.child(i), sb);
                }
                sb.append(")");
            }
            if (t.getNegate()) {
                sb.append(")");
                return;
            }
            return;
        }
        if (x instanceof ValNode) {
            sb.append(((ValNode) x).val);
        }
    }
}
