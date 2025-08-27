package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/WrapperBase.class */
abstract class WrapperBase {
    protected MysqlPooledConnection pooledConnection;
    protected Map<Class<?>, Object> unwrappedInterfaces = null;
    protected ExceptionInterceptor exceptionInterceptor;

    protected void checkAndFireConnectionError(SQLException sqlEx) throws SQLException {
        if (this.pooledConnection != null && SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE.equals(sqlEx.getSQLState())) {
            this.pooledConnection.callConnectionEventListeners(1, sqlEx);
        }
        throw sqlEx;
    }

    protected WrapperBase(MysqlPooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
        this.exceptionInterceptor = this.pooledConnection.getExceptionInterceptor();
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/WrapperBase$ConnectionErrorFiringInvocationHandler.class */
    protected class ConnectionErrorFiringInvocationHandler implements InvocationHandler {
        Object invokeOn;

        public ConnectionErrorFiringInvocationHandler(Object toInvokeOn) {
            this.invokeOn = null;
            this.invokeOn = toInvokeOn;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("equals".equals(method.getName())) {
                return Boolean.valueOf(args[0].equals(this));
            }
            Object result = null;
            try {
                result = method.invoke(this.invokeOn, args);
                if (result != null) {
                    result = proxyIfInterfaceIsJdbc(result, result.getClass());
                }
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof SQLException) {
                    WrapperBase.this.checkAndFireConnectionError((SQLException) e.getTargetException());
                } else {
                    throw e;
                }
            }
            return result;
        }

        private Object proxyIfInterfaceIsJdbc(Object toProxy, Class<?> clazz) {
            Class<?>[] interfaces = clazz.getInterfaces();
            int len$ = interfaces.length;
            if (0 < len$) {
                Class<?> iclass = interfaces[0];
                String packageName = Util.getPackageName(iclass);
                if ("java.sql".equals(packageName) || "javax.sql".equals(packageName)) {
                    return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfaces, WrapperBase.this.new ConnectionErrorFiringInvocationHandler(toProxy));
                }
                return proxyIfInterfaceIsJdbc(toProxy, iclass);
            }
            return toProxy;
        }
    }
}
