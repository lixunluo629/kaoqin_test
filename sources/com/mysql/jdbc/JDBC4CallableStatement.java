package com.mysql.jdbc;

import com.mysql.jdbc.CallableStatement;
import java.io.Reader;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4CallableStatement.class */
public class JDBC4CallableStatement extends CallableStatement {
    public JDBC4CallableStatement(MySQLConnection conn, CallableStatement.CallableStatementParamInfo paramInfo) throws SQLException {
        super(conn, paramInfo);
    }

    public JDBC4CallableStatement(MySQLConnection conn, String sql, String catalog, boolean isFunctionCall) throws SQLException {
        super(conn, sql, catalog, isFunctionCall);
    }

    @Override // com.mysql.jdbc.CallableStatement
    protected ResultSet getParamTypes(String catalog, String routineName) throws SQLException {
        java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();
        if (this.callingStoredFunction) {
            return dbmd.getFunctionColumns(catalog, null, routineName, QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
        }
        boolean getProcRetFuncsCurrentValue = this.connection.getGetProceduresReturnsFunctions();
        try {
            this.connection.setGetProceduresReturnsFunctions(false);
            ResultSet procedureColumns = dbmd.getProcedureColumns(catalog, null, routineName, QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            this.connection.setGetProceduresReturnsFunctions(getProcRetFuncsCurrentValue);
            return procedureColumns;
        } catch (Throwable th) {
            this.connection.setGetProceduresReturnsFunctions(getProcRetFuncsCurrentValue);
            throw th;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        JDBC4PreparedStatementHelper.setRowId(this, parameterIndex, x);
    }

    @Override // java.sql.CallableStatement
    public void setRowId(String parameterName, RowId x) throws SQLException {
        JDBC4PreparedStatementHelper.setRowId(this, getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.PreparedStatement
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        JDBC4PreparedStatementHelper.setSQLXML(this, parameterIndex, xmlObject);
    }

    @Override // java.sql.CallableStatement
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        JDBC4PreparedStatementHelper.setSQLXML(this, getNamedParamIndex(parameterName, false), xmlObject);
    }

    @Override // java.sql.CallableStatement
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        SQLXML retValue = ((JDBC4ResultSet) rs).getSQLXML(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        SQLXML retValue = ((JDBC4ResultSet) rs).getSQLXML(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public RowId getRowId(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        RowId retValue = ((JDBC4ResultSet) rs).getRowId(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public RowId getRowId(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        RowId retValue = ((JDBC4ResultSet) rs).getRowId(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        JDBC4PreparedStatementHelper.setNClob(this, parameterIndex, value);
    }

    @Override // java.sql.CallableStatement
    public void setNClob(String parameterName, NClob value) throws SQLException {
        JDBC4PreparedStatementHelper.setNClob(this, getNamedParamIndex(parameterName, false), value);
    }

    @Override // java.sql.CallableStatement
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        setNClob(getNamedParamIndex(parameterName, false), reader);
    }

    @Override // java.sql.CallableStatement
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        setNClob(getNamedParamIndex(parameterName, false), reader, length);
    }

    @Override // java.sql.CallableStatement
    public void setNString(String parameterName, String value) throws SQLException {
        setNString(getNamedParamIndex(parameterName, false), value);
    }

    @Override // java.sql.CallableStatement
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        Reader retValue = rs.getCharacterStream(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Reader getCharacterStream(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        Reader retValue = rs.getCharacterStream(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        Reader retValue = ((JDBC4ResultSet) rs).getNCharacterStream(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        Reader retValue = ((JDBC4ResultSet) rs).getNCharacterStream(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public NClob getNClob(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        NClob retValue = ((JDBC4ResultSet) rs).getNClob(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public NClob getNClob(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        NClob retValue = ((JDBC4ResultSet) rs).getNClob(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public String getNString(int parameterIndex) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
        String retValue = ((JDBC4ResultSet) rs).getNString(mapOutputParameterIndexToRsIndex(parameterIndex));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public String getNString(String parameterName) throws SQLException {
        ResultSetInternalMethods rs = getOutputParameters(0);
        String retValue = ((JDBC4ResultSet) rs).getNString(fixParameterName(parameterName));
        this.outputParamWasNull = rs.wasNull();
        return retValue;
    }
}
