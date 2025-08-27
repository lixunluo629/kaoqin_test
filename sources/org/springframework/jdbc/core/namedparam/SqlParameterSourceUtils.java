package org.springframework.jdbc.core.namedparam;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.SqlParameterValue;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/SqlParameterSourceUtils.class */
public class SqlParameterSourceUtils {
    public static SqlParameterSource[] createBatch(Map<String, ?>[] valueMaps) {
        MapSqlParameterSource[] batch = new MapSqlParameterSource[valueMaps.length];
        for (int i = 0; i < valueMaps.length; i++) {
            batch[i] = new MapSqlParameterSource(valueMaps[i]);
        }
        return batch;
    }

    public static SqlParameterSource[] createBatch(Object[] beans) {
        BeanPropertySqlParameterSource[] batch = new BeanPropertySqlParameterSource[beans.length];
        for (int i = 0; i < beans.length; i++) {
            batch[i] = new BeanPropertySqlParameterSource(beans[i]);
        }
        return batch;
    }

    public static Object getTypedValue(SqlParameterSource source, String parameterName) {
        int sqlType = source.getSqlType(parameterName);
        if (sqlType != Integer.MIN_VALUE) {
            if (source.getTypeName(parameterName) != null) {
                return new SqlParameterValue(sqlType, source.getTypeName(parameterName), source.getValue(parameterName));
            }
            return new SqlParameterValue(sqlType, source.getValue(parameterName));
        }
        return source.getValue(parameterName);
    }

    public static Map<String, String> extractCaseInsensitiveParameterNames(SqlParameterSource parameterSource) {
        Map<String, String> caseInsensitiveParameterNames = new HashMap<>();
        if (parameterSource instanceof BeanPropertySqlParameterSource) {
            String[] propertyNames = ((BeanPropertySqlParameterSource) parameterSource).getReadablePropertyNames();
            for (String name : propertyNames) {
                caseInsensitiveParameterNames.put(name.toLowerCase(), name);
            }
        } else if (parameterSource instanceof MapSqlParameterSource) {
            for (String name2 : ((MapSqlParameterSource) parameterSource).getValues().keySet()) {
                caseInsensitiveParameterNames.put(name2.toLowerCase(), name2);
            }
        }
        return caseInsensitiveParameterNames;
    }
}
