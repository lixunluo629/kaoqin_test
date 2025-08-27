package com.mysql.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MysqlParameterMetadata.class */
public class MysqlParameterMetadata implements ParameterMetaData {
    boolean returnSimpleMetadata;
    ResultSetMetaData metadata;
    int parameterCount;
    private ExceptionInterceptor exceptionInterceptor;

    MysqlParameterMetadata(Field[] fieldInfo, int parameterCount, ExceptionInterceptor exceptionInterceptor) {
        this.returnSimpleMetadata = false;
        this.metadata = null;
        this.parameterCount = 0;
        this.metadata = new ResultSetMetaData(fieldInfo, false, true, exceptionInterceptor);
        this.parameterCount = parameterCount;
        this.exceptionInterceptor = exceptionInterceptor;
    }

    MysqlParameterMetadata(int count) {
        this.returnSimpleMetadata = false;
        this.metadata = null;
        this.parameterCount = 0;
        this.parameterCount = count;
        this.returnSimpleMetadata = true;
    }

    @Override // java.sql.ParameterMetaData
    public int getParameterCount() throws SQLException {
        return this.parameterCount;
    }

    @Override // java.sql.ParameterMetaData
    public int isNullable(int arg0) throws SQLException {
        checkAvailable();
        return this.metadata.isNullable(arg0);
    }

    private void checkAvailable() throws SQLException {
        if (this.metadata == null || this.metadata.fields == null) {
            throw SQLError.createSQLException("Parameter metadata not available for the given statement", SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, this.exceptionInterceptor);
        }
    }

    @Override // java.sql.ParameterMetaData
    public boolean isSigned(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return false;
        }
        checkAvailable();
        return this.metadata.isSigned(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public int getPrecision(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return 0;
        }
        checkAvailable();
        return this.metadata.getPrecision(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public int getScale(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return 0;
        }
        checkAvailable();
        return this.metadata.getScale(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public int getParameterType(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return 12;
        }
        checkAvailable();
        return this.metadata.getColumnType(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public String getParameterTypeName(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return "VARCHAR";
        }
        checkAvailable();
        return this.metadata.getColumnTypeName(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public String getParameterClassName(int arg0) throws SQLException {
        if (this.returnSimpleMetadata) {
            checkBounds(arg0);
            return "java.lang.String";
        }
        checkAvailable();
        return this.metadata.getColumnClassName(arg0);
    }

    @Override // java.sql.ParameterMetaData
    public int getParameterMode(int arg0) throws SQLException {
        return 1;
    }

    private void checkBounds(int paramNumber) throws SQLException {
        if (paramNumber < 1) {
            throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is invalid.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        if (paramNumber > this.parameterCount) {
            throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is greater than number of parameters, which is '" + this.parameterCount + "'.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
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
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
    }
}
