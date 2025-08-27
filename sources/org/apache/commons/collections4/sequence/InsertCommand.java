package org.apache.commons.collections4.sequence;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/InsertCommand.class */
public class InsertCommand<T> extends EditCommand<T> {
    public InsertCommand(T object) {
        super(object);
    }

    @Override // org.apache.commons.collections4.sequence.EditCommand
    public void accept(CommandVisitor<T> visitor) {
        visitor.visitInsertCommand(getObject());
    }
}
