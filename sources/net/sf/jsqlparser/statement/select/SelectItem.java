package net.sf.jsqlparser.statement.select;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/SelectItem.class */
public interface SelectItem {
    void accept(SelectItemVisitor selectItemVisitor);
}
