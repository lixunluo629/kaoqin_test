package org.apache.commons.collections4.sequence;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/DeleteCommand.class */
public class DeleteCommand<T> extends EditCommand<T> {
    public DeleteCommand(T object) {
        super(object);
    }

    @Override // org.apache.commons.collections4.sequence.EditCommand
    public void accept(CommandVisitor<T> visitor) {
        visitor.visitDeleteCommand(getObject());
    }
}
