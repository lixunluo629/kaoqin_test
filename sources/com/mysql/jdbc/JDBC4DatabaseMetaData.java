package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4DatabaseMetaData.class */
public class JDBC4DatabaseMetaData extends DatabaseMetaData {
    public JDBC4DatabaseMetaData(MySQLConnection connToSet, String databaseToSet) {
        super(connToSet, databaseToSet);
    }

    @Override // java.sql.DatabaseMetaData
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return RowIdLifetime.ROWID_UNSUPPORTED;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException e) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.conn.getExceptionInterceptor());
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;
    }

    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        Field[] fields = createProcedureColumnsFields();
        return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
    }

    @Override // com.mysql.jdbc.DatabaseMetaData, java.sql.DatabaseMetaData
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        Field[] fields = createFieldMetadataForGetProcedures();
        return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
    }

    @Override // com.mysql.jdbc.DatabaseMetaData
    protected int getJDBC4FunctionNoTableConstant() {
        return 1;
    }

    @Override // com.mysql.jdbc.DatabaseMetaData
    protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns) {
        return getProcedureOrFunctionColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns);
    }

    protected static int getProcedureOrFunctionColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns) {
        return (isInParam && isOutParam) ? forGetFunctionColumns ? 2 : 2 : isInParam ? forGetFunctionColumns ? 1 : 1 : isOutParam ? forGetFunctionColumns ? 3 : 4 : isReturnParam ? forGetFunctionColumns ? 4 : 5 : forGetFunctionColumns ? 0 : 0;
    }
}
