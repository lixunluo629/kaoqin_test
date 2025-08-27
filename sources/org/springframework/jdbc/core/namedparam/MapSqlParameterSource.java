package org.springframework.jdbc.core.namedparam;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/MapSqlParameterSource.class */
public class MapSqlParameterSource extends AbstractSqlParameterSource {
    private final Map<String, Object> values = new LinkedHashMap();

    public MapSqlParameterSource() {
    }

    public MapSqlParameterSource(String paramName, Object value) {
        addValue(paramName, value);
    }

    public MapSqlParameterSource(Map<String, ?> values) {
        addValues(values);
    }

    public MapSqlParameterSource addValue(String paramName, Object value) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        if (value instanceof SqlParameterValue) {
            registerSqlType(paramName, ((SqlParameterValue) value).getSqlType());
        }
        return this;
    }

    public MapSqlParameterSource addValue(String paramName, Object value, int sqlType) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        registerSqlType(paramName, sqlType);
        return this;
    }

    public MapSqlParameterSource addValue(String paramName, Object value, int sqlType, String typeName) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        registerSqlType(paramName, sqlType);
        registerTypeName(paramName, typeName);
        return this;
    }

    public MapSqlParameterSource addValues(Map<String, ?> values) {
        if (values != null) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                this.values.put(entry.getKey(), entry.getValue());
                if (entry.getValue() instanceof SqlParameterValue) {
                    SqlParameterValue value = (SqlParameterValue) entry.getValue();
                    registerSqlType(entry.getKey(), value.getSqlType());
                }
            }
        }
        return this;
    }

    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public boolean hasValue(String paramName) {
        return this.values.containsKey(paramName);
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public Object getValue(String paramName) {
        if (!hasValue(paramName)) {
            throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
        }
        return this.values.get(paramName);
    }
}
