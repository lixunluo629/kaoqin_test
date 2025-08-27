package org.apache.ibatis.logging.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.springframework.hateoas.Link;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/jdbc/ResultSetLogger.class */
public final class ResultSetLogger extends BaseJdbcLogger implements InvocationHandler {
    private static Set<Integer> BLOB_TYPES = new HashSet();
    private boolean first;
    private int rows;
    private final ResultSet rs;
    private final Set<Integer> blobColumns;

    static {
        BLOB_TYPES.add(-2);
        BLOB_TYPES.add(2004);
        BLOB_TYPES.add(2005);
        BLOB_TYPES.add(-16);
        BLOB_TYPES.add(-4);
        BLOB_TYPES.add(-1);
        BLOB_TYPES.add(2011);
        BLOB_TYPES.add(-3);
    }

    private ResultSetLogger(ResultSet rs, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.first = true;
        this.blobColumns = new HashSet();
        this.rs = rs;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }
            Object o = method.invoke(this.rs, params);
            if (Link.REL_NEXT.equals(method.getName())) {
                if (((Boolean) o).booleanValue()) {
                    this.rows++;
                    if (isTraceEnabled()) {
                        ResultSetMetaData rsmd = this.rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        if (this.first) {
                            this.first = false;
                            printColumnHeaders(rsmd, columnCount);
                        }
                        printColumnValues(columnCount);
                    }
                } else {
                    debug("     Total: " + this.rows, false);
                }
            }
            clearColumnInfo();
            return o;
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    private void printColumnHeaders(ResultSetMetaData rsmd, int columnCount) throws SQLException {
        StringBuilder row = new StringBuilder();
        row.append("   Columns: ");
        for (int i = 1; i <= columnCount; i++) {
            if (BLOB_TYPES.contains(Integer.valueOf(rsmd.getColumnType(i)))) {
                this.blobColumns.add(Integer.valueOf(i));
            }
            String colname = rsmd.getColumnLabel(i);
            row.append(colname);
            if (i != columnCount) {
                row.append(", ");
            }
        }
        trace(row.toString(), false);
    }

    private void printColumnValues(int columnCount) throws SQLException {
        String colname;
        StringBuilder row = new StringBuilder();
        row.append("       Row: ");
        for (int i = 1; i <= columnCount; i++) {
            try {
                if (this.blobColumns.contains(Integer.valueOf(i))) {
                    colname = "<<BLOB>>";
                } else {
                    colname = this.rs.getString(i);
                }
            } catch (SQLException e) {
                colname = "<<Cannot Display>>";
            }
            row.append(colname);
            if (i != columnCount) {
                row.append(", ");
            }
        }
        trace(row.toString(), false);
    }

    public static ResultSet newInstance(ResultSet rs, Log statementLog, int queryStack) {
        InvocationHandler handler = new ResultSetLogger(rs, statementLog, queryStack);
        ClassLoader cl = ResultSet.class.getClassLoader();
        return (ResultSet) Proxy.newProxyInstance(cl, new Class[]{ResultSet.class}, handler);
    }

    public ResultSet getRs() {
        return this.rs;
    }
}
