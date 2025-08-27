package net.sf.jsqlparser.statement;

import java.util.List;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/Statements.class */
public class Statements {
    private List<Statement> statements;

    public List<Statement> getStatements() {
        return this.statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Statement stmt : this.statements) {
            b.append(stmt.toString()).append(";\n");
        }
        return b.toString();
    }
}
