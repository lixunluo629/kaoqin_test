package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SubSelect;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/expression/ExpressionVisitorAdapter.class */
public class ExpressionVisitorAdapter implements ExpressionVisitor, ItemsListVisitor {
    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NullValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Function function) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(SignedExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcParameter parameter) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcNamedParameter parameter) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DoubleValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LongValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DateValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimeValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimestampValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(StringValue value) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Addition expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Division expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Multiplication expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Subtraction expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AndExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OrExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Between expr) {
        expr.getLeftExpression().accept(this);
        expr.getBetweenExpressionStart().accept(this);
        expr.getBetweenExpressionEnd().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(EqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThan expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(InExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getLeftItemsList().accept(this);
        expr.getRightItemsList().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IsNullExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LikeExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThan expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NotEqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Column column) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor, net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(SubSelect subSelect) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CaseExpression expr) {
        expr.getSwitchExpression().accept(this);
        for (Expression x : expr.getWhenClauses()) {
            x.accept(this);
        }
        expr.getElseExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(WhenClause expr) {
        expr.getWhenExpression().accept(this);
        expr.getThenExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExistsExpression expr) {
        expr.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AllComparisonExpression expr) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnyComparisonExpression expr) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Concat expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Matches expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseAnd expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseOr expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseXor expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CastExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Modulo expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnalyticExpression expr) {
        expr.getExpression().accept(this);
        expr.getDefaultValue().accept(this);
        expr.getOffset().accept(this);
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }
        expr.getWindowElement().getRange().getStart().getExpression().accept(this);
        expr.getWindowElement().getRange().getEnd().getExpression().accept(this);
        expr.getWindowElement().getOffset().getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExtractExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IntervalExpression expr) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OracleHierarchicalExpression expr) {
        expr.getConnectExpression().accept(this);
        expr.getStartExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMatchOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(ExpressionList expressionList) {
        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }

    protected void visitBinaryExpression(BinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JsonExpression jsonExpr) {
        visit(jsonExpr.getColumn());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMySQLOperator expr) {
        visitBinaryExpression(expr);
    }
}
