package org.apache.ibatis.logging.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ExceptionUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/jdbc/StatementLogger.class */
public final class StatementLogger extends BaseJdbcLogger implements InvocationHandler {
    private final Statement statement;

    private StatementLogger(Statement stmt, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.statement = stmt;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }
            if (EXECUTE_METHODS.contains(method.getName())) {
                if (isDebugEnabled()) {
                    debug(" Executing: " + removeBreakingWhitespace((String) params[0]), true);
                }
                if ("executeQuery".equals(method.getName())) {
                    ResultSet rs = (ResultSet) method.invoke(this.statement, params);
                    if (rs == null) {
                        return null;
                    }
                    return ResultSetLogger.newInstance(rs, this.statementLog, this.queryStack);
                }
                return method.invoke(this.statement, params);
            }
            if ("getResultSet".equals(method.getName())) {
                ResultSet rs2 = (ResultSet) method.invoke(this.statement, params);
                if (rs2 == null) {
                    return null;
                }
                return ResultSetLogger.newInstance(rs2, this.statementLog, this.queryStack);
            }
            return method.invoke(this.statement, params);
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    public static Statement newInstance(Statement stmt, Log statementLog, int queryStack) {
        InvocationHandler handler = new StatementLogger(stmt, statementLog, queryStack);
        ClassLoader cl = Statement.class.getClassLoader();
        return (Statement) Proxy.newProxyInstance(cl, new Class[]{Statement.class}, handler);
    }

    public Statement getStatement() {
        return this.statement;
    }
}
