package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SingleColumnRowMapper.class */
public class SingleColumnRowMapper<T> implements RowMapper<T> {
    private Class<?> requiredType;

    public SingleColumnRowMapper() {
    }

    public SingleColumnRowMapper(Class<T> requiredType) {
        setRequiredType(requiredType);
    }

    public void setRequiredType(Class<T> requiredType) {
        this.requiredType = ClassUtils.resolvePrimitiveIfNecessary(requiredType);
    }

    @Override // org.springframework.jdbc.core.RowMapper
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        if (columnCount != 1) {
            throw new IncorrectResultSetColumnCountException(1, columnCount);
        }
        T t = (T) getColumnValue(resultSet, 1, this.requiredType);
        if (t != null && this.requiredType != null && !this.requiredType.isInstance(t)) {
            try {
                return (T) convertValueToRequiredType(t, this.requiredType);
            } catch (IllegalArgumentException e) {
                throw new TypeMismatchDataAccessException("Type mismatch affecting row number " + i + " and column type '" + metaData.getColumnTypeName(1) + "': " + e.getMessage());
            }
        }
        return t;
    }

    protected Object getColumnValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        if (requiredType != null) {
            return JdbcUtils.getResultSetValue(rs, index, requiredType);
        }
        return getColumnValue(rs, index);
    }

    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }

    protected Object convertValueToRequiredType(Object value, Class<?> requiredType) {
        if (String.class == requiredType) {
            return value.toString();
        }
        if (Number.class.isAssignableFrom(requiredType)) {
            if (value instanceof Number) {
                return NumberUtils.convertNumberToTargetClass((Number) value, requiredType);
            }
            return NumberUtils.parseNumber(value.toString(), requiredType);
        }
        throw new IllegalArgumentException("Value [" + value + "] is of type [" + value.getClass().getName() + "] and cannot be converted to required type [" + requiredType.getName() + "]");
    }

    public static <T> SingleColumnRowMapper<T> newInstance(Class<T> requiredType) {
        return new SingleColumnRowMapper<>(requiredType);
    }
}
