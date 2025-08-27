package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.statement.select.SetOperationList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SetOperation.class */
public abstract class SetOperation {
    private SetOperationList.SetOperationType type;

    public SetOperation(SetOperationList.SetOperationType type) {
        this.type = type;
    }

    public String toString() {
        return this.type.name();
    }
}
