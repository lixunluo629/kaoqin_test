package net.sf.jsqlparser.util;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
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
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/TablesNamesFinder.class */
public class TablesNamesFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor {
    private List<String> tables;
    private List<String> otherItemNames;

    public List<String> getTableList(Delete delete) {
        init();
        this.tables.add(delete.getTable().getName());
        if (delete.getWhere() != null) {
            delete.getWhere().accept(this);
        }
        return this.tables;
    }

    public List<String> getTableList(Insert insert) {
        init();
        this.tables.add(insert.getTable().getName());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }
        return this.tables;
    }

    public List<String> getTableList(Replace replace) {
        init();
        this.tables.add(replace.getTable().getName());
        if (replace.getExpressions() != null) {
            for (Expression expression : replace.getExpressions()) {
                expression.accept(this);
            }
        }
        if (replace.getItemsList() != null) {
            replace.getItemsList().accept(this);
        }
        return this.tables;
    }

    public List<String> getTableList(Select select) {
        init();
        if (select.getWithItemsList() != null) {
            for (WithItem withItem : select.getWithItemsList()) {
                withItem.accept(this);
            }
        }
        select.getSelectBody().accept(this);
        return this.tables;
    }

    public List<String> getTableList(Update update) {
        init();
        for (Table table : update.getTables()) {
            this.tables.add(table.getName());
        }
        if (update.getExpressions() != null) {
            for (Expression expression : update.getExpressions()) {
                expression.accept(this);
            }
        }
        if (update.getFromItem() != null) {
            update.getFromItem().accept(this);
        }
        if (update.getJoins() != null) {
            for (Join join : update.getJoins()) {
                join.getRightItem().accept(this);
            }
        }
        if (update.getWhere() != null) {
            update.getWhere().accept(this);
        }
        return this.tables;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(WithItem withItem) {
        this.otherItemNames.add(withItem.getName().toLowerCase());
        withItem.getSelectBody().accept(this);
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(PlainSelect plainSelect) {
        plainSelect.getFromItem().accept(this);
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(this);
            }
        }
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(Table tableName) {
        String tableWholeName = tableName.getFullyQualifiedName();
        if (!this.otherItemNames.contains(tableWholeName.toLowerCase()) && !this.tables.contains(tableWholeName)) {
            this.tables.add(tableWholeName);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Addition addition) {
        visitBinaryExpression(addition);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        between.getBetweenExpressionStart().accept(this);
        between.getBetweenExpressionEnd().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Column tableColumn) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Division division) {
        visitBinaryExpression(division);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DoubleValue doubleValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(EqualsTo equalsTo) {
        visitBinaryExpression(equalsTo);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Function function) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThan greaterThan) {
        visitBinaryExpression(greaterThan);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitBinaryExpression(greaterThanEquals);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);
        inExpression.getRightItemsList().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(SignedExpression signedExpression) {
        signedExpression.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IsNullExpression isNullExpression) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcParameter jdbcParameter) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExistsExpression existsExpression) {
        existsExpression.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LongValue longValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThan minorThan) {
        visitBinaryExpression(minorThan);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThanEquals minorThanEquals) {
        visitBinaryExpression(minorThanEquals);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NotEqualsTo notEqualsTo) {
        visitBinaryExpression(notEqualsTo);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NullValue nullValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(StringValue stringValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction);
    }

    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        binaryExpression.getLeftExpression().accept(this);
        binaryExpression.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(ExpressionList expressionList) {
        for (Expression expression : expressionList.getExpressions()) {
            expression.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DateValue dateValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimestampValue timestampValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimeValue timeValue) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CaseExpression caseExpression) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(WhenClause whenClause) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AllComparisonExpression allComparisonExpression) {
        allComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(this);
        subjoin.getJoin().getRightItem().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Concat concat) {
        visitBinaryExpression(concat);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Matches matches) {
        visitBinaryExpression(matches);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CastExpression cast) {
        cast.getLeftExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnalyticExpression analytic) {
    }

    @Override // net.sf.jsqlparser.statement.select.SelectVisitor
    public void visit(SetOperationList list) {
        for (PlainSelect plainSelect : list.getPlainSelects()) {
            visit(plainSelect);
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExtractExpression eexpr) {
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.getSubSelect().getSelectBody().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList exprList : multiExprList.getExprList()) {
            exprList.accept(this);
        }
    }

    @Override // net.sf.jsqlparser.statement.select.FromItemVisitor
    public void visit(ValuesList valuesList) {
    }

    private void init() {
        this.otherItemNames = new ArrayList();
        this.tables = new ArrayList();
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IntervalExpression iexpr) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OracleHierarchicalExpression oexpr) {
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMySQLOperator rexpr) {
        visitBinaryExpression(rexpr);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JsonExpression jsonExpr) {
    }
}
