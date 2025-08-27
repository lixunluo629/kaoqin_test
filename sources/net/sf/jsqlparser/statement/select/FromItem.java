package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Alias;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/FromItem.class */
public interface FromItem {
    void accept(FromItemVisitor fromItemVisitor);

    Alias getAlias();

    void setAlias(Alias alias);

    Pivot getPivot();

    void setPivot(Pivot pivot);
}
