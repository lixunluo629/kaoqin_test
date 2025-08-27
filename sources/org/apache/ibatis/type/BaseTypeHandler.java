package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/BaseTypeHandler.class */
public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T> {
    protected Configuration configuration;

    public abstract void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException;

    public abstract T getNullableResult(ResultSet resultSet, String str) throws SQLException;

    public abstract T getNullableResult(ResultSet resultSet, int i) throws SQLException;

    public abstract T getNullableResult(CallableStatement callableStatement, int i) throws SQLException;

    public void setConfiguration(Configuration c) {
        this.configuration = c;
    }

    @Override // org.apache.ibatis.type.TypeHandler
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
                return;
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: " + e, e);
            }
        }
        try {
            setNonNullParameter(ps, i, parameter, jdbcType);
        } catch (Exception e2) {
            throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different configuration property. Cause: " + e2, e2);
        }
    }

    @Override // org.apache.ibatis.type.TypeHandler
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            T result = getNullableResult(rs, columnName);
            if (rs.wasNull()) {
                return null;
            }
            return result;
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
        }
    }

    @Override // org.apache.ibatis.type.TypeHandler
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            T result = getNullableResult(rs, columnIndex);
            if (rs.wasNull()) {
                return null;
            }
            return result;
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + e, e);
        }
    }

    @Override // org.apache.ibatis.type.TypeHandler
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            T result = getNullableResult(cs, columnIndex);
            if (cs.wasNull()) {
                return null;
            }
            return result;
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
    }
}
