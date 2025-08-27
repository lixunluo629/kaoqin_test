package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/interceptors/ResultSetScannerInterceptor.class */
public class ResultSetScannerInterceptor implements StatementInterceptor {
    protected Pattern regexP;

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        String regexFromUser = props.getProperty("resultSetScannerRegex");
        if (regexFromUser == null || regexFromUser.length() == 0) {
            throw new SQLException("resultSetScannerRegex must be configured, and must be > 0 characters");
        }
        try {
            this.regexP = Pattern.compile(regexFromUser);
        } catch (Throwable t) {
            SQLException sqlEx = new SQLException("Can't use configured regex due to underlying exception.");
            sqlEx.initCause(t);
            throw sqlEx;
        }
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, final ResultSetInternalMethods originalResultSet, Connection connection) throws SQLException {
        return (ResultSetInternalMethods) Proxy.newProxyInstance(originalResultSet.getClass().getClassLoader(), new Class[]{ResultSetInternalMethods.class}, new InvocationHandler() { // from class: com.mysql.jdbc.interceptors.ResultSetScannerInterceptor.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("equals".equals(method.getName())) {
                    return Boolean.valueOf(args[0].equals(this));
                }
                Object invocationResult = method.invoke(originalResultSet, args);
                String methodName = method.getName();
                if ((invocationResult != null && (invocationResult instanceof String)) || "getString".equals(methodName) || "getObject".equals(methodName) || "getObjectStoredProc".equals(methodName)) {
                    Matcher matcher = ResultSetScannerInterceptor.this.regexP.matcher(invocationResult.toString());
                    if (matcher.matches()) {
                        throw new SQLException("value disallowed by filter");
                    }
                }
                return invocationResult;
            }
        });
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void destroy() {
    }
}
