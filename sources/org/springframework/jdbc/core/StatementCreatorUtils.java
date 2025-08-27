package org.springframework.jdbc.core;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.SpringProperties;
import org.springframework.jdbc.support.SqlValue;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/StatementCreatorUtils.class */
public abstract class StatementCreatorUtils {
    public static final String IGNORE_GETPARAMETERTYPE_PROPERTY_NAME = "spring.jdbc.getParameterType.ignore";
    static final Boolean shouldIgnoreGetParameterType;
    static final Set<String> driversWithNoSupportForGetParameterType = Collections.newSetFromMap(new ConcurrentHashMap(1));
    private static final Log logger = LogFactory.getLog(StatementCreatorUtils.class);
    private static final Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap(32);

    static {
        String propVal = SpringProperties.getProperty(IGNORE_GETPARAMETERTYPE_PROPERTY_NAME);
        shouldIgnoreGetParameterType = propVal != null ? Boolean.valueOf(propVal) : null;
        javaTypeToSqlTypeMap.put(Boolean.TYPE, 16);
        javaTypeToSqlTypeMap.put(Boolean.class, 16);
        javaTypeToSqlTypeMap.put(Byte.TYPE, -6);
        javaTypeToSqlTypeMap.put(Byte.class, -6);
        javaTypeToSqlTypeMap.put(Short.TYPE, 5);
        javaTypeToSqlTypeMap.put(Short.class, 5);
        javaTypeToSqlTypeMap.put(Integer.TYPE, 4);
        javaTypeToSqlTypeMap.put(Integer.class, 4);
        javaTypeToSqlTypeMap.put(Long.TYPE, -5);
        javaTypeToSqlTypeMap.put(Long.class, -5);
        javaTypeToSqlTypeMap.put(BigInteger.class, -5);
        javaTypeToSqlTypeMap.put(Float.TYPE, 6);
        javaTypeToSqlTypeMap.put(Float.class, 6);
        javaTypeToSqlTypeMap.put(Double.TYPE, 8);
        javaTypeToSqlTypeMap.put(Double.class, 8);
        javaTypeToSqlTypeMap.put(BigDecimal.class, 3);
        javaTypeToSqlTypeMap.put(Date.class, 91);
        javaTypeToSqlTypeMap.put(Time.class, 92);
        javaTypeToSqlTypeMap.put(Timestamp.class, 93);
        javaTypeToSqlTypeMap.put(Blob.class, 2004);
        javaTypeToSqlTypeMap.put(Clob.class, 2005);
    }

    public static int javaTypeToSqlParameterType(Class<?> javaType) {
        Integer sqlType = javaTypeToSqlTypeMap.get(javaType);
        if (sqlType != null) {
            return sqlType.intValue();
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return 2;
        }
        if (isStringValue(javaType)) {
            return 12;
        }
        if (isDateValue(javaType) || Calendar.class.isAssignableFrom(javaType)) {
            return 93;
        }
        return Integer.MIN_VALUE;
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, SqlParameter param, Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, param.getSqlType(), param.getTypeName(), param.getScale(), inValue);
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, sqlType, null, null, inValue);
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, sqlType, typeName, null, inValue);
    }

    private static void setParameterValueInternal(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Integer scale, Object inValue) throws SQLException {
        String typeNameToUse = typeName;
        int sqlTypeToUse = sqlType;
        Object inValueToUse = inValue;
        if (inValue instanceof SqlParameterValue) {
            SqlParameterValue parameterValue = (SqlParameterValue) inValue;
            if (logger.isDebugEnabled()) {
                logger.debug("Overriding type info with runtime info from SqlParameterValue: column index " + paramIndex + ", SQL type " + parameterValue.getSqlType() + ", type name " + parameterValue.getTypeName());
            }
            if (parameterValue.getSqlType() != Integer.MIN_VALUE) {
                sqlTypeToUse = parameterValue.getSqlType();
            }
            if (parameterValue.getTypeName() != null) {
                typeNameToUse = parameterValue.getTypeName();
            }
            inValueToUse = parameterValue.getValue();
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Setting SQL statement parameter value: column index " + paramIndex + ", parameter value [" + inValueToUse + "], value class [" + (inValueToUse != null ? inValueToUse.getClass().getName() : "null") + "], SQL type " + (sqlTypeToUse == Integer.MIN_VALUE ? "unknown" : Integer.toString(sqlTypeToUse)));
        }
        if (inValueToUse == null) {
            setNull(ps, paramIndex, sqlTypeToUse, typeNameToUse);
        } else {
            setValue(ps, paramIndex, sqlTypeToUse, typeNameToUse, scale, inValueToUse);
        }
    }

    private static void setNull(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
        if (sqlType == Integer.MIN_VALUE || (sqlType == 1111 && typeName == null)) {
            boolean useSetObject = false;
            Integer sqlTypeToUse = null;
            DatabaseMetaData dbmd = null;
            String jdbcDriverName = null;
            boolean tryGetParameterType = true;
            if (shouldIgnoreGetParameterType == null) {
                try {
                    dbmd = ps.getConnection().getMetaData();
                    jdbcDriverName = dbmd.getDriverName();
                    tryGetParameterType = !driversWithNoSupportForGetParameterType.contains(jdbcDriverName);
                    if (tryGetParameterType && jdbcDriverName.startsWith("Oracle")) {
                        tryGetParameterType = false;
                        driversWithNoSupportForGetParameterType.add(jdbcDriverName);
                    }
                } catch (Throwable ex) {
                    logger.debug("Could not check connection metadata", ex);
                }
            } else {
                tryGetParameterType = !shouldIgnoreGetParameterType.booleanValue();
            }
            if (tryGetParameterType) {
                try {
                    sqlTypeToUse = Integer.valueOf(ps.getParameterMetaData().getParameterType(paramIndex));
                } catch (Throwable ex2) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("JDBC 3.0 getParameterType call not supported - using fallback method instead: " + ex2);
                    }
                }
            }
            if (sqlTypeToUse == null) {
                sqlTypeToUse = 0;
                if (dbmd == null) {
                    try {
                        dbmd = ps.getConnection().getMetaData();
                    } catch (Throwable ex3) {
                        logger.debug("Could not check connection metadata", ex3);
                    }
                }
                if (jdbcDriverName == null) {
                    jdbcDriverName = dbmd.getDriverName();
                }
                if (shouldIgnoreGetParameterType == null) {
                    driversWithNoSupportForGetParameterType.add(jdbcDriverName);
                }
                String databaseProductName = dbmd.getDatabaseProductName();
                if (databaseProductName.startsWith("Informix") || (jdbcDriverName.startsWith("Microsoft") && jdbcDriverName.contains("SQL Server"))) {
                    useSetObject = true;
                } else if (databaseProductName.startsWith("DB2") || jdbcDriverName.startsWith("jConnect") || jdbcDriverName.startsWith("SQLServer") || jdbcDriverName.startsWith("Apache Derby")) {
                    sqlTypeToUse = 12;
                }
            }
            if (useSetObject) {
                ps.setObject(paramIndex, null);
                return;
            } else {
                ps.setNull(paramIndex, sqlTypeToUse.intValue());
                return;
            }
        }
        if (typeName != null) {
            ps.setNull(paramIndex, sqlType, typeName);
        } else {
            ps.setNull(paramIndex, sqlType);
        }
    }

    private static void setValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Integer scale, Object inValue) throws SQLException {
        if (inValue instanceof SqlTypeValue) {
            ((SqlTypeValue) inValue).setTypeValue(ps, paramIndex, sqlType, typeName);
            return;
        }
        if (inValue instanceof SqlValue) {
            ((SqlValue) inValue).setValue(ps, paramIndex);
            return;
        }
        if (sqlType == 12 || sqlType == -1) {
            ps.setString(paramIndex, inValue.toString());
            return;
        }
        if (sqlType == -9 || sqlType == -16) {
            ps.setNString(paramIndex, inValue.toString());
            return;
        }
        if ((sqlType == 2005 || sqlType == 2011) && isStringValue(inValue.getClass())) {
            String strVal = inValue.toString();
            if (strVal.length() > 4000) {
                try {
                    if (sqlType == 2011) {
                        ps.setNClob(paramIndex, new StringReader(strVal), strVal.length());
                    } else {
                        ps.setClob(paramIndex, new StringReader(strVal), strVal.length());
                    }
                    return;
                } catch (AbstractMethodError err) {
                    logger.debug("JDBC driver does not implement JDBC 4.0 'setClob(int, Reader, long)' method", err);
                } catch (SQLFeatureNotSupportedException ex) {
                    logger.debug("JDBC driver does not support JDBC 4.0 'setClob(int, Reader, long)' method", ex);
                }
            }
            if (sqlType == 2011) {
                ps.setNString(paramIndex, strVal);
                return;
            } else {
                ps.setString(paramIndex, strVal);
                return;
            }
        }
        if (sqlType == 3 || sqlType == 2) {
            if (inValue instanceof BigDecimal) {
                ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
                return;
            } else if (scale != null) {
                ps.setObject(paramIndex, inValue, sqlType, scale.intValue());
                return;
            } else {
                ps.setObject(paramIndex, inValue, sqlType);
                return;
            }
        }
        if (sqlType == 16) {
            if (inValue instanceof Boolean) {
                ps.setBoolean(paramIndex, ((Boolean) inValue).booleanValue());
                return;
            } else {
                ps.setObject(paramIndex, inValue, 16);
                return;
            }
        }
        if (sqlType == 91) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof Date) {
                    ps.setDate(paramIndex, (Date) inValue);
                    return;
                } else {
                    ps.setDate(paramIndex, new Date(((java.util.Date) inValue).getTime()));
                    return;
                }
            }
            if (inValue instanceof Calendar) {
                Calendar cal = (Calendar) inValue;
                ps.setDate(paramIndex, new Date(cal.getTime().getTime()), cal);
                return;
            } else {
                ps.setObject(paramIndex, inValue, 91);
                return;
            }
        }
        if (sqlType == 92) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof Time) {
                    ps.setTime(paramIndex, (Time) inValue);
                    return;
                } else {
                    ps.setTime(paramIndex, new Time(((java.util.Date) inValue).getTime()));
                    return;
                }
            }
            if (inValue instanceof Calendar) {
                Calendar cal2 = (Calendar) inValue;
                ps.setTime(paramIndex, new Time(cal2.getTime().getTime()), cal2);
                return;
            } else {
                ps.setObject(paramIndex, inValue, 92);
                return;
            }
        }
        if (sqlType == 93) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof Timestamp) {
                    ps.setTimestamp(paramIndex, (Timestamp) inValue);
                    return;
                } else {
                    ps.setTimestamp(paramIndex, new Timestamp(((java.util.Date) inValue).getTime()));
                    return;
                }
            }
            if (inValue instanceof Calendar) {
                Calendar cal3 = (Calendar) inValue;
                ps.setTimestamp(paramIndex, new Timestamp(cal3.getTime().getTime()), cal3);
                return;
            } else {
                ps.setObject(paramIndex, inValue, 93);
                return;
            }
        }
        if (sqlType == Integer.MIN_VALUE || (sqlType == 1111 && "Oracle".equals(ps.getConnection().getMetaData().getDatabaseProductName()))) {
            if (isStringValue(inValue.getClass())) {
                ps.setString(paramIndex, inValue.toString());
                return;
            }
            if (isDateValue(inValue.getClass())) {
                ps.setTimestamp(paramIndex, new Timestamp(((java.util.Date) inValue).getTime()));
                return;
            } else if (inValue instanceof Calendar) {
                Calendar cal4 = (Calendar) inValue;
                ps.setTimestamp(paramIndex, new Timestamp(cal4.getTime().getTime()), cal4);
                return;
            } else {
                ps.setObject(paramIndex, inValue);
                return;
            }
        }
        ps.setObject(paramIndex, inValue, sqlType);
    }

    private static boolean isStringValue(Class<?> inValueType) {
        return CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType);
    }

    private static boolean isDateValue(Class<?> inValueType) {
        return (!java.util.Date.class.isAssignableFrom(inValueType) || Date.class.isAssignableFrom(inValueType) || Time.class.isAssignableFrom(inValueType) || Timestamp.class.isAssignableFrom(inValueType)) ? false : true;
    }

    public static void cleanupParameters(Object... paramValues) {
        if (paramValues != null) {
            cleanupParameters(Arrays.asList(paramValues));
        }
    }

    public static void cleanupParameters(Collection<?> paramValues) {
        if (paramValues != null) {
            for (Object inValue : paramValues) {
                if (inValue instanceof DisposableSqlTypeValue) {
                    ((DisposableSqlTypeValue) inValue).cleanup();
                } else if (inValue instanceof SqlValue) {
                    ((SqlValue) inValue).cleanup();
                }
            }
        }
    }
}
