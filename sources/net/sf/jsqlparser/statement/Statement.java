package net.sf.jsqlparser.statement;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/Statement.class */
public interface Statement {
    void accept(StatementVisitor statementVisitor);
}
