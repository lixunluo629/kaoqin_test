package org.apache.ibatis.logging.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ExceptionUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/jdbc/PreparedStatementLogger.class */
public final class PreparedStatementLogger extends BaseJdbcLogger implements InvocationHandler {
    private final PreparedStatement statement;

    private PreparedStatementLogger(PreparedStatement stmt, Log statementLog, int queryStack) {
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
                    debug("Parameters: " + getParameterValueString(), true);
                }
                clearColumnInfo();
                if ("executeQuery".equals(method.getName())) {
                    ResultSet rs = (ResultSet) method.invoke(this.statement, params);
                    if (rs == null) {
                        return null;
                    }
                    return ResultSetLogger.newInstance(rs, this.statementLog, this.queryStack);
                }
                return method.invoke(this.statement, params);
            }
            if (SET_METHODS.contains(method.getName())) {
                if ("setNull".equals(method.getName())) {
                    setColumn(params[0], null);
                } else {
                    setColumn(params[0], params[1]);
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
            if ("getUpdateCount".equals(method.getName())) {
                int updateCount = ((Integer) method.invoke(this.statement, params)).intValue();
                if (updateCount != -1) {
                    debug("   Updates: " + updateCount, false);
                }
                return Integer.valueOf(updateCount);
            }
            return method.invoke(this.statement, params);
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    public static PreparedStatement newInstance(PreparedStatement stmt, Log statementLog, int queryStack) {
        InvocationHandler handler = new PreparedStatementLogger(stmt, statementLog, queryStack);
        ClassLoader cl = PreparedStatement.class.getClassLoader();
        return (PreparedStatement) Proxy.newProxyInstance(cl, new Class[]{PreparedStatement.class, CallableStatement.class}, handler);
    }

    public PreparedStatement getPreparedStatement() {
        return this.statement;
    }
}
