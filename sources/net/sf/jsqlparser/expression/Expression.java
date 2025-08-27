package net.sf.jsqlparser.expression;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/Expression.class */
public interface Expression {
    void accept(ExpressionVisitor expressionVisitor);
}
