package org.apache.ibatis.datasource.pooled;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/pooled/PooledDataSource.class */
public class PooledDataSource implements DataSource {
    private static final Log log = LogFactory.getLog((Class<?>) PooledDataSource.class);
    private final PoolState state;
    private final UnpooledDataSource dataSource;
    protected int poolMaximumActiveConnections;
    protected int poolMaximumIdleConnections;
    protected int poolMaximumCheckoutTime;
    protected int poolTimeToWait;
    protected int poolMaximumLocalBadConnectionTolerance;
    protected String poolPingQuery;
    protected boolean poolPingEnabled;
    protected int poolPingConnectionsNotUsedFor;
    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = new UnpooledDataSource();
    }

    public PooledDataSource(UnpooledDataSource dataSource) {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = dataSource;
    }

    public PooledDataSource(String driver, String url, String username, String password) {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = new UnpooledDataSource(driver, url, username, password);
        this.expectedConnectionTypeCode = assembleConnectionTypeCode(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    public PooledDataSource(String driver, String url, Properties driverProperties) {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = new UnpooledDataSource(driver, url, driverProperties);
        this.expectedConnectionTypeCode = assembleConnectionTypeCode(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username, String password) {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
        this.expectedConnectionTypeCode = assembleConnectionTypeCode(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
        this.state = new PoolState(this);
        this.poolMaximumActiveConnections = 10;
        this.poolMaximumIdleConnections = 5;
        this.poolMaximumCheckoutTime = 20000;
        this.poolTimeToWait = 20000;
        this.poolMaximumLocalBadConnectionTolerance = 3;
        this.poolPingQuery = "NO PING QUERY SET";
        this.dataSource = new UnpooledDataSource(driverClassLoader, driver, url, driverProperties);
        this.expectedConnectionTypeCode = assembleConnectionTypeCode(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return popConnection(this.dataSource.getUsername(), this.dataSource.getPassword()).getProxyConnection();
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    public void setDriver(String driver) {
        this.dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        this.dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        this.dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        this.dataSource.setPassword(password);
        forceCloseAll();
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.dataSource.setAutoCommit(Boolean.valueOf(defaultAutoCommit));
        forceCloseAll();
    }

    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        this.dataSource.setDefaultTransactionIsolationLevel(defaultTransactionIsolationLevel);
        forceCloseAll();
    }

    public void setDriverProperties(Properties driverProps) {
        this.dataSource.setDriverProperties(driverProps);
        forceCloseAll();
    }

    public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
        this.poolMaximumActiveConnections = poolMaximumActiveConnections;
        forceCloseAll();
    }

    public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
        this.poolMaximumIdleConnections = poolMaximumIdleConnections;
        forceCloseAll();
    }

    public void setPoolMaximumLocalBadConnectionTolerance(int poolMaximumLocalBadConnectionTolerance) {
        this.poolMaximumLocalBadConnectionTolerance = poolMaximumLocalBadConnectionTolerance;
    }

    public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
        this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
        forceCloseAll();
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
        forceCloseAll();
    }

    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
        forceCloseAll();
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
        forceCloseAll();
    }

    public void setPoolPingConnectionsNotUsedFor(int milliseconds) {
        this.poolPingConnectionsNotUsedFor = milliseconds;
        forceCloseAll();
    }

    public String getDriver() {
        return this.dataSource.getDriver();
    }

    public String getUrl() {
        return this.dataSource.getUrl();
    }

    public String getUsername() {
        return this.dataSource.getUsername();
    }

    public String getPassword() {
        return this.dataSource.getPassword();
    }

    public boolean isAutoCommit() {
        return this.dataSource.isAutoCommit().booleanValue();
    }

    public Integer getDefaultTransactionIsolationLevel() {
        return this.dataSource.getDefaultTransactionIsolationLevel();
    }

    public Properties getDriverProperties() {
        return this.dataSource.getDriverProperties();
    }

    public int getPoolMaximumActiveConnections() {
        return this.poolMaximumActiveConnections;
    }

    public int getPoolMaximumIdleConnections() {
        return this.poolMaximumIdleConnections;
    }

    public int getPoolMaximumLocalBadConnectionTolerance() {
        return this.poolMaximumLocalBadConnectionTolerance;
    }

    public int getPoolMaximumCheckoutTime() {
        return this.poolMaximumCheckoutTime;
    }

    public int getPoolTimeToWait() {
        return this.poolTimeToWait;
    }

    public String getPoolPingQuery() {
        return this.poolPingQuery;
    }

    public boolean isPoolPingEnabled() {
        return this.poolPingEnabled;
    }

    public int getPoolPingConnectionsNotUsedFor() {
        return this.poolPingConnectionsNotUsedFor;
    }

    public void forceCloseAll() {
        synchronized (this.state) {
            this.expectedConnectionTypeCode = assembleConnectionTypeCode(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
            for (int i = this.state.activeConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = this.state.activeConnections.remove(i - 1);
                    conn.invalidate();
                    Connection realConn = conn.getRealConnection();
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    realConn.close();
                } catch (Exception e) {
                }
            }
            for (int i2 = this.state.idleConnections.size(); i2 > 0; i2--) {
                try {
                    PooledConnection conn2 = this.state.idleConnections.remove(i2 - 1);
                    conn2.invalidate();
                    Connection realConn2 = conn2.getRealConnection();
                    if (!realConn2.getAutoCommit()) {
                        realConn2.rollback();
                    }
                    realConn2.close();
                } catch (Exception e2) {
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("PooledDataSource forcefully closed/removed all connections.");
        }
    }

    public PoolState getPoolState() {
        return this.state;
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    protected void pushConnection(PooledConnection conn) throws SQLException {
        synchronized (this.state) {
            this.state.activeConnections.remove(conn);
            if (conn.isValid()) {
                if (this.state.idleConnections.size() < this.poolMaximumIdleConnections && conn.getConnectionTypeCode() == this.expectedConnectionTypeCode) {
                    this.state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    if (!conn.getRealConnection().getAutoCommit()) {
                        conn.getRealConnection().rollback();
                    }
                    PooledConnection newConn = new PooledConnection(conn.getRealConnection(), this);
                    this.state.idleConnections.add(newConn);
                    newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
                    newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
                    conn.invalidate();
                    if (log.isDebugEnabled()) {
                        log.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
                    }
                    this.state.notifyAll();
                } else {
                    this.state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    if (!conn.getRealConnection().getAutoCommit()) {
                        conn.getRealConnection().rollback();
                    }
                    conn.getRealConnection().close();
                    if (log.isDebugEnabled()) {
                        log.debug("Closed connection " + conn.getRealHashCode() + ".");
                    }
                    conn.invalidate();
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("A bad connection (" + conn.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                }
                this.state.badConnectionCount++;
            }
        }
    }

    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;
        while (conn == null) {
            synchronized (this.state) {
                if (!this.state.idleConnections.isEmpty()) {
                    conn = this.state.idleConnections.remove(0);
                    if (log.isDebugEnabled()) {
                        log.debug("Checked out connection " + conn.getRealHashCode() + " from pool.");
                    }
                } else if (this.state.activeConnections.size() < this.poolMaximumActiveConnections) {
                    conn = new PooledConnection(this.dataSource.getConnection(), this);
                    if (log.isDebugEnabled()) {
                        log.debug("Created connection " + conn.getRealHashCode() + ".");
                    }
                } else {
                    PooledConnection oldestActiveConnection = this.state.activeConnections.get(0);
                    long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                    if (longestCheckoutTime > this.poolMaximumCheckoutTime) {
                        this.state.claimedOverdueConnectionCount++;
                        this.state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                        this.state.accumulatedCheckoutTime += longestCheckoutTime;
                        this.state.activeConnections.remove(oldestActiveConnection);
                        if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                            try {
                                oldestActiveConnection.getRealConnection().rollback();
                            } catch (SQLException e) {
                                log.debug("Bad connection. Could not roll back");
                            }
                        }
                        conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                        conn.setCreatedTimestamp(oldestActiveConnection.getCreatedTimestamp());
                        conn.setLastUsedTimestamp(oldestActiveConnection.getLastUsedTimestamp());
                        oldestActiveConnection.invalidate();
                        if (log.isDebugEnabled()) {
                            log.debug("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        }
                    } else {
                        if (!countedWait) {
                            try {
                                this.state.hadToWaitCount++;
                                countedWait = true;
                            } catch (InterruptedException e2) {
                            }
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("Waiting as long as " + this.poolTimeToWait + " milliseconds for connection.");
                        }
                        long wt = System.currentTimeMillis();
                        this.state.wait(this.poolTimeToWait);
                        this.state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                    }
                }
                if (conn != null) {
                    if (conn.isValid()) {
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(this.dataSource.getUrl(), username, password));
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        this.state.activeConnections.add(conn);
                        this.state.requestCount++;
                        this.state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection.");
                        }
                        this.state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > this.poolMaximumIdleConnections + this.poolMaximumLocalBadConnectionTolerance) {
                            if (log.isDebugEnabled()) {
                                log.debug("PooledDataSource: Could not get a good connection to the database.");
                            }
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }
        if (conn == null) {
            if (log.isDebugEnabled()) {
                log.debug("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
            }
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
        }
        return conn;
    }

    protected boolean pingConnection(PooledConnection conn) throws SQLException {
        boolean result;
        try {
            result = !conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
            }
            result = false;
        }
        if (result && this.poolPingEnabled && this.poolPingConnectionsNotUsedFor >= 0 && conn.getTimeElapsedSinceLastUse() > this.poolPingConnectionsNotUsedFor) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Testing connection " + conn.getRealHashCode() + " ...");
                }
                Connection realConn = conn.getRealConnection();
                Statement statement = realConn.createStatement();
                ResultSet rs = statement.executeQuery(this.poolPingQuery);
                rs.close();
                statement.close();
                if (!realConn.getAutoCommit()) {
                    realConn.rollback();
                }
                result = true;
                if (log.isDebugEnabled()) {
                    log.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
                }
            } catch (Exception e2) {
                log.warn("Execution of ping query '" + this.poolPingQuery + "' failed: " + e2.getMessage());
                try {
                    conn.getRealConnection().close();
                } catch (Exception e3) {
                }
                result = false;
                if (log.isDebugEnabled()) {
                    log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e2.getMessage());
                }
            }
        }
        return result;
    }

    public static Connection unwrapConnection(Connection conn) throws IllegalArgumentException {
        if (Proxy.isProxyClass(conn.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(conn);
            if (handler instanceof PooledConnection) {
                return ((PooledConnection) handler).getRealConnection();
            }
        }
        return conn;
    }

    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override // javax.sql.CommonDataSource
    public Logger getParentLogger() {
        return Logger.getLogger("global");
    }
}
