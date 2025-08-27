package net.sf.jsqlparser.util;

import java.util.LinkedList;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/AddAliasesVisitor.class */
public class AddAliasesVisitor implements SelectVisitor, SelectItemVisitor {
    private List<String> aliases = new LinkedList();
    private boolean firstRun = true;
    private int counter = 0;
    private String prefix = "A";

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(PlainSelect plainSelect) {
        this.firstRun = true;
        this.counter = 0;
        this.aliases.clear();
        for (SelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
        }
        this.firstRun = false;
        for (SelectItem item2 : plainSelect.getSelectItems()) {
            item2.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(SetOperationList setOpList) {
        for (PlainSelect select : setOpList.getPlainSelects()) {
            select.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(SelectExpressionItem selectExpressionItem) {
        String alias;
        if (this.firstRun) {
            if (selectExpressionItem.getAlias() != null) {
                this.aliases.add(selectExpressionItem.getAlias().getName().toUpperCase());
            }
        } else if (selectExpressionItem.getAlias() == null) {
            do {
                alias = getNextAlias().toUpperCase();
            } while (this.aliases.contains(alias));
            this.aliases.add(alias);
            selectExpressionItem.setAlias(new Alias(alias));
        }
    }

    protected String getNextAlias() {
        this.counter++;
        return this.prefix + this.counter;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(WithItem withItem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // net.sf.jsqlparser.statement.select.SelectItemVisitor
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
