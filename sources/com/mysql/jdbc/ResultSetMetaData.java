package com.mysql.jdbc;

import ch.qos.logback.core.joran.action.ActionConst;
import com.fasterxml.jackson.core.JsonFactory;
import java.sql.SQLException;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.poifs.common.POIFSConstants;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ResultSetMetaData.class */
public class ResultSetMetaData implements java.sql.ResultSetMetaData {
    Field[] fields;
    boolean useOldAliasBehavior;
    boolean treatYearAsDate;
    private ExceptionInterceptor exceptionInterceptor;

    private static int clampedGetLength(Field f) {
        long fieldLength = f.getLength();
        if (fieldLength > 2147483647L) {
            fieldLength = 2147483647L;
        }
        return (int) fieldLength;
    }

    private static final boolean isDecimalType(int type) {
        switch (type) {
            case -7:
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            default:
                return false;
        }
    }

    public ResultSetMetaData(Field[] fields, boolean useOldAliasBehavior, boolean treatYearAsDate, ExceptionInterceptor exceptionInterceptor) {
        this.useOldAliasBehavior = false;
        this.treatYearAsDate = true;
        this.fields = fields;
        this.useOldAliasBehavior = useOldAliasBehavior;
        this.treatYearAsDate = treatYearAsDate;
        this.exceptionInterceptor = exceptionInterceptor;
    }

    @Override // java.sql.ResultSetMetaData
    public String getCatalogName(int column) throws SQLException {
        Field f = getField(column);
        String database = f.getDatabaseName();
        return database == null ? "" : database;
    }

    public String getColumnCharacterEncoding(int column) throws SQLException {
        String mysqlName = getColumnCharacterSet(column);
        String javaName = null;
        if (mysqlName != null) {
            try {
                javaName = CharsetMapping.getJavaEncodingForMysqlCharset(mysqlName);
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
        return javaName;
    }

    public String getColumnCharacterSet(int column) throws SQLException {
        return getField(column).getEncoding();
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnClassName(int column) throws SQLException {
        Field f = getField(column);
        return getClassNameForJavaType(f.getSQLType(), f.isUnsigned(), f.getMysqlType(), f.isBinary() || f.isBlob(), f.isOpaqueBinary(), this.treatYearAsDate);
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnCount() throws SQLException {
        return this.fields.length;
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnDisplaySize(int column) throws SQLException {
        Field f = getField(column);
        int lengthInBytes = clampedGetLength(f);
        return lengthInBytes / f.getMaxBytesPerCharacter();
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnLabel(int column) throws SQLException {
        if (this.useOldAliasBehavior) {
            return getColumnName(column);
        }
        return getField(column).getColumnLabel();
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnName(int column) throws SQLException {
        if (this.useOldAliasBehavior) {
            return getField(column).getName();
        }
        String name = getField(column).getNameNoAliases();
        if (name != null && name.length() == 0) {
            return getField(column).getName();
        }
        return name;
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnType(int column) throws SQLException {
        return getField(column).getSQLType();
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnTypeName(int column) throws SQLException {
        Field field = getField(column);
        int mysqlType = field.getMysqlType();
        int jdbcType = field.getSQLType();
        switch (mysqlType) {
            case 0:
            case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                return field.isUnsigned() ? "DECIMAL UNSIGNED" : "DECIMAL";
            case 1:
                return field.isUnsigned() ? "TINYINT UNSIGNED" : "TINYINT";
            case 2:
                return field.isUnsigned() ? "SMALLINT UNSIGNED" : "SMALLINT";
            case 3:
                return field.isUnsigned() ? "INT UNSIGNED" : "INT";
            case 4:
                return field.isUnsigned() ? "FLOAT UNSIGNED" : "FLOAT";
            case 5:
                return field.isUnsigned() ? "DOUBLE UNSIGNED" : "DOUBLE";
            case 6:
                return ActionConst.NULL;
            case 7:
                return "TIMESTAMP";
            case 8:
                return field.isUnsigned() ? "BIGINT UNSIGNED" : "BIGINT";
            case 9:
                return field.isUnsigned() ? "MEDIUMINT UNSIGNED" : "MEDIUMINT";
            case 10:
                return "DATE";
            case 11:
                return "TIME";
            case 12:
                return "DATETIME";
            case 13:
                return "YEAR";
            case 15:
                return "VARCHAR";
            case 16:
                return "BIT";
            case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
                return JsonFactory.FORMAT_NAME_JSON;
            case 247:
                return "ENUM";
            case EscherProperties.GEOTEXT__STRETCHCHARHEIGHT /* 248 */:
                return "SET";
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
                return "TINYBLOB";
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
                return "MEDIUMBLOB";
            case 251:
                return "LONGBLOB";
            case 252:
                if (getField(column).isBinary()) {
                    return "BLOB";
                }
                return "TEXT";
            case 253:
                if (jdbcType == -3) {
                    return "VARBINARY";
                }
                return "VARCHAR";
            case 254:
                if (jdbcType == -2) {
                    return "BINARY";
                }
                return "CHAR";
            case 255:
                return "GEOMETRY";
            default:
                return "UNKNOWN";
        }
    }

    protected Field getField(int columnIndex) throws SQLException {
        if (columnIndex < 1 || columnIndex > this.fields.length) {
            throw SQLError.createSQLException(Messages.getString("ResultSetMetaData.46"), SQLError.SQL_STATE_INVALID_COLUMN_NUMBER, this.exceptionInterceptor);
        }
        return this.fields[columnIndex - 1];
    }

    @Override // java.sql.ResultSetMetaData
    public int getPrecision(int column) throws SQLException {
        Field f = getField(column);
        if (isDecimalType(f.getSQLType())) {
            if (f.getDecimals() > 0) {
                return (clampedGetLength(f) - 1) + f.getPrecisionAdjustFactor();
            }
            return clampedGetLength(f) + f.getPrecisionAdjustFactor();
        }
        switch (f.getMysqlType()) {
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
                return clampedGetLength(f);
            default:
                return clampedGetLength(f) / f.getMaxBytesPerCharacter();
        }
    }

    @Override // java.sql.ResultSetMetaData
    public int getScale(int column) throws SQLException {
        Field f = getField(column);
        if (isDecimalType(f.getSQLType())) {
            return f.getDecimals();
        }
        return 0;
    }

    @Override // java.sql.ResultSetMetaData
    public String getSchemaName(int column) throws SQLException {
        return "";
    }

    @Override // java.sql.ResultSetMetaData
    public String getTableName(int column) throws SQLException {
        String res = this.useOldAliasBehavior ? getField(column).getTableName() : getField(column).getTableNameNoAliases();
        return res == null ? "" : res;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isAutoIncrement(int column) throws SQLException {
        Field f = getField(column);
        return f.isAutoIncrement();
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isCaseSensitive(int column) throws SQLException {
        Field field = getField(column);
        int sqlType = field.getSQLType();
        switch (sqlType) {
            case -7:
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 91:
            case 92:
            case 93:
                return false;
            case -1:
            case 1:
            case 12:
                if (field.isBinary()) {
                    return true;
                }
                String collationName = field.getCollation();
                return (collationName == null || collationName.endsWith("_ci")) ? false : true;
            default:
                return true;
        }
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return isWritable(column);
    }

    @Override // java.sql.ResultSetMetaData
    public int isNullable(int column) throws SQLException {
        if (!getField(column).isNotNull()) {
            return 1;
        }
        return 0;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isReadOnly(int column) throws SQLException {
        return getField(column).isReadOnly();
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isSearchable(int column) throws SQLException {
        return true;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isSigned(int column) throws SQLException {
        Field f = getField(column);
        int sqlType = f.getSQLType();
        switch (sqlType) {
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                if (!f.isUnsigned()) {
                }
                break;
            case 91:
            case 92:
            case 93:
                break;
        }
        return false;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isWritable(int column) throws SQLException {
        return !isReadOnly(column);
    }

    public String toString() {
        StringBuilder toStringBuf = new StringBuilder();
        toStringBuf.append(super.toString());
        toStringBuf.append(" - Field level information: ");
        for (int i = 0; i < this.fields.length; i++) {
            toStringBuf.append("\n\t");
            toStringBuf.append(this.fields[i].toString());
        }
        return toStringBuf.toString();
    }

    static String getClassNameForJavaType(int javaType, boolean isUnsigned, int mysqlTypeIfKnown, boolean isBinaryOrBlob, boolean isOpaqueBinary, boolean treatYearAsDate) {
        switch (javaType) {
            case -7:
            case 16:
                return "java.lang.Boolean";
            case -6:
                if (isUnsigned) {
                    return "java.lang.Integer";
                }
                return "java.lang.Integer";
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                if (!isUnsigned) {
                    return "java.lang.Long";
                }
                return "java.math.BigInteger";
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
                if (mysqlTypeIfKnown == 255 || isBinaryOrBlob) {
                    return "[B";
                }
                return "java.lang.String";
            case -1:
            case 1:
            case 12:
                if (!isOpaqueBinary) {
                    return "java.lang.String";
                }
                return "[B";
            case 2:
            case 3:
                return "java.math.BigDecimal";
            case 4:
                if (!isUnsigned || mysqlTypeIfKnown == 9) {
                    return "java.lang.Integer";
                }
                return "java.lang.Long";
            case 5:
                if (isUnsigned) {
                    return "java.lang.Integer";
                }
                return "java.lang.Integer";
            case 6:
            case 8:
                return "java.lang.Double";
            case 7:
                return "java.lang.Float";
            case 91:
                return (treatYearAsDate || mysqlTypeIfKnown != 13) ? "java.sql.Date" : "java.lang.Short";
            case 92:
                return "java.sql.Time";
            case 93:
                return "java.sql.Timestamp";
            default:
                return "java.lang.Object";
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
