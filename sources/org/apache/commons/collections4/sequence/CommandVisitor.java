package org.apache.commons.collections4.sequence;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/sequence/CommandVisitor.class */
public interface CommandVisitor<T> {
    void visitInsertCommand(T t);

    void visitKeepCommand(T t);

    void visitDeleteCommand(T t);
}
