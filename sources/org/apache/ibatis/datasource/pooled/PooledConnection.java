package org.apache.ibatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.ibatis.reflection.ExceptionUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/pooled/PooledConnection.class */
class PooledConnection implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final Class<?>[] IFACES = {Connection.class};
    private final int hashCode;
    private final PooledDataSource dataSource;
    private final Connection realConnection;
    private long checkoutTimestamp;
    private int connectionTypeCode;
    private long createdTimestamp = System.currentTimeMillis();
    private long lastUsedTimestamp = System.currentTimeMillis();
    private boolean valid = true;
    private final Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);

    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.hashCode = connection.hashCode();
        this.realConnection = connection;
        this.dataSource = dataSource;
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid && this.realConnection != null && this.dataSource.pingConnection(this);
    }

    public Connection getRealConnection() {
        return this.realConnection;
    }

    public Connection getProxyConnection() {
        return this.proxyConnection;
    }

    public int getRealHashCode() {
        if (this.realConnection == null) {
            return 0;
        }
        return this.realConnection.hashCode();
    }

    public int getConnectionTypeCode() {
        return this.connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public long getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return this.lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - this.lastUsedTimestamp;
    }

    public long getAge() {
        return System.currentTimeMillis() - this.createdTimestamp;
    }

    public long getCheckoutTimestamp() {
        return this.checkoutTimestamp;
    }

    public void setCheckoutTimestamp(long timestamp) {
        this.checkoutTimestamp = timestamp;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - this.checkoutTimestamp;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        return obj instanceof PooledConnection ? this.realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode() : (obj instanceof Connection) && this.hashCode == obj.hashCode();
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if ("close".hashCode() == methodName.hashCode() && "close".equals(methodName)) {
            this.dataSource.pushConnection(this);
            return null;
        }
        try {
            if (!Object.class.equals(method.getDeclaringClass())) {
                checkConnection();
            }
            return method.invoke(this.realConnection, args);
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    private void checkConnection() throws SQLException {
        if (!this.valid) {
            throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
        }
    }
}
