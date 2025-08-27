package org.apache.ibatis.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/AbstractSQL.class */
public abstract class AbstractSQL<T> {
    private static final String AND = ") \nAND (";
    private static final String OR = ") \nOR (";
    private final SQLStatement sql = new SQLStatement();

    public abstract T getSelf();

    public T UPDATE(String table) {
        sql().statementType = SQLStatement.StatementType.UPDATE;
        sql().tables.add(table);
        return getSelf();
    }

    public T SET(String sets) {
        sql().sets.add(sets);
        return getSelf();
    }

    public T SET(String... sets) {
        sql().sets.addAll(Arrays.asList(sets));
        return getSelf();
    }

    public T INSERT_INTO(String tableName) {
        sql().statementType = SQLStatement.StatementType.INSERT;
        sql().tables.add(tableName);
        return getSelf();
    }

    public T VALUES(String columns, String values) {
        sql().columns.add(columns);
        sql().values.add(values);
        return getSelf();
    }

    public T INTO_COLUMNS(String... columns) {
        sql().columns.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T INTO_VALUES(String... values) {
        sql().values.addAll(Arrays.asList(values));
        return getSelf();
    }

    public T SELECT(String columns) {
        sql().statementType = SQLStatement.StatementType.SELECT;
        sql().select.add(columns);
        return getSelf();
    }

    public T SELECT(String... columns) {
        sql().statementType = SQLStatement.StatementType.SELECT;
        sql().select.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T SELECT_DISTINCT(String columns) {
        sql().distinct = true;
        SELECT(columns);
        return getSelf();
    }

    public T SELECT_DISTINCT(String... columns) {
        sql().distinct = true;
        SELECT(columns);
        return getSelf();
    }

    public T DELETE_FROM(String table) {
        sql().statementType = SQLStatement.StatementType.DELETE;
        sql().tables.add(table);
        return getSelf();
    }

    public T FROM(String table) {
        sql().tables.add(table);
        return getSelf();
    }

    public T FROM(String... tables) {
        sql().tables.addAll(Arrays.asList(tables));
        return getSelf();
    }

    public T JOIN(String join) {
        sql().join.add(join);
        return getSelf();
    }

    public T JOIN(String... joins) {
        sql().join.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T INNER_JOIN(String join) {
        sql().innerJoin.add(join);
        return getSelf();
    }

    public T INNER_JOIN(String... joins) {
        sql().innerJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T LEFT_OUTER_JOIN(String join) {
        sql().leftOuterJoin.add(join);
        return getSelf();
    }

    public T LEFT_OUTER_JOIN(String... joins) {
        sql().leftOuterJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T RIGHT_OUTER_JOIN(String join) {
        sql().rightOuterJoin.add(join);
        return getSelf();
    }

    public T RIGHT_OUTER_JOIN(String... joins) {
        sql().rightOuterJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T OUTER_JOIN(String join) {
        sql().outerJoin.add(join);
        return getSelf();
    }

    public T OUTER_JOIN(String... joins) {
        sql().outerJoin.addAll(Arrays.asList(joins));
        return getSelf();
    }

    public T WHERE(String conditions) {
        sql().where.add(conditions);
        sql().lastList = sql().where;
        return getSelf();
    }

    public T WHERE(String... conditions) {
        sql().where.addAll(Arrays.asList(conditions));
        sql().lastList = sql().where;
        return getSelf();
    }

    public T OR() {
        sql().lastList.add(OR);
        return getSelf();
    }

    public T AND() {
        sql().lastList.add(AND);
        return getSelf();
    }

    public T GROUP_BY(String columns) {
        sql().groupBy.add(columns);
        return getSelf();
    }

    public T GROUP_BY(String... columns) {
        sql().groupBy.addAll(Arrays.asList(columns));
        return getSelf();
    }

    public T HAVING(String conditions) {
        sql().having.add(conditions);
        sql().lastList = sql().having;
        return getSelf();
    }

    public T HAVING(String... conditions) {
        sql().having.addAll(Arrays.asList(conditions));
        sql().lastList = sql().having;
        return getSelf();
    }

    public T ORDER_BY(String columns) {
        sql().orderBy.add(columns);
        return getSelf();
    }

    public T ORDER_BY(String... columns) {
        sql().orderBy.addAll(Arrays.asList(columns));
        return getSelf();
    }

    private SQLStatement sql() {
        return this.sql;
    }

    public <A extends Appendable> A usingAppender(A a) throws IOException {
        sql().sql(a);
        return a;
    }

    public String toString() throws IOException {
        StringBuilder sb = new StringBuilder();
        sql().sql(sb);
        return sb.toString();
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/AbstractSQL$SafeAppendable.class */
    private static class SafeAppendable {
        private final Appendable a;
        private boolean empty = true;

        public SafeAppendable(Appendable a) {
            this.a = a;
        }

        public SafeAppendable append(CharSequence s) throws IOException {
            try {
                if (this.empty && s.length() > 0) {
                    this.empty = false;
                }
                this.a.append(s);
                return this;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean isEmpty() {
            return this.empty;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/AbstractSQL$SQLStatement.class */
    private static class SQLStatement {
        StatementType statementType;
        List<String> sets = new ArrayList();
        List<String> select = new ArrayList();
        List<String> tables = new ArrayList();
        List<String> join = new ArrayList();
        List<String> innerJoin = new ArrayList();
        List<String> outerJoin = new ArrayList();
        List<String> leftOuterJoin = new ArrayList();
        List<String> rightOuterJoin = new ArrayList();
        List<String> where = new ArrayList();
        List<String> having = new ArrayList();
        List<String> groupBy = new ArrayList();
        List<String> orderBy = new ArrayList();
        List<String> lastList = new ArrayList();
        List<String> columns = new ArrayList();
        List<String> values = new ArrayList();
        boolean distinct;

        /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/AbstractSQL$SQLStatement$StatementType.class */
        public enum StatementType {
            DELETE,
            INSERT,
            SELECT,
            UPDATE
        }

        private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close, String conjunction) throws IOException {
            if (!parts.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                builder.append(keyword);
                builder.append(SymbolConstants.SPACE_SYMBOL);
                builder.append(open);
                String last = "________";
                int n = parts.size();
                for (int i = 0; i < n; i++) {
                    String part = parts.get(i);
                    if (i > 0 && !part.equals(AbstractSQL.AND) && !part.equals(AbstractSQL.OR) && !last.equals(AbstractSQL.AND) && !last.equals(AbstractSQL.OR)) {
                        builder.append(conjunction);
                    }
                    builder.append(part);
                    last = part;
                }
                builder.append(close);
            }
        }

        private String selectSQL(SafeAppendable builder) throws IOException {
            if (this.distinct) {
                sqlClause(builder, "SELECT DISTINCT", this.select, "", "", ", ");
            } else {
                sqlClause(builder, "SELECT", this.select, "", "", ", ");
            }
            sqlClause(builder, "FROM", this.tables, "", "", ", ");
            joins(builder);
            sqlClause(builder, "WHERE", this.where, "(", ")", " AND ");
            sqlClause(builder, "GROUP BY", this.groupBy, "", "", ", ");
            sqlClause(builder, "HAVING", this.having, "(", ")", " AND ");
            sqlClause(builder, "ORDER BY", this.orderBy, "", "", ", ");
            return builder.toString();
        }

        private void joins(SafeAppendable builder) throws IOException {
            sqlClause(builder, "JOIN", this.join, "", "", "\nJOIN ");
            sqlClause(builder, "INNER JOIN", this.innerJoin, "", "", "\nINNER JOIN ");
            sqlClause(builder, "OUTER JOIN", this.outerJoin, "", "", "\nOUTER JOIN ");
            sqlClause(builder, "LEFT OUTER JOIN", this.leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
            sqlClause(builder, "RIGHT OUTER JOIN", this.rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
        }

        private String insertSQL(SafeAppendable builder) throws IOException {
            sqlClause(builder, "INSERT INTO", this.tables, "", "", "");
            sqlClause(builder, "", this.columns, "(", ")", ", ");
            sqlClause(builder, "VALUES", this.values, "(", ")", ", ");
            return builder.toString();
        }

        private String deleteSQL(SafeAppendable builder) throws IOException {
            sqlClause(builder, "DELETE FROM", this.tables, "", "", "");
            sqlClause(builder, "WHERE", this.where, "(", ")", " AND ");
            return builder.toString();
        }

        private String updateSQL(SafeAppendable builder) throws IOException {
            sqlClause(builder, "UPDATE", this.tables, "", "", "");
            joins(builder);
            sqlClause(builder, "SET", this.sets, "", "", ", ");
            sqlClause(builder, "WHERE", this.where, "(", ")", " AND ");
            return builder.toString();
        }

        public String sql(Appendable a) throws IOException {
            String answer;
            SafeAppendable builder = new SafeAppendable(a);
            if (this.statementType == null) {
                return null;
            }
            switch (this.statementType) {
                case DELETE:
                    answer = deleteSQL(builder);
                    break;
                case INSERT:
                    answer = insertSQL(builder);
                    break;
                case SELECT:
                    answer = selectSQL(builder);
                    break;
                case UPDATE:
                    answer = updateSQL(builder);
                    break;
                default:
                    answer = null;
                    break;
            }
            return answer;
        }
    }
}
