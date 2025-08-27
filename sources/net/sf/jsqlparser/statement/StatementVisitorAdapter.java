package net.sf.jsqlparser.statement;

import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/StatementVisitorAdapter.class */
public class StatementVisitorAdapter implements StatementVisitor {
    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Select select) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Delete delete) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Update update) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Insert insert) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Replace replace) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Drop drop) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Truncate truncate) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateIndex createIndex) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateTable createTable) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateView createView) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Alter alter) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Statements stmts) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Execute execute) {
    }
}
