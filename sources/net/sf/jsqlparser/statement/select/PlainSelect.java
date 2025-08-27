package net.sf.jsqlparser.statement.select;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.schema.Table;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/statement/select/PlainSelect.class */
public class PlainSelect implements SelectBody {
    private List<SelectItem> selectItems;
    private List<Table> intoTables;
    private FromItem fromItem;
    private List<Join> joins;
    private Expression where;
    private List<Expression> groupByColumnReferences;
    private List<OrderByElement> orderByElements;
    private Expression having;
    private Limit limit;
    private Top top;
    private Distinct distinct = null;
    private OracleHierarchicalExpression oracleHierarchical = null;
    private boolean oracleSiblings = false;

    public FromItem getFromItem() {
        return this.fromItem;
    }

    public List<Table> getIntoTables() {
        return this.intoTables;
    }

    public List<SelectItem> getSelectItems() {
        return this.selectItems;
    }

    public Expression getWhere() {
        return this.where;
    }

    public void setFromItem(FromItem item) {
        this.fromItem = item;
    }

    public void setIntoTables(List<Table> intoTables) {
        this.intoTables = intoTables;
    }

    public void setSelectItems(List<SelectItem> list) {
        this.selectItems = list;
    }

    public void addSelectItems(SelectItem... items) {
        if (this.selectItems == null) {
            this.selectItems = new ArrayList();
        }
        Collections.addAll(this.selectItems, items);
    }

    public void setWhere(Expression where) {
        this.where = where;
    }

    public List<Join> getJoins() {
        return this.joins;
    }

    public void setJoins(List<Join> list) {
        this.joins = list;
    }

    @Override // net.sf.jsqlparser.statement.select.SelectBody
    public void accept(SelectVisitor selectVisitor) {
        selectVisitor.visit(this);
    }

    public List<OrderByElement> getOrderByElements() {
        return this.orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public Limit getLimit() {
        return this.limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Top getTop() {
        return this.top;
    }

    public void setTop(Top top) {
        this.top = top;
    }

    public Distinct getDistinct() {
        return this.distinct;
    }

    public void setDistinct(Distinct distinct) {
        this.distinct = distinct;
    }

    public Expression getHaving() {
        return this.having;
    }

    public void setHaving(Expression expression) {
        this.having = expression;
    }

    public List<Expression> getGroupByColumnReferences() {
        return this.groupByColumnReferences;
    }

    public void setGroupByColumnReferences(List<Expression> list) {
        this.groupByColumnReferences = list;
    }

    public OracleHierarchicalExpression getOracleHierarchical() {
        return this.oracleHierarchical;
    }

    public void setOracleHierarchical(OracleHierarchicalExpression oracleHierarchical) {
        this.oracleHierarchical = oracleHierarchical;
    }

    public boolean isOracleSiblings() {
        return this.oracleSiblings;
    }

    public void setOracleSiblings(boolean oracleSiblings) {
        this.oracleSiblings = oracleSiblings;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.distinct != null) {
            sql.append(this.distinct).append(SymbolConstants.SPACE_SYMBOL);
        }
        if (this.top != null) {
            sql.append(this.top).append(SymbolConstants.SPACE_SYMBOL);
        }
        sql.append(getStringList(this.selectItems));
        if (this.intoTables != null) {
            sql.append(" INTO ");
            Iterator<Table> iter = this.intoTables.iterator();
            while (iter.hasNext()) {
                sql.append(iter.next().toString());
                if (iter.hasNext()) {
                    sql.append(", ");
                }
            }
        }
        if (this.fromItem != null) {
            sql.append(" FROM ").append(this.fromItem);
            if (this.joins != null) {
                for (Join join : this.joins) {
                    if (join.isSimple()) {
                        sql.append(", ").append(join);
                    } else {
                        sql.append(SymbolConstants.SPACE_SYMBOL).append(join);
                    }
                }
            }
            if (this.where != null) {
                sql.append(" WHERE ").append(this.where);
            }
            if (this.oracleHierarchical != null) {
                sql.append(this.oracleHierarchical.toString());
            }
            sql.append(getFormatedList(this.groupByColumnReferences, "GROUP BY"));
            if (this.having != null) {
                sql.append(" HAVING ").append(this.having);
            }
            sql.append(orderByToString(this.oracleSiblings, this.orderByElements));
            if (this.limit != null) {
                sql.append(this.limit);
            }
        }
        return sql.toString();
    }

    public static String orderByToString(List<OrderByElement> orderByElements) {
        return orderByToString(false, orderByElements);
    }

    public static String orderByToString(boolean oracleSiblings, List<OrderByElement> orderByElements) {
        return getFormatedList(orderByElements, oracleSiblings ? "ORDER SIBLINGS BY" : "ORDER BY");
    }

    public static String getFormatedList(List<?> list, String expression) {
        return getFormatedList(list, expression, true, false);
    }

    public static String getFormatedList(List<?> list, String expression, boolean useComma, boolean useBrackets) {
        String sql = getStringList(list, useComma, useBrackets);
        if (sql.length() > 0) {
            if (expression.length() > 0) {
                sql = SymbolConstants.SPACE_SYMBOL + expression + SymbolConstants.SPACE_SYMBOL + sql;
            } else {
                sql = SymbolConstants.SPACE_SYMBOL + sql;
            }
        }
        return sql;
    }

    public static String getStringList(List<?> list) {
        return getStringList(list, true, false);
    }

    public static String getStringList(List<?> list, boolean useComma, boolean useBrackets) {
        String ans = "";
        String comma = ",";
        if (!useComma) {
            comma = "";
        }
        if (list != null) {
            if (useBrackets) {
                ans = ans + "(";
            }
            int i = 0;
            while (i < list.size()) {
                ans = ans + "" + list.get(i) + (i < list.size() - 1 ? comma + SymbolConstants.SPACE_SYMBOL : "");
                i++;
            }
            if (useBrackets) {
                ans = ans + ")";
            }
        }
        return ans;
    }
}
