package com.zaxxer.hikari.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/ProxyResultSet.class */
public abstract class ProxyResultSet implements ResultSet {
    protected final ProxyConnection connection;
    protected final ProxyStatement statement;
    final ResultSet delegate;

    protected ProxyResultSet(ProxyConnection connection, ProxyStatement statement, ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.delegate = resultSet;
    }

    final SQLException checkException(SQLException e) {
        return this.connection.checkException(e);
    }

    public String toString() {
        return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + this.delegate;
    }

    @Override // java.sql.ResultSet
    public final Statement getStatement() throws SQLException {
        return this.statement;
    }

    @Override // java.sql.ResultSet
    public void updateRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.updateRow();
    }

    @Override // java.sql.ResultSet
    public void insertRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.insertRow();
    }

    @Override // java.sql.ResultSet
    public void deleteRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.deleteRow();
    }

    @Override // java.sql.Wrapper
    public final <T> T unwrap(Class<T> cls) throws SQLException {
        if (cls.isInstance(this.delegate)) {
            return (T) this.delegate;
        }
        if (this.delegate != null) {
            return (T) this.delegate.unwrap(cls);
        }
        throw new SQLException("Wrapped ResultSet is not an instance of " + cls);
    }
}
