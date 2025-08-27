package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.statement.select.SetOperationList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/UnionOp.class */
public class UnionOp extends SetOperation {
    private boolean distinct;
    private boolean all;

    public UnionOp() {
        super(SetOperationList.SetOperationType.UNION);
    }

    public boolean isAll() {
        return this.all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override // net.sf.jsqlparser.statement.select.SetOperation
    public String toString() {
        String allDistinct = "";
        if (isAll()) {
            allDistinct = " ALL";
        } else if (isDistinct()) {
            allDistinct = " DISTINCT";
        }
        return super.toString() + allDistinct;
    }
}
