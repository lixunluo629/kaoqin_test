package net.sf.jsqlparser.util.deparser;

import ch.qos.logback.core.joran.action.ActionConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
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
import net.sf.jsqlparser.expression.operators.relational.OldOracleJoinBinaryExpression;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.hyperic.sigar.NetFlags;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/util/deparser/ExpressionDeParser.class */
public class ExpressionDeParser implements ExpressionVisitor, ItemsListVisitor {
    private StringBuilder buffer;
    private SelectVisitor selectVisitor;
    private boolean useBracketsInExprList = true;

    public ExpressionDeParser() {
    }

    public ExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
        this.selectVisitor = selectVisitor;
        this.buffer = buffer;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Addition addition) {
        visitBinaryExpression(addition, " + ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression, " AND ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        if (between.isNot()) {
            this.buffer.append(" NOT");
        }
        this.buffer.append(" BETWEEN ");
        between.getBetweenExpressionStart().accept(this);
        this.buffer.append(" AND ");
        between.getBetweenExpressionEnd().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(EqualsTo equalsTo) {
        visitOldOracleJoinBinaryExpression(equalsTo, " = ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Division division) {
        visitBinaryExpression(division, " / ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DoubleValue doubleValue) {
        this.buffer.append(doubleValue.toString());
    }

    public void visitOldOracleJoinBinaryExpression(OldOracleJoinBinaryExpression expression, String operator) {
        if (expression.isNot()) {
            this.buffer.append(" NOT ");
        }
        expression.getLeftExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == 1) {
            this.buffer.append("(+)");
        }
        this.buffer.append(operator);
        expression.getRightExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == 2) {
            this.buffer.append("(+)");
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThan greaterThan) {
        visitOldOracleJoinBinaryExpression(greaterThan, " > ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(GreaterThanEquals greaterThanEquals) {
        visitOldOracleJoinBinaryExpression(greaterThanEquals, " >= ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(InExpression inExpression) {
        if (inExpression.getLeftExpression() == null) {
            inExpression.getLeftItemsList().accept(this);
        } else {
            inExpression.getLeftExpression().accept(this);
            if (inExpression.getOldOracleJoinSyntax() == 1) {
                this.buffer.append("(+)");
            }
        }
        if (inExpression.isNot()) {
            this.buffer.append(" NOT");
        }
        this.buffer.append(" IN ");
        inExpression.getRightItemsList().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(SignedExpression signedExpression) {
        this.buffer.append(signedExpression.getSign());
        signedExpression.getExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
        if (isNullExpression.isNot()) {
            this.buffer.append(" IS NOT NULL");
        } else {
            this.buffer.append(" IS NULL");
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcParameter jdbcParameter) {
        this.buffer.append("?");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression, " LIKE ");
        String escape = likeExpression.getEscape();
        if (escape != null) {
            this.buffer.append(" ESCAPE '").append(escape).append('\'');
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExistsExpression existsExpression) {
        if (existsExpression.isNot()) {
            this.buffer.append("NOT EXISTS ");
        } else {
            this.buffer.append("EXISTS ");
        }
        existsExpression.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(LongValue longValue) {
        this.buffer.append(longValue.getStringValue());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThan minorThan) {
        visitOldOracleJoinBinaryExpression(minorThan, " < ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(MinorThanEquals minorThanEquals) {
        visitOldOracleJoinBinaryExpression(minorThanEquals, " <= ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication, " * ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NotEqualsTo notEqualsTo) {
        visitOldOracleJoinBinaryExpression(notEqualsTo, SymbolConstants.SPACE_SYMBOL + notEqualsTo.getStringExpression() + SymbolConstants.SPACE_SYMBOL);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(NullValue nullValue) {
        this.buffer.append(ActionConst.NULL);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression, " OR ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Parenthesis parenthesis) {
        if (parenthesis.isNot()) {
            this.buffer.append(" NOT ");
        }
        this.buffer.append("(");
        parenthesis.getExpression().accept(this);
        this.buffer.append(")");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(StringValue stringValue) {
        this.buffer.append("'").append(stringValue.getValue()).append("'");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction, " - ");
    }

    private void visitBinaryExpression(BinaryExpression binaryExpression, String operator) {
        if (binaryExpression.isNot()) {
            this.buffer.append(" NOT ");
        }
        binaryExpression.getLeftExpression().accept(this);
        this.buffer.append(operator);
        binaryExpression.getRightExpression().accept(this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor, net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(SubSelect subSelect) {
        this.buffer.append("(");
        subSelect.getSelectBody().accept(this.selectVisitor);
        this.buffer.append(")");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Column tableColumn) {
        Table table = tableColumn.getTable();
        String tableName = null;
        if (table != null) {
            if (table.getAlias() != null) {
                tableName = table.getAlias().getName();
            } else {
                tableName = table.getFullyQualifiedName();
            }
        }
        if (tableName != null && !tableName.isEmpty()) {
            this.buffer.append(tableName).append(".");
        }
        this.buffer.append(tableColumn.getColumnName());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Function function) {
        if (function.isEscaped()) {
            this.buffer.append("{fn ");
        }
        this.buffer.append(function.getName());
        if (function.isAllColumns() && function.getParameters() == null) {
            this.buffer.append("(*)");
        } else if (function.getParameters() == null) {
            this.buffer.append("()");
        } else {
            boolean oldUseBracketsInExprList = this.useBracketsInExprList;
            if (function.isDistinct()) {
                this.useBracketsInExprList = false;
                this.buffer.append("(DISTINCT ");
            } else if (function.isAllColumns()) {
                this.useBracketsInExprList = false;
                this.buffer.append("(ALL ");
            }
            visit(function.getParameters());
            this.useBracketsInExprList = oldUseBracketsInExprList;
            if (function.isDistinct() || function.isAllColumns()) {
                this.buffer.append(")");
            }
        }
        if (function.isEscaped()) {
            this.buffer.append("}");
        }
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(ExpressionList expressionList) {
        if (this.useBracketsInExprList) {
            this.buffer.append("(");
        }
        Iterator<Expression> iter = expressionList.getExpressions().iterator();
        while (iter.hasNext()) {
            Expression expression = iter.next();
            expression.accept(this);
            if (iter.hasNext()) {
                this.buffer.append(", ");
            }
        }
        if (this.useBracketsInExprList) {
            this.buffer.append(")");
        }
    }

    public SelectVisitor getSelectVisitor() {
        return this.selectVisitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        this.selectVisitor = visitor;
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(DateValue dateValue) {
        this.buffer.append("{d '").append(dateValue.getValue().toString()).append("'}");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimestampValue timestampValue) {
        this.buffer.append("{ts '").append(timestampValue.getValue().toString()).append("'}");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(TimeValue timeValue) {
        this.buffer.append("{t '").append(timeValue.getValue().toString()).append("'}");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CaseExpression caseExpression) {
        this.buffer.append("CASE ");
        Expression switchExp = caseExpression.getSwitchExpression();
        if (switchExp != null) {
            switchExp.accept(this);
            this.buffer.append(SymbolConstants.SPACE_SYMBOL);
        }
        for (Expression exp : caseExpression.getWhenClauses()) {
            exp.accept(this);
        }
        Expression elseExp = caseExpression.getElseExpression();
        if (elseExp != null) {
            this.buffer.append("ELSE ");
            elseExp.accept(this);
            this.buffer.append(SymbolConstants.SPACE_SYMBOL);
        }
        this.buffer.append("END");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(WhenClause whenClause) {
        this.buffer.append("WHEN ");
        whenClause.getWhenExpression().accept(this);
        this.buffer.append(" THEN ");
        whenClause.getThenExpression().accept(this);
        this.buffer.append(SymbolConstants.SPACE_SYMBOL);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AllComparisonExpression allComparisonExpression) {
        this.buffer.append(" ALL ");
        allComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        this.buffer.append(" ANY ");
        anyComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Concat concat) {
        visitBinaryExpression(concat, " || ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Matches matches) {
        visitOldOracleJoinBinaryExpression(matches, " @@ ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd, " & ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr, " | ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor, " ^ ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(CastExpression cast) {
        if (cast.isUseCastKeyword()) {
            this.buffer.append("CAST(");
            this.buffer.append(cast.getLeftExpression());
            this.buffer.append(" AS ");
            this.buffer.append(cast.getType());
            this.buffer.append(")");
            return;
        }
        this.buffer.append(cast.getLeftExpression());
        this.buffer.append(NetFlags.ANY_ADDR_V6);
        this.buffer.append(cast.getType());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo, " % ");
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(AnalyticExpression aexpr) {
        this.buffer.append(aexpr.toString());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(ExtractExpression eexpr) {
        this.buffer.append(eexpr.toString());
    }

    @Override // net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor
    public void visit(MultiExpressionList multiExprList) {
        Iterator<ExpressionList> it = multiExprList.getExprList().iterator();
        while (it.hasNext()) {
            it.next().accept(this);
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(IntervalExpression iexpr) {
        this.buffer.append(iexpr.toString());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        this.buffer.append(jdbcNamedParameter.toString());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(OracleHierarchicalExpression oexpr) {
        this.buffer.append(oexpr.toString());
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr, SymbolConstants.SPACE_SYMBOL + rexpr.getStringExpression() + SymbolConstants.SPACE_SYMBOL);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(RegExpMySQLOperator rexpr) {
        visitBinaryExpression(rexpr, SymbolConstants.SPACE_SYMBOL + rexpr.getStringExpression() + SymbolConstants.SPACE_SYMBOL);
    }

    @Override // net.sf.jsqlparser.expression.ExpressionVisitor
    public void visit(JsonExpression jsonExpr) {
        this.buffer.append(jsonExpr.toString());
    }
}
