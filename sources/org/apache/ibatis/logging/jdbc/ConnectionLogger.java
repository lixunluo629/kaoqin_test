package org.apache.ibatis.logging.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ExceptionUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/jdbc/ConnectionLogger.class */
public final class ConnectionLogger extends BaseJdbcLogger implements InvocationHandler {
    private final Connection connection;

    private ConnectionLogger(Connection conn, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.connection = conn;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }
            if ("prepareStatement".equals(method.getName())) {
                if (isDebugEnabled()) {
                    debug(" Preparing: " + removeBreakingWhitespace((String) params[0]), true);
                }
                PreparedStatement stmt = (PreparedStatement) method.invoke(this.connection, params);
                return PreparedStatementLogger.newInstance(stmt, this.statementLog, this.queryStack);
            }
            if ("prepareCall".equals(method.getName())) {
                if (isDebugEnabled()) {
                    debug(" Preparing: " + removeBreakingWhitespace((String) params[0]), true);
                }
                PreparedStatement stmt2 = (PreparedStatement) method.invoke(this.connection, params);
                return PreparedStatementLogger.newInstance(stmt2, this.statementLog, this.queryStack);
            }
            if ("createStatement".equals(method.getName())) {
                Statement stmt3 = (Statement) method.invoke(this.connection, params);
                return StatementLogger.newInstance(stmt3, this.statementLog, this.queryStack);
            }
            return method.invoke(this.connection, params);
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    public static Connection newInstance(Connection conn, Log statementLog, int queryStack) {
        InvocationHandler handler = new ConnectionLogger(conn, statementLog, queryStack);
        ClassLoader cl = Connection.class.getClassLoader();
        return (Connection) Proxy.newProxyInstance(cl, new Class[]{Connection.class}, handler);
    }

    public Connection getConnection() {
        return this.connection;
    }
}
