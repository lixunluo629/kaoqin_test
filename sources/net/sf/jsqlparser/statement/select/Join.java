package net.sf.jsqlparser.statement.select;

import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/Join.class */
public class Join {
    private boolean outer = false;
    private boolean right = false;
    private boolean left = false;
    private boolean natural = false;
    private boolean full = false;
    private boolean inner = false;
    private boolean simple = false;
    private boolean cross = false;
    private FromItem rightItem;
    private Expression onExpression;
    private List<Column> usingColumns;

    public boolean isSimple() {
        return this.simple;
    }

    public void setSimple(boolean b) {
        this.simple = b;
    }

    public boolean isInner() {
        return this.inner;
    }

    public void setInner(boolean b) {
        this.inner = b;
    }

    public boolean isOuter() {
        return this.outer;
    }

    public void setOuter(boolean b) {
        this.outer = b;
    }

    public boolean isLeft() {
        return this.left;
    }

    public void setLeft(boolean b) {
        this.left = b;
    }

    public boolean isRight() {
        return this.right;
    }

    public void setRight(boolean b) {
        this.right = b;
    }

    public boolean isNatural() {
        return this.natural;
    }

    public void setNatural(boolean b) {
        this.natural = b;
    }

    public boolean isFull() {
        return this.full;
    }

    public void setFull(boolean b) {
        this.full = b;
    }

    public boolean isCross() {
        return this.cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    public FromItem getRightItem() {
        return this.rightItem;
    }

    public void setRightItem(FromItem item) {
        this.rightItem = item;
    }

    public Expression getOnExpression() {
        return this.onExpression;
    }

    public void setOnExpression(Expression expression) {
        this.onExpression = expression;
    }

    public List<Column> getUsingColumns() {
        return this.usingColumns;
    }

    public void setUsingColumns(List<Column> list) {
        this.usingColumns = list;
    }

    public String toString() {
        if (isSimple()) {
            return "" + this.rightItem;
        }
        String type = "";
        if (isRight()) {
            type = type + "RIGHT ";
        } else if (isNatural()) {
            type = type + "NATURAL ";
        } else if (isFull()) {
            type = type + "FULL ";
        } else if (isLeft()) {
            type = type + "LEFT ";
        } else if (isCross()) {
            type = type + "CROSS ";
        }
        if (isOuter()) {
            type = type + "OUTER ";
        } else if (isInner()) {
            type = type + "INNER ";
        }
        return type + "JOIN " + this.rightItem + (this.onExpression != null ? " ON " + this.onExpression + "" : "") + PlainSelect.getFormatedList(this.usingColumns, "USING", true, true);
    }
}
