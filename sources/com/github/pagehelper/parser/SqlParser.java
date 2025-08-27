package com.github.pagehelper.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/SqlParser.class */
public class SqlParser {
    private static final List<SelectItem> COUNT_ITEM = new ArrayList();
    private static final Alias TABLE_ALIAS;
    private Map<String, String> CACHE = new ConcurrentHashMap();

    static {
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(*)")));
        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    public void isSupportedSql(String sql) {
        if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new RuntimeException("分页插件不支持包含for update的sql");
        }
    }

    public String getSmartCountSql(String sql) {
        isSupportedSql(sql);
        if (this.CACHE.get(sql) != null) {
            return this.CACHE.get(sql);
        }
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            Select select = (Select) stmt;
            SelectBody selectBody = select.getSelectBody();
            processSelectBody(selectBody);
            processWithItemsList(select.getWithItemsList());
            sqlToCount(select);
            String result = select.toString();
            this.CACHE.put(sql, result);
            return result;
        } catch (Throwable th) {
            String countSql = getSimpleCountSql(sql);
            this.CACHE.put(sql, countSql);
            return countSql;
        }
    }

    public String getSimpleCountSql(String sql) {
        isSupportedSql(sql);
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(*) from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    public void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if ((selectBody instanceof PlainSelect) && isSimpleCount((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
            return;
        }
        PlainSelect plainSelect = new PlainSelect();
        SubSelect subSelect = new SubSelect();
        subSelect.setSelectBody(selectBody);
        subSelect.setAlias(TABLE_ALIAS);
        plainSelect.setFromItem(subSelect);
        plainSelect.setSelectItems(COUNT_ITEM);
        select.setSelectBody(plainSelect);
    }

    public boolean isSimpleCount(PlainSelect select) {
        if (select.getGroupByColumnReferences() != null || select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            if (item.toString().contains("?")) {
                return false;
            }
            if ((item instanceof SelectExpressionItem) && (((SelectExpressionItem) item).getExpression() instanceof Function)) {
                return false;
            }
        }
        return true;
    }

    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
            return;
        }
        if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
                return;
            }
            return;
        }
        SetOperationList operationList = (SetOperationList) selectBody;
        if (operationList.getPlainSelects() != null && operationList.getPlainSelects().size() > 0) {
            List<PlainSelect> plainSelects = operationList.getPlainSelects();
            for (PlainSelect plainSelect : plainSelects) {
                processPlainSelect(plainSelect);
            }
        }
        if (!orderByHashParameters(operationList.getOrderByElements())) {
            operationList.setOrderByElements(null);
        }
    }

    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null && subJoin.getJoin().getRightItem() != null) {
                processFromItem(subJoin.getJoin().getRightItem());
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
                return;
            }
            return;
        }
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
                return;
            }
            return;
        }
        if (!(fromItem instanceof ValuesList) && (fromItem instanceof LateralSubSelect)) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect2 = lateralSubSelect.getSubSelect();
                if (subSelect2.getSelectBody() != null) {
                    processSelectBody(subSelect2.getSelectBody());
                }
            }
        }
    }

    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }
}
