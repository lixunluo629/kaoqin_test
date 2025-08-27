package com.github.pagehelper.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
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
import net.sf.jsqlparser.statement.select.Top;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/parser/SqlServer.class */
public class SqlServer {
    private static final String WRAP_TABLE = "WRAP_OUTER_TABLE";
    private static final Map<String, String> CACHE = new ConcurrentHashMap();
    private static final String START_ROW = String.valueOf(Long.MIN_VALUE);
    private static final String PAGE_SIZE = String.valueOf(Long.MAX_VALUE);
    private static final String PAGE_TABLE_NAME = "PAGE_TABLE_ALIAS";
    public static final Alias PAGE_TABLE_ALIAS = new Alias(PAGE_TABLE_NAME);
    private static final String PAGE_ROW_NUMBER = "PAGE_ROW_NUMBER";
    private static final Column PAGE_ROW_NUMBER_COLUMN = new Column(PAGE_ROW_NUMBER);
    private static final Top TOP100_PERCENT = new Top();

    static {
        TOP100_PERCENT.setRowCount(100L);
        TOP100_PERCENT.setPercentage(true);
    }

    public String convertToPageSql(String sql, int offset, int limit) {
        String pageSql = CACHE.get(sql);
        if (pageSql == null) {
            try {
                Statement stmt = CCJSqlParserUtil.parse(sql);
                if (!(stmt instanceof Select)) {
                    throw new RuntimeException("分页语句必须是Select查询!");
                }
                Select pageSelect = getPageSelect((Select) stmt);
                pageSql = pageSelect.toString();
                CACHE.put(sql, pageSql);
            } catch (Throwable th) {
                throw new RuntimeException("不支持该SQL转换为分页查询!");
            }
        }
        return pageSql.replace(START_ROW, String.valueOf(offset)).replace(PAGE_SIZE, String.valueOf(limit));
    }

    private Select getPageSelect(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof SetOperationList) {
            selectBody = wrapSetOperationList((SetOperationList) selectBody);
        }
        if (((PlainSelect) selectBody).getTop() != null) {
            throw new RuntimeException("被分页的语句已经包含了Top，不能再通过分页插件进行分页查询!");
        }
        List<SelectItem> selectItems = getSelectItems((PlainSelect) selectBody);
        addRowNumber((PlainSelect) selectBody);
        processSelectBody(selectBody, 0);
        Select select2 = new Select();
        PlainSelect newSelectBody = new PlainSelect();
        Top top = new Top();
        top.setRowCount(Long.MAX_VALUE);
        newSelectBody.setTop(top);
        List<OrderByElement> orderByElements = new ArrayList<>();
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(PAGE_ROW_NUMBER_COLUMN);
        orderByElements.add(orderByElement);
        newSelectBody.setOrderByElements(orderByElements);
        GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(PAGE_ROW_NUMBER_COLUMN);
        greaterThan.setRightExpression(new LongValue(Long.MIN_VALUE));
        newSelectBody.setWhere(greaterThan);
        newSelectBody.setSelectItems(selectItems);
        SubSelect fromItem = new SubSelect();
        fromItem.setSelectBody(selectBody);
        fromItem.setAlias(PAGE_TABLE_ALIAS);
        newSelectBody.setFromItem(fromItem);
        select2.setSelectBody(newSelectBody);
        if (isNotEmptyList(select.getWithItemsList())) {
            select2.setWithItemsList(select.getWithItemsList());
        }
        return select2;
    }

    private SelectBody wrapSetOperationList(SetOperationList setOperationList) {
        PlainSelect plainSelect = setOperationList.getPlainSelects().get(setOperationList.getPlainSelects().size() - 1);
        PlainSelect selectBody = new PlainSelect();
        List<SelectItem> selectItems = getSelectItems(plainSelect);
        selectBody.setSelectItems(selectItems);
        SubSelect fromItem = new SubSelect();
        fromItem.setSelectBody(setOperationList);
        fromItem.setAlias(new Alias(WRAP_TABLE));
        selectBody.setFromItem(fromItem);
        if (isNotEmptyList(plainSelect.getOrderByElements())) {
            selectBody.setOrderByElements(plainSelect.getOrderByElements());
            plainSelect.setOrderByElements(null);
        }
        return selectBody;
    }

    private List<SelectItem> getSelectItems(PlainSelect plainSelect) {
        List<SelectItem> selectItems = new ArrayList<>();
        for (SelectItem selectItem : plainSelect.getSelectItems()) {
            if (selectItem instanceof SelectExpressionItem) {
                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                if (selectExpressionItem.getAlias() != null) {
                    SelectExpressionItem expressionItem = new SelectExpressionItem(new Column(selectExpressionItem.getAlias().getName()));
                    selectItems.add(expressionItem);
                } else if (selectExpressionItem.getExpression() instanceof Column) {
                    Column column = (Column) selectExpressionItem.getExpression();
                    if (column.getTable() != null) {
                        Column newColumn = new Column(column.getColumnName());
                        SelectExpressionItem item = new SelectExpressionItem(newColumn);
                        selectItems.add(item);
                    } else {
                        selectItems.add(selectItem);
                    }
                } else {
                    selectItems.add(selectItem);
                }
            } else if (selectItem instanceof AllTableColumns) {
                selectItems.add(new AllColumns());
            } else {
                selectItems.add(selectItem);
            }
        }
        return selectItems;
    }

    private void addRowNumber(PlainSelect plainSelect) {
        StringBuilder orderByBuilder = new StringBuilder();
        orderByBuilder.append("ROW_NUMBER() OVER (");
        if (isNotEmptyList(plainSelect.getOrderByElements())) {
            orderByBuilder.append(PlainSelect.orderByToString(false, plainSelect.getOrderByElements()));
            if (isNotEmptyList(plainSelect.getOrderByElements())) {
                plainSelect.setOrderByElements(null);
            }
            orderByBuilder.append(") ");
            orderByBuilder.append(PAGE_ROW_NUMBER);
            Column orderByColumn = new Column(orderByBuilder.toString());
            plainSelect.getSelectItems().add(0, new SelectExpressionItem(orderByColumn));
            return;
        }
        throw new RuntimeException("请您在sql中包含order by语句!");
    }

    private void processSelectBody(SelectBody selectBody, int level) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody, level + 1);
            return;
        }
        if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody(), level + 1);
                return;
            }
            return;
        }
        SetOperationList operationList = (SetOperationList) selectBody;
        if (operationList.getPlainSelects() != null && operationList.getPlainSelects().size() > 0) {
            List<PlainSelect> plainSelects = operationList.getPlainSelects();
            for (PlainSelect plainSelect : plainSelects) {
                processPlainSelect(plainSelect, level + 1);
            }
        }
    }

    private void processPlainSelect(PlainSelect plainSelect, int level) {
        if (level > 1 && isNotEmptyList(plainSelect.getOrderByElements()) && plainSelect.getTop() == null) {
            plainSelect.setTop(TOP100_PERCENT);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem(), level + 1);
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem(), level + 1);
                }
            }
        }
    }

    private void processFromItem(FromItem fromItem, int level) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null && subJoin.getJoin().getRightItem() != null) {
                processFromItem(subJoin.getJoin().getRightItem(), level + 1);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft(), level + 1);
                return;
            }
            return;
        }
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody(), level + 1);
                return;
            }
            return;
        }
        if (!(fromItem instanceof ValuesList) && (fromItem instanceof LateralSubSelect)) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect2 = lateralSubSelect.getSubSelect();
                if (subSelect2.getSelectBody() != null) {
                    processSelectBody(subSelect2.getSelectBody(), level + 1);
                }
            }
        }
    }

    private boolean isNotEmptyList(List<?> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }

    private boolean isNotEmptyString(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return true;
    }
}
