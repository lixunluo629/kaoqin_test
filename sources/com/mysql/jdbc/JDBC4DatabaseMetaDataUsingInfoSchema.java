package com.mysql.jdbc;

import com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4DatabaseMetaDataUsingInfoSchema.class */
public class JDBC4DatabaseMetaDataUsingInfoSchema extends DatabaseMetaDataUsingInfoSchema {
    public JDBC4DatabaseMetaDataUsingInfoSchema(MySQLConnection connToSet, String databaseToSet) throws SQLException {
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

    @Override // com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema
    protected ResultSet getProcedureColumnsNoISParametersView(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        Field[] fields = createProcedureColumnsFields();
        return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
    }

    @Override // com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema
    protected String getRoutineTypeConditionForGetProcedures() {
        return this.conn.getGetProceduresReturnsFunctions() ? "" : "ROUTINE_TYPE = 'PROCEDURE' AND ";
    }

    @Override // com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema
    protected String getRoutineTypeConditionForGetProcedureColumns() {
        return this.conn.getGetProceduresReturnsFunctions() ? "" : "ROUTINE_TYPE = 'PROCEDURE' AND ";
    }

    @Override // com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema
    protected int getJDBC4FunctionConstant(DatabaseMetaDataUsingInfoSchema.JDBC4FunctionConstant constant) {
        switch (constant) {
            case FUNCTION_COLUMN_IN:
                return 1;
            case FUNCTION_COLUMN_INOUT:
                return 2;
            case FUNCTION_COLUMN_OUT:
                return 3;
            case FUNCTION_COLUMN_RETURN:
                return 4;
            case FUNCTION_COLUMN_RESULT:
                return 5;
            case FUNCTION_COLUMN_UNKNOWN:
                return 0;
            case FUNCTION_NO_NULLS:
                return 0;
            case FUNCTION_NULLABLE:
                return 1;
            case FUNCTION_NULLABLE_UNKNOWN:
                return 2;
            default:
                return -1;
        }
    }

    @Override // com.mysql.jdbc.DatabaseMetaDataUsingInfoSchema, com.mysql.jdbc.DatabaseMetaData
    protected int getJDBC4FunctionNoTableConstant() {
        return 1;
    }

    @Override // com.mysql.jdbc.DatabaseMetaData
    protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns) {
        return JDBC4DatabaseMetaData.getProcedureOrFunctionColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns);
    }
}
