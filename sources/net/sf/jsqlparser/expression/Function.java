package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/Function.class */
public class Function implements Expression {
    private String name;
    private ExpressionList parameters;
    private boolean allColumns = false;
    private boolean distinct = false;
    private boolean isEscaped = false;

    @Override // net.sf.jsqlparser.expression.Expression
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public boolean isAllColumns() {
        return this.allColumns;
    }

    public void setAllColumns(boolean b) {
        this.allColumns = b;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean b) {
        this.distinct = b;
    }

    public ExpressionList getParameters() {
        return this.parameters;
    }

    public void setParameters(ExpressionList list) {
        this.parameters = list;
    }

    public boolean isEscaped() {
        return this.isEscaped;
    }

    public void setEscaped(boolean isEscaped) {
        this.isEscaped = isEscaped;
    }

    public String toString() {
        String params;
        if (this.parameters != null) {
            params = this.parameters.toString();
            if (isDistinct()) {
                params = params.replaceFirst("\\(", "(DISTINCT ");
            } else if (isAllColumns()) {
                params = params.replaceFirst("\\(", "(ALL ");
            }
        } else if (isAllColumns()) {
            params = "(*)";
        } else {
            params = "()";
        }
        String ans = this.name + "" + params + "";
        if (this.isEscaped) {
            ans = "{fn " + ans + "}";
        }
        return ans;
    }
}
