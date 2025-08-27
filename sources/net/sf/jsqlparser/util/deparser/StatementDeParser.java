package net.sf.jsqlparser.util.deparser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
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
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/StatementDeParser.class */
public class StatementDeParser implements StatementVisitor {
    private StringBuilder buffer;

    public StatementDeParser(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateIndex createIndex) {
        CreateIndexDeParser createIndexDeParser = new CreateIndexDeParser(this.buffer);
        createIndexDeParser.deParse(createIndex);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateTable createTable) {
        CreateTableDeParser createTableDeParser = new CreateTableDeParser(this.buffer);
        createTableDeParser.deParse(createTable);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(CreateView createView) {
        CreateViewDeParser createViewDeParser = new CreateViewDeParser(this.buffer);
        createViewDeParser.deParse(createView);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Delete delete) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        DeleteDeParser deleteDeParser = new DeleteDeParser(expressionDeParser, this.buffer);
        deleteDeParser.deParse(delete);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Drop drop) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Insert insert) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser, selectDeParser, this.buffer);
        insertDeParser.deParse(insert);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Replace replace) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        ReplaceDeParser replaceDeParser = new ReplaceDeParser(expressionDeParser, selectDeParser, this.buffer);
        replaceDeParser.deParse(replace);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Select select) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        if (select.getWithItemsList() != null && !select.getWithItemsList().isEmpty()) {
            this.buffer.append("WITH ");
            Iterator<WithItem> iter = select.getWithItemsList().iterator();
            while (iter.hasNext()) {
                WithItem withItem = iter.next();
                this.buffer.append(withItem);
                if (iter.hasNext()) {
                    this.buffer.append(",");
                }
                this.buffer.append(SymbolConstants.SPACE_SYMBOL);
            }
        }
        select.getSelectBody().accept(selectDeParser);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Truncate truncate) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Update update) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        UpdateDeParser updateDeParser = new UpdateDeParser(expressionDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        updateDeParser.deParse(update);
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Alter alter) {
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Statements stmts) {
        stmts.accept(this);
    }

    @Override // net.sf.jsqlparser.statement.StatementVisitor
    public void visit(Execute execute) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(this.buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, this.buffer);
        ExecuteDeParser executeDeParser = new ExecuteDeParser(expressionDeParser, this.buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        executeDeParser.deParse(execute);
    }
}
