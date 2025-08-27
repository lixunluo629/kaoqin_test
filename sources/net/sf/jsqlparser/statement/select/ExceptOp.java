package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.statement.select.SetOperationList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/ExceptOp.class */
public class ExceptOp extends SetOperation {
    public ExceptOp() {
        super(SetOperationList.SetOperationType.EXCEPT);
    }
}
