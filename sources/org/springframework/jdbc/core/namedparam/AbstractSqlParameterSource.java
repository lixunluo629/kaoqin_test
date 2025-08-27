package org.springframework.jdbc.core.namedparam;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/AbstractSqlParameterSource.class */
public abstract class AbstractSqlParameterSource implements SqlParameterSource {
    private final Map<String, Integer> sqlTypes = new HashMap();
    private final Map<String, String> typeNames = new HashMap();

    public void registerSqlType(String paramName, int sqlType) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.sqlTypes.put(paramName, Integer.valueOf(sqlType));
    }

    public void registerTypeName(String paramName, String typeName) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.typeNames.put(paramName, typeName);
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public int getSqlType(String paramName) {
        Assert.notNull(paramName, "Parameter name must not be null");
        Integer sqlType = this.sqlTypes.get(paramName);
        if (sqlType != null) {
            return sqlType.intValue();
        }
        return Integer.MIN_VALUE;
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public String getTypeName(String paramName) {
        Assert.notNull(paramName, "Parameter name must not be null");
        return this.typeNames.get(paramName);
    }
}
