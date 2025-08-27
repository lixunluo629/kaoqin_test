package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.statement.select.SetOperationList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/IntersectOp.class */
public class IntersectOp extends SetOperation {
    public IntersectOp() {
        super(SetOperationList.SetOperationType.INTERSECT);
    }
}
