package com.zaxxer.hikari.pool;

import com.mysql.jdbc.SQLError;
import com.zaxxer.hikari.util.ClockSource;
import com.zaxxer.hikari.util.FastList;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/ProxyConnection.class */
public abstract class ProxyConnection implements Connection {
    static final int DIRTY_BIT_READONLY = 1;
    static final int DIRTY_BIT_AUTOCOMMIT = 2;
    static final int DIRTY_BIT_ISOLATION = 4;
    static final int DIRTY_BIT_CATALOG = 8;
    static final int DIRTY_BIT_NETTIMEOUT = 16;
    static final int DIRTY_BIT_SCHEMA = 32;
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ProxyConnection.class);
    private static final Set<String> ERROR_STATES = new HashSet();
    private static final Set<Integer> ERROR_CODES;
    protected Connection delegate;
    private final PoolEntry poolEntry;
    private final ProxyLeakTask leakTask;
    private final FastList<Statement> openStatements;
    private int dirtyBits;
    private long lastAccess;
    private boolean isCommitStateDirty;
    private boolean isReadOnly;
    private boolean isAutoCommit;
    private int networkTimeout;
    private int transactionIsolation;
    private String dbcatalog;
    private String dbschema;

    static {
        ERROR_STATES.add(SQLError.SQL_STATE_FEATURE_NOT_SUPPORTED);
        ERROR_STATES.add("57P01");
        ERROR_STATES.add("57P02");
        ERROR_STATES.add("57P03");
        ERROR_STATES.add(SQLError.SQL_STATE_DISCONNECT_ERROR);
        ERROR_STATES.add("JZ0C0");
        ERROR_STATES.add("JZ0C1");
        ERROR_CODES = new HashSet();
        ERROR_CODES.add(500150);
        ERROR_CODES.add(2399);
    }

    protected ProxyConnection(PoolEntry poolEntry, Connection connection, FastList<Statement> openStatements, ProxyLeakTask leakTask, long now, boolean isReadOnly, boolean isAutoCommit) {
        this.poolEntry = poolEntry;
        this.delegate = connection;
        this.openStatements = openStatements;
        this.leakTask = leakTask;
        this.lastAccess = now;
        this.isReadOnly = isReadOnly;
        this.isAutoCommit = isAutoCommit;
    }

    public final String toString() {
        return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + this.delegate;
    }

    final boolean getAutoCommitState() {
        return this.isAutoCommit;
    }

    final String getCatalogState() {
        return this.dbcatalog;
    }

    final String getSchemaState() {
        return this.dbschema;
    }

    final int getTransactionIsolationState() {
        return this.transactionIsolation;
    }

    final boolean getReadOnlyState() {
        return this.isReadOnly;
    }

    final int getNetworkTimeoutState() {
        return this.networkTimeout;
    }

    final PoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final java.sql.SQLException checkException(java.sql.SQLException r8) {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zaxxer.hikari.pool.ProxyConnection.checkException(java.sql.SQLException):java.sql.SQLException");
    }

    final synchronized void untrackStatement(Statement statement) {
        this.openStatements.remove(statement);
    }

    final void markCommitStateDirty() {
        if (this.isAutoCommit) {
            this.lastAccess = ClockSource.currentTime();
        } else {
            this.isCommitStateDirty = true;
        }
    }

    void cancelLeakTask() {
        this.leakTask.cancel();
    }

    private synchronized <T extends Statement> T trackStatement(T statement) {
        this.openStatements.add(statement);
        return statement;
    }

    private synchronized void closeStatements() throws SQLException {
        int size = this.openStatements.size();
        if (size > 0) {
            for (int i = 0; i < size && this.delegate != ClosedConnection.CLOSED_CONNECTION; i++) {
                try {
                    Statement ignored = this.openStatements.get(i);
                    if (ignored != null) {
                        ignored.close();
                    }
                } catch (SQLException e) {
                    LOGGER.warn("{} - Connection {} marked as broken because of an exception closing open statements during Connection.close()", this.poolEntry.getPoolName(), this.delegate);
                    this.leakTask.cancel();
                    this.poolEntry.evict("(exception closing Statements during Connection.close())");
                    this.delegate = ClosedConnection.CLOSED_CONNECTION;
                }
            }
            this.openStatements.clear();
        }
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public final void close() throws SQLException {
        closeStatements();
        if (this.delegate != ClosedConnection.CLOSED_CONNECTION) {
            this.leakTask.cancel();
            try {
                if (this.isCommitStateDirty && !this.isAutoCommit) {
                    this.delegate.rollback();
                    this.lastAccess = ClockSource.currentTime();
                    LOGGER.debug("{} - Executed rollback on connection {} due to dirty commit state on close().", this.poolEntry.getPoolName(), this.delegate);
                }
                if (this.dirtyBits != 0) {
                    this.poolEntry.resetConnectionState(this, this.dirtyBits);
                    this.lastAccess = ClockSource.currentTime();
                }
                this.delegate.clearWarnings();
            } catch (SQLException e) {
                if (!this.poolEntry.isMarkedEvicted()) {
                    throw checkException(e);
                }
            } finally {
                this.delegate = ClosedConnection.CLOSED_CONNECTION;
                this.poolEntry.recycle(this.lastAccess);
            }
        }
    }

    @Override // java.sql.Connection
    public boolean isClosed() throws SQLException {
        return this.delegate == ClosedConnection.CLOSED_CONNECTION;
    }

    @Override // java.sql.Connection
    public Statement createStatement() throws SQLException {
        return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement()));
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int concurrency) throws SQLException {
        return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency)));
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int concurrency, int holdability) throws SQLException {
        return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(resultSetType, concurrency, holdability)));
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, (CallableStatement) trackStatement(this.delegate.prepareCall(sql)));
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int concurrency) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, (CallableStatement) trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency)));
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, (CallableStatement) trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency, holdability)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql, autoGeneratedKeys)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int concurrency, int holdability) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency, holdability)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql, columnIndexes)));
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, (PreparedStatement) trackStatement(this.delegate.prepareStatement(sql, columnNames)));
    }

    @Override // java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        markCommitStateDirty();
        return ProxyFactory.getProxyDatabaseMetaData(this, this.delegate.getMetaData());
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        this.delegate.commit();
        this.isCommitStateDirty = false;
        this.lastAccess = ClockSource.currentTime();
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        this.delegate.rollback();
        this.isCommitStateDirty = false;
        this.lastAccess = ClockSource.currentTime();
    }

    @Override // java.sql.Connection
    public void rollback(Savepoint savepoint) throws SQLException {
        this.delegate.rollback(savepoint);
        this.isCommitStateDirty = false;
        this.lastAccess = ClockSource.currentTime();
    }

    @Override // java.sql.Connection
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.delegate.setAutoCommit(autoCommit);
        this.isAutoCommit = autoCommit;
        this.dirtyBits |= 2;
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.delegate.setReadOnly(readOnly);
        this.isReadOnly = readOnly;
        this.isCommitStateDirty = false;
        this.dirtyBits |= 1;
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        this.delegate.setTransactionIsolation(level);
        this.transactionIsolation = level;
        this.dirtyBits |= 4;
    }

    @Override // java.sql.Connection
    public void setCatalog(String catalog) throws SQLException {
        this.delegate.setCatalog(catalog);
        this.dbcatalog = catalog;
        this.dirtyBits |= 8;
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.delegate.setNetworkTimeout(executor, milliseconds);
        this.networkTimeout = milliseconds;
        this.dirtyBits |= 16;
    }

    public void setSchema(String schema) throws SQLException {
        this.delegate.setSchema(schema);
        this.dbschema = schema;
        this.dirtyBits |= 32;
    }

    @Override // java.sql.Wrapper
    public final boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this.delegate) || (this.delegate != null && this.delegate.isWrapperFor(iface));
    }

    @Override // java.sql.Wrapper
    public final <T> T unwrap(Class<T> cls) throws SQLException {
        if (cls.isInstance(this.delegate)) {
            return (T) this.delegate;
        }
        if (this.delegate != null) {
            return (T) this.delegate.unwrap(cls);
        }
        throw new SQLException("Wrapped connection is not an instance of " + cls);
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/ProxyConnection$ClosedConnection.class */
    private static final class ClosedConnection {
        static final Connection CLOSED_CONNECTION = getClosedConnection();

        private ClosedConnection() {
        }

        private static Connection getClosedConnection() {
            InvocationHandler handler = (proxy, method, args) -> {
                String methodName = method.getName();
                if ("isClosed".equals(methodName)) {
                    return Boolean.TRUE;
                }
                if ("isValid".equals(methodName)) {
                    return Boolean.FALSE;
                }
                if ("abort".equals(methodName)) {
                    return Void.TYPE;
                }
                if ("close".equals(methodName)) {
                    return Void.TYPE;
                }
                if ("toString".equals(methodName)) {
                    return ClosedConnection.class.getCanonicalName();
                }
                throw new SQLException("Connection is closed");
            };
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, handler);
        }
    }
}
