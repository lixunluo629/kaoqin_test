package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/FunctionItem.class */
public class FunctionItem {
    private Function function;
    private Alias alias;

    public Alias getAlias() {
        return this.alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public Function getFunction() {
        return this.function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String toString() {
        return this.function + (this.alias != null ? this.alias.toString() : "");
    }
}
