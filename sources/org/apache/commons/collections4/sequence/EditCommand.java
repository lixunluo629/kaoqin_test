package org.apache.commons.collections4.sequence;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/EditCommand.class */
public abstract class EditCommand<T> {
    private final T object;

    public abstract void accept(CommandVisitor<T> commandVisitor);

    protected EditCommand(T object) {
        this.object = object;
    }

    protected T getObject() {
        return this.object;
    }
}
