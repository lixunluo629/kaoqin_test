package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.statement.select.SetOperationList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/MinusOp.class */
public class MinusOp extends SetOperation {
    public MinusOp() {
        super(SetOperationList.SetOperationType.MINUS);
    }
}
