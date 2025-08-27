package org.springframework.jdbc.support.nativejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/Jdbc4NativeJdbcExtractor.class */
public class Jdbc4NativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private Class<? extends Connection> connectionType = Connection.class;
    private Class<? extends Statement> statementType = Statement.class;
    private Class<? extends PreparedStatement> preparedStatementType = PreparedStatement.class;
    private Class<? extends CallableStatement> callableStatementType = CallableStatement.class;
    private Class<? extends ResultSet> resultSetType = ResultSet.class;

    public void setConnectionType(Class<? extends Connection> connectionType) {
        this.connectionType = connectionType;
    }

    public void setStatementType(Class<? extends Statement> statementType) {
        this.statementType = statementType;
    }

    public void setPreparedStatementType(Class<? extends PreparedStatement> preparedStatementType) {
        this.preparedStatementType = preparedStatementType;
    }

    public void setCallableStatementType(Class<? extends CallableStatement> callableStatementType) {
        this.callableStatementType = callableStatementType;
    }

    public void setResultSetType(Class<? extends ResultSet> resultSetType) {
        this.resultSetType = resultSetType;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter
    protected Connection doGetNativeConnection(Connection con) throws SQLException {
        return (Connection) con.unwrap(this.connectionType);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Statement getNativeStatement(Statement stmt) throws SQLException {
        return (Statement) stmt.unwrap(this.statementType);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
        return (PreparedStatement) ps.unwrap(this.preparedStatementType);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
        return (CallableStatement) cs.unwrap(this.callableStatementType);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
        return (ResultSet) rs.unwrap(this.resultSetType);
    }
}
