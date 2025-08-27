package org.springframework.jdbc.support;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.UsesJava7;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/JdbcUtils.class */
public abstract class JdbcUtils {
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;
    private static final boolean getObjectWithTypeAvailable = ClassUtils.hasMethod(ResultSet.class, "getObject", Integer.TYPE, Class.class);
    private static final Log logger = LogFactory.getLog(JdbcUtils.class);

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                logger.debug("Could not close JDBC Connection", ex);
            } catch (Throwable ex2) {
                logger.debug("Unexpected exception on closing JDBC Connection", ex2);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC Statement", ex);
            } catch (Throwable ex2) {
                logger.trace("Unexpected exception on closing JDBC Statement", ex2);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC ResultSet", ex);
            } catch (Throwable ex2) {
                logger.trace("Unexpected exception on closing JDBC ResultSet", ex2);
            }
        }
    }

    @UsesJava7
    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        Object value;
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }
        if (String.class == requiredType) {
            return rs.getString(index);
        }
        if (Boolean.TYPE == requiredType || Boolean.class == requiredType) {
            value = Boolean.valueOf(rs.getBoolean(index));
        } else if (Byte.TYPE == requiredType || Byte.class == requiredType) {
            value = Byte.valueOf(rs.getByte(index));
        } else if (Short.TYPE == requiredType || Short.class == requiredType) {
            value = Short.valueOf(rs.getShort(index));
        } else if (Integer.TYPE == requiredType || Integer.class == requiredType) {
            value = Integer.valueOf(rs.getInt(index));
        } else if (Long.TYPE == requiredType || Long.class == requiredType) {
            value = Long.valueOf(rs.getLong(index));
        } else if (Float.TYPE == requiredType || Float.class == requiredType) {
            value = Float.valueOf(rs.getFloat(index));
        } else if (Double.TYPE == requiredType || Double.class == requiredType || Number.class == requiredType) {
            value = Double.valueOf(rs.getDouble(index));
        } else {
            if (BigDecimal.class == requiredType) {
                return rs.getBigDecimal(index);
            }
            if (Date.class == requiredType) {
                return rs.getDate(index);
            }
            if (Time.class == requiredType) {
                return rs.getTime(index);
            }
            if (Timestamp.class == requiredType || java.util.Date.class == requiredType) {
                return rs.getTimestamp(index);
            }
            if (byte[].class == requiredType) {
                return rs.getBytes(index);
            }
            if (Blob.class == requiredType) {
                return rs.getBlob(index);
            }
            if (Clob.class == requiredType) {
                return rs.getClob(index);
            }
            if (requiredType.isEnum()) {
                Object obj = rs.getObject(index);
                if (obj instanceof String) {
                    return obj;
                }
                if (obj instanceof Number) {
                    return NumberUtils.convertNumberToTargetClass((Number) obj, Integer.class);
                }
                return rs.getString(index);
            }
            if (getObjectWithTypeAvailable) {
                try {
                    return rs.getObject(index, requiredType);
                } catch (AbstractMethodError err) {
                    logger.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
                } catch (SQLFeatureNotSupportedException ex) {
                    logger.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
                } catch (SQLException ex2) {
                    logger.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex2);
                }
            }
            String typeName = requiredType.getSimpleName();
            if ("LocalDate".equals(typeName)) {
                return rs.getDate(index);
            }
            if ("LocalTime".equals(typeName)) {
                return rs.getTime(index);
            }
            if ("LocalDateTime".equals(typeName)) {
                return rs.getTimestamp(index);
            }
            return getResultSetValue(rs, index);
        }
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1L, (int) blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1L, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            obj = ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) ? rs.getTimestamp(index) : rs.getDate(index);
        } else if ((obj instanceof Date) && "java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
            obj = rs.getTimestamp(index);
        }
        return obj;
    }

    public static Object extractDatabaseMetaData(DataSource dataSource, DatabaseMetaDataCallback action) throws MetaDataAccessException {
        try {
            try {
                try {
                    Connection con = DataSourceUtils.getConnection(dataSource);
                    if (con == null) {
                        throw new MetaDataAccessException("Connection returned by DataSource [" + dataSource + "] was null");
                    }
                    DatabaseMetaData metaData = con.getMetaData();
                    if (metaData == null) {
                        throw new MetaDataAccessException("DatabaseMetaData returned by Connection [" + con + "] was null");
                    }
                    Object objProcessMetaData = action.processMetaData(metaData);
                    DataSourceUtils.releaseConnection(con, dataSource);
                    return objProcessMetaData;
                } catch (SQLException ex) {
                    throw new MetaDataAccessException("Error while extracting DatabaseMetaData", ex);
                } catch (CannotGetJdbcConnectionException ex2) {
                    throw new MetaDataAccessException("Could not get Connection for extracting meta-data", ex2);
                }
            } catch (AbstractMethodError err) {
                throw new MetaDataAccessException("JDBC DatabaseMetaData method not implemented by JDBC driver - upgrade your driver", err);
            }
        } catch (Throwable th) {
            DataSourceUtils.releaseConnection(null, dataSource);
            throw th;
        }
    }

    public static Object extractDatabaseMetaData(DataSource dataSource, final String metaDataMethodName) throws MetaDataAccessException {
        return extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() { // from class: org.springframework.jdbc.support.JdbcUtils.1
            @Override // org.springframework.jdbc.support.DatabaseMetaDataCallback
            public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
                try {
                    return DatabaseMetaData.class.getMethod(metaDataMethodName, new Class[0]).invoke(dbmd, new Object[0]);
                } catch (IllegalAccessException ex) {
                    throw new MetaDataAccessException("Could not access DatabaseMetaData method '" + metaDataMethodName + "'", ex);
                } catch (NoSuchMethodException ex2) {
                    throw new MetaDataAccessException("No method named '" + metaDataMethodName + "' found on DatabaseMetaData instance [" + dbmd + "]", ex2);
                } catch (InvocationTargetException ex3) {
                    if (ex3.getTargetException() instanceof SQLException) {
                        throw ((SQLException) ex3.getTargetException());
                    }
                    throw new MetaDataAccessException("Invocation of DatabaseMetaData method '" + metaDataMethodName + "' failed", ex3);
                }
            }
        });
    }

    public static boolean supportsBatchUpdates(Connection con) throws SQLException {
        try {
            DatabaseMetaData dbmd = con.getMetaData();
            if (dbmd != null) {
                if (dbmd.supportsBatchUpdates()) {
                    logger.debug("JDBC driver supports batch updates");
                    return true;
                }
                logger.debug("JDBC driver does not support batch updates");
            }
            return false;
        } catch (SQLException ex) {
            logger.debug("JDBC driver 'supportsBatchUpdates' method threw exception", ex);
            return false;
        }
    }

    public static String commonDatabaseName(String source) {
        String name = source;
        if (source != null && source.startsWith("DB2")) {
            name = "DB2";
        } else if ("Sybase SQL Server".equals(source) || "Adaptive Server Enterprise".equals(source) || "ASE".equals(source) || "sql server".equalsIgnoreCase(source)) {
            name = "Sybase";
        }
        return name;
    }

    public static boolean isNumeric(int sqlType) {
        return -7 == sqlType || -5 == sqlType || 3 == sqlType || 8 == sqlType || 6 == sqlType || 4 == sqlType || 2 == sqlType || 7 == sqlType || 5 == sqlType || -6 == sqlType;
    }

    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (!StringUtils.hasLength(name)) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

    public static String convertUnderscoreNameToPropertyName(String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && name.length() > 0) {
            if (name.length() > 1 && name.charAt(1) == '_') {
                result.append(Character.toUpperCase(name.charAt(0)));
            } else {
                result.append(Character.toLowerCase(name.charAt(0)));
            }
            for (int i = 1; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == '_') {
                    nextIsUpper = true;
                } else if (nextIsUpper) {
                    result.append(Character.toUpperCase(c));
                    nextIsUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }
}
