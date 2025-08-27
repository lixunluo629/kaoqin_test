package com.mysql.jdbc;

import com.mysql.jdbc.ServerPreparedStatement;
import java.io.Reader;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4ServerPreparedStatement.class */
public class JDBC4ServerPreparedStatement extends ServerPreparedStatement {
    public JDBC4ServerPreparedStatement(MySQLConnection conn, String sql, String catalog, int resultSetType, int resultSetConcurrency) throws SQLException {
        super(conn, sql, catalog, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        if (!this.charEncoding.equalsIgnoreCase("UTF-8") && !this.charEncoding.equalsIgnoreCase("utf8")) {
            throw SQLError.createSQLException("Can not call setNCharacterStream() when connection character set isn't UTF-8", getExceptionInterceptor());
        }
        checkClosed();
        if (reader == null) {
            setNull(parameterIndex, -2);
            return;
        }
        ServerPreparedStatement.BindValue binding = getBinding(parameterIndex, true);
        resetToType(binding, 252);
        binding.value = reader;
        binding.isLongData = true;
        if (this.connection.getUseStreamLengthsInPrepStmts()) {
            binding.bindLength = length;
        } else {
            binding.bindLength = -1L;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        setNClob(parameterIndex, x.getCharacterStream(), this.connection.getUseStreamLengthsInPrepStmts() ? x.length() : -1L);
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        if (!this.charEncoding.equalsIgnoreCase("UTF-8") && !this.charEncoding.equalsIgnoreCase("utf8")) {
            throw SQLError.createSQLException("Can not call setNClob() when connection character set isn't UTF-8", getExceptionInterceptor());
        }
        checkClosed();
        if (reader == null) {
            setNull(parameterIndex, 2011);
            return;
        }
        ServerPreparedStatement.BindValue binding = getBinding(parameterIndex, true);
        resetToType(binding, 252);
        binding.value = reader;
        binding.isLongData = true;
        if (this.connection.getUseStreamLengthsInPrepStmts()) {
            binding.bindLength = length;
        } else {
            binding.bindLength = -1L;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setNString(int parameterIndex, String x) throws SQLException {
        if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
            setString(parameterIndex, x);
            return;
        }
        throw SQLError.createSQLException("Can not call setNString() when connection character set isn't UTF-8", getExceptionInterceptor());
    }

    @Override // java.sql.PreparedStatement
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        JDBC4PreparedStatementHelper.setRowId(this, parameterIndex, x);
    }

    @Override // java.sql.PreparedStatement
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        JDBC4PreparedStatementHelper.setSQLXML(this, parameterIndex, xmlObject);
    }
}
