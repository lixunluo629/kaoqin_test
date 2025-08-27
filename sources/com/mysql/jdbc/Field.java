package com.mysql.jdbc;

import com.itextpdf.io.font.PdfEncodings;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.poifs.common.POIFSConstants;
import org.springframework.beans.PropertyAccessor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Field.class */
public class Field {
    private static final int AUTO_INCREMENT_FLAG = 512;
    private static final int NO_CHARSET_INFO = -1;
    private byte[] buffer;
    private int collationIndex;
    private String encoding;
    private int colDecimals;
    private short colFlag;
    private String collationName;
    private MySQLConnection connection;
    private String databaseName;
    private int databaseNameLength;
    private int databaseNameStart;
    protected int defaultValueLength;
    protected int defaultValueStart;
    private String fullName;
    private String fullOriginalName;
    private boolean isImplicitTempTable;
    private long length;
    private int mysqlType;
    private String name;
    private int nameLength;
    private int nameStart;
    private String originalColumnName;
    private int originalColumnNameLength;
    private int originalColumnNameStart;
    private String originalTableName;
    private int originalTableNameLength;
    private int originalTableNameStart;
    private int precisionAdjustFactor;
    private int sqlType;
    private String tableName;
    private int tableNameLength;
    private int tableNameStart;
    private boolean useOldNameMetadata;
    private boolean isSingleBit;
    private int maxBytesPerChar;
    private final boolean valueNeedsQuoting;

    Field(MySQLConnection conn, byte[] buffer, int databaseNameStart, int databaseNameLength, int tableNameStart, int tableNameLength, int originalTableNameStart, int originalTableNameLength, int nameStart, int nameLength, int originalColumnNameStart, int originalColumnNameLength, long length, int mysqlType, short colFlag, int colDecimals, int defaultValueStart, int defaultValueLength, int charsetIndex) throws SQLException {
        this.collationIndex = 0;
        this.encoding = null;
        this.collationName = null;
        this.connection = null;
        this.databaseName = null;
        this.databaseNameLength = -1;
        this.databaseNameStart = -1;
        this.defaultValueLength = -1;
        this.defaultValueStart = -1;
        this.fullName = null;
        this.fullOriginalName = null;
        this.isImplicitTempTable = false;
        this.mysqlType = -1;
        this.originalColumnName = null;
        this.originalColumnNameLength = -1;
        this.originalColumnNameStart = -1;
        this.originalTableName = null;
        this.originalTableNameLength = -1;
        this.originalTableNameStart = -1;
        this.precisionAdjustFactor = 0;
        this.sqlType = -1;
        this.useOldNameMetadata = false;
        this.connection = conn;
        this.buffer = buffer;
        this.nameStart = nameStart;
        this.nameLength = nameLength;
        this.tableNameStart = tableNameStart;
        this.tableNameLength = tableNameLength;
        this.length = length;
        this.colFlag = colFlag;
        this.colDecimals = colDecimals;
        this.mysqlType = mysqlType;
        this.databaseNameStart = databaseNameStart;
        this.databaseNameLength = databaseNameLength;
        this.originalTableNameStart = originalTableNameStart;
        this.originalTableNameLength = originalTableNameLength;
        this.originalColumnNameStart = originalColumnNameStart;
        this.originalColumnNameLength = originalColumnNameLength;
        this.defaultValueStart = defaultValueStart;
        this.defaultValueLength = defaultValueLength;
        this.collationIndex = charsetIndex;
        this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
        checkForImplicitTemporaryTable();
        boolean isFromFunction = this.originalTableNameLength == 0;
        if (this.mysqlType == 252) {
            if (this.connection.getBlobsAreStrings() || (this.connection.getFunctionsNeverReturnBlobs() && isFromFunction)) {
                this.sqlType = 12;
                this.mysqlType = 15;
            } else if (this.collationIndex == 63 || !this.connection.versionMeetsMinimum(4, 1, 0)) {
                if (this.connection.getUseBlobToStoreUTF8OutsideBMP() && shouldSetupForUtf8StringInBlob()) {
                    setupForUtf8StringInBlob();
                } else {
                    setBlobTypeBasedOnLength();
                    this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
                }
            } else {
                this.mysqlType = 253;
                this.sqlType = -1;
            }
        }
        if (this.sqlType == -6 && this.length == 1 && this.connection.getTinyInt1isBit() && conn.getTinyInt1isBit()) {
            if (conn.getTransformedBitIsBoolean()) {
                this.sqlType = 16;
            } else {
                this.sqlType = -7;
            }
        }
        if (!isNativeNumericType() && !isNativeDateTimeType()) {
            this.encoding = this.connection.getEncodingForIndex(this.collationIndex);
            if (PdfEncodings.UNICODE_BIG.equals(this.encoding)) {
                this.encoding = "UTF-16";
            }
            if (this.mysqlType == 245) {
                this.encoding = "UTF-8";
            }
            boolean isBinary = isBinary();
            if (this.connection.versionMeetsMinimum(4, 1, 0) && this.mysqlType == 253 && isBinary && this.collationIndex == 63) {
                if (this.connection.getFunctionsNeverReturnBlobs() && isFromFunction) {
                    this.sqlType = 12;
                    this.mysqlType = 15;
                } else if (isOpaqueBinary()) {
                    this.sqlType = -3;
                }
            }
            if (this.connection.versionMeetsMinimum(4, 1, 0) && this.mysqlType == 254 && isBinary && this.collationIndex == 63 && isOpaqueBinary() && !this.connection.getBlobsAreStrings()) {
                this.sqlType = -2;
            }
            if (this.mysqlType == 16) {
                this.isSingleBit = this.length == 0 || (this.length == 1 && (this.connection.versionMeetsMinimum(5, 0, 21) || this.connection.versionMeetsMinimum(5, 1, 10)));
                if (!this.isSingleBit) {
                    this.colFlag = (short) (this.colFlag | 128);
                    this.colFlag = (short) (this.colFlag | 16);
                    isBinary = true;
                }
            }
            if (this.sqlType == -4 && !isBinary) {
                this.sqlType = -1;
            } else if (this.sqlType == -3 && !isBinary) {
                this.sqlType = 12;
            }
        } else {
            this.encoding = "US-ASCII";
        }
        if (!isUnsigned()) {
            switch (this.mysqlType) {
                case 0:
                case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                    this.precisionAdjustFactor = -1;
                    break;
                case 4:
                case 5:
                    this.precisionAdjustFactor = 1;
                    break;
            }
        } else {
            switch (this.mysqlType) {
                case 4:
                case 5:
                    this.precisionAdjustFactor = 1;
                    break;
            }
        }
        this.valueNeedsQuoting = determineNeedsQuoting();
    }

    private boolean shouldSetupForUtf8StringInBlob() throws SQLException {
        String includePattern = this.connection.getUtf8OutsideBmpIncludedColumnNamePattern();
        String excludePattern = this.connection.getUtf8OutsideBmpExcludedColumnNamePattern();
        if (excludePattern != null && !StringUtils.isEmptyOrWhitespaceOnly(excludePattern)) {
            try {
                if (getOriginalName().matches(excludePattern)) {
                    if (includePattern != null && !StringUtils.isEmptyOrWhitespaceOnly(includePattern)) {
                        try {
                            if (getOriginalName().matches(includePattern)) {
                                return true;
                            }
                            return false;
                        } catch (PatternSyntaxException pse) {
                            SQLException sqlEx = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpIncludedColumnNamePattern\"", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.connection.getExceptionInterceptor());
                            if (!this.connection.getParanoid()) {
                                sqlEx.initCause(pse);
                            }
                            throw sqlEx;
                        }
                    }
                    return false;
                }
                return true;
            } catch (PatternSyntaxException pse2) {
                SQLException sqlEx2 = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpExcludedColumnNamePattern\"", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.connection.getExceptionInterceptor());
                if (!this.connection.getParanoid()) {
                    sqlEx2.initCause(pse2);
                }
                throw sqlEx2;
            }
        }
        return true;
    }

    private void setupForUtf8StringInBlob() {
        if (this.length == 255 || this.length == 65535) {
            this.mysqlType = 15;
            this.sqlType = 12;
        } else {
            this.mysqlType = 253;
            this.sqlType = -1;
        }
        this.collationIndex = 33;
    }

    Field(MySQLConnection conn, byte[] buffer, int nameStart, int nameLength, int tableNameStart, int tableNameLength, int length, int mysqlType, short colFlag, int colDecimals) throws SQLException {
        this(conn, buffer, -1, -1, tableNameStart, tableNameLength, -1, -1, nameStart, nameLength, -1, -1, length, mysqlType, colFlag, colDecimals, -1, -1, -1);
    }

    Field(String tableName, String columnName, int jdbcType, int length) {
        this.collationIndex = 0;
        this.encoding = null;
        this.collationName = null;
        this.connection = null;
        this.databaseName = null;
        this.databaseNameLength = -1;
        this.databaseNameStart = -1;
        this.defaultValueLength = -1;
        this.defaultValueStart = -1;
        this.fullName = null;
        this.fullOriginalName = null;
        this.isImplicitTempTable = false;
        this.mysqlType = -1;
        this.originalColumnName = null;
        this.originalColumnNameLength = -1;
        this.originalColumnNameStart = -1;
        this.originalTableName = null;
        this.originalTableNameLength = -1;
        this.originalTableNameStart = -1;
        this.precisionAdjustFactor = 0;
        this.sqlType = -1;
        this.useOldNameMetadata = false;
        this.tableName = tableName;
        this.name = columnName;
        this.length = length;
        this.sqlType = jdbcType;
        this.colFlag = (short) 0;
        this.colDecimals = 0;
        this.valueNeedsQuoting = determineNeedsQuoting();
    }

    Field(String tableName, String columnName, int charsetIndex, int jdbcType, int length) {
        this.collationIndex = 0;
        this.encoding = null;
        this.collationName = null;
        this.connection = null;
        this.databaseName = null;
        this.databaseNameLength = -1;
        this.databaseNameStart = -1;
        this.defaultValueLength = -1;
        this.defaultValueStart = -1;
        this.fullName = null;
        this.fullOriginalName = null;
        this.isImplicitTempTable = false;
        this.mysqlType = -1;
        this.originalColumnName = null;
        this.originalColumnNameLength = -1;
        this.originalColumnNameStart = -1;
        this.originalTableName = null;
        this.originalTableNameLength = -1;
        this.originalTableNameStart = -1;
        this.precisionAdjustFactor = 0;
        this.sqlType = -1;
        this.useOldNameMetadata = false;
        this.tableName = tableName;
        this.name = columnName;
        this.length = length;
        this.sqlType = jdbcType;
        this.colFlag = (short) 0;
        this.colDecimals = 0;
        this.collationIndex = charsetIndex;
        this.valueNeedsQuoting = determineNeedsQuoting();
        switch (this.sqlType) {
            case -3:
            case -2:
                this.colFlag = (short) (this.colFlag | 128);
                this.colFlag = (short) (this.colFlag | 16);
                break;
        }
    }

    private void checkForImplicitTemporaryTable() {
        this.isImplicitTempTable = this.tableNameLength > 5 && this.buffer[this.tableNameStart] == 35 && this.buffer[this.tableNameStart + 1] == 115 && this.buffer[this.tableNameStart + 2] == 113 && this.buffer[this.tableNameStart + 3] == 108 && this.buffer[this.tableNameStart + 4] == 95;
    }

    public String getEncoding() throws SQLException {
        return this.encoding;
    }

    public void setEncoding(String javaEncodingName, Connection conn) throws SQLException {
        this.encoding = javaEncodingName;
        try {
            this.collationIndex = CharsetMapping.getCollationIndexForJavaEncoding(javaEncodingName, conn);
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    public int getCollationIndex() throws SQLException {
        return this.collationIndex;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0107, code lost:
    
        r5.collationName = r13.getString("Collation");
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0124, code lost:
    
        throw r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x012c, code lost:
    
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0138, code lost:
    
        if (r12 == null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x013b, code lost:
    
        r12.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x013b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.lang.String getCollation() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 370
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.Field.getCollation():java.lang.String");
    }

    public String getColumnLabel() throws SQLException {
        return getName();
    }

    public String getDatabaseName() throws SQLException {
        if (this.databaseName == null && this.databaseNameStart != -1 && this.databaseNameLength != -1) {
            this.databaseName = getStringFromBytes(this.databaseNameStart, this.databaseNameLength);
        }
        return this.databaseName;
    }

    int getDecimals() {
        return this.colDecimals;
    }

    public String getFullName() throws SQLException {
        if (this.fullName == null) {
            StringBuilder fullNameBuf = new StringBuilder(getTableName().length() + 1 + getName().length());
            fullNameBuf.append(this.tableName);
            fullNameBuf.append('.');
            fullNameBuf.append(this.name);
            this.fullName = fullNameBuf.toString();
        }
        return this.fullName;
    }

    public String getFullOriginalName() throws SQLException {
        getOriginalName();
        if (this.originalColumnName == null) {
            return null;
        }
        if (this.fullName == null) {
            StringBuilder fullOriginalNameBuf = new StringBuilder(getOriginalTableName().length() + 1 + getOriginalName().length());
            fullOriginalNameBuf.append(this.originalTableName);
            fullOriginalNameBuf.append('.');
            fullOriginalNameBuf.append(this.originalColumnName);
            this.fullOriginalName = fullOriginalNameBuf.toString();
        }
        return this.fullOriginalName;
    }

    public long getLength() {
        return this.length;
    }

    public synchronized int getMaxBytesPerCharacter() throws SQLException {
        if (this.maxBytesPerChar == 0) {
            this.maxBytesPerChar = this.connection.getMaxBytesPerChar(Integer.valueOf(this.collationIndex), getEncoding());
        }
        return this.maxBytesPerChar;
    }

    public int getMysqlType() {
        return this.mysqlType;
    }

    public String getName() throws SQLException {
        if (this.name == null) {
            this.name = getStringFromBytes(this.nameStart, this.nameLength);
        }
        return this.name;
    }

    public String getNameNoAliases() throws SQLException {
        if (this.useOldNameMetadata) {
            return getName();
        }
        if (this.connection != null && this.connection.versionMeetsMinimum(4, 1, 0)) {
            return getOriginalName();
        }
        return getName();
    }

    public String getOriginalName() throws SQLException {
        if (this.originalColumnName == null && this.originalColumnNameStart != -1 && this.originalColumnNameLength != -1) {
            this.originalColumnName = getStringFromBytes(this.originalColumnNameStart, this.originalColumnNameLength);
        }
        return this.originalColumnName;
    }

    public String getOriginalTableName() throws SQLException {
        if (this.originalTableName == null && this.originalTableNameStart != -1 && this.originalTableNameLength != -1) {
            this.originalTableName = getStringFromBytes(this.originalTableNameStart, this.originalTableNameLength);
        }
        return this.originalTableName;
    }

    public int getPrecisionAdjustFactor() {
        return this.precisionAdjustFactor;
    }

    public int getSQLType() {
        return this.sqlType;
    }

    private String getStringFromBytes(int stringStart, int stringLength) throws SQLException {
        String stringVal;
        if (stringStart == -1 || stringLength == -1) {
            return null;
        }
        if (stringLength == 0) {
            return "";
        }
        if (this.connection != null && this.connection.getUseUnicode()) {
            String javaEncoding = this.connection.getCharacterSetMetadata();
            if (javaEncoding == null) {
                javaEncoding = this.connection.getEncoding();
            }
            if (javaEncoding != null) {
                SingleByteCharsetConverter converter = null;
                if (this.connection != null) {
                    converter = this.connection.getCharsetConverter(javaEncoding);
                }
                if (converter != null) {
                    stringVal = converter.toString(this.buffer, stringStart, stringLength);
                } else {
                    try {
                        stringVal = StringUtils.toString(this.buffer, stringStart, stringLength, javaEncoding);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(Messages.getString("Field.12") + javaEncoding + Messages.getString("Field.13"));
                    }
                }
            } else {
                stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
            }
        } else {
            stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
        }
        return stringVal;
    }

    public String getTable() throws SQLException {
        return getTableName();
    }

    public String getTableName() throws SQLException {
        if (this.tableName == null) {
            this.tableName = getStringFromBytes(this.tableNameStart, this.tableNameLength);
        }
        return this.tableName;
    }

    public String getTableNameNoAliases() throws SQLException {
        if (this.connection.versionMeetsMinimum(4, 1, 0)) {
            return getOriginalTableName();
        }
        return getTableName();
    }

    public boolean isAutoIncrement() {
        return (this.colFlag & 512) > 0;
    }

    public boolean isBinary() {
        return (this.colFlag & 128) > 0;
    }

    public boolean isBlob() {
        return (this.colFlag & 16) > 0;
    }

    private boolean isImplicitTemporaryTable() {
        return this.isImplicitTempTable;
    }

    public boolean isMultipleKey() {
        return (this.colFlag & 8) > 0;
    }

    boolean isNotNull() {
        return (this.colFlag & 1) > 0;
    }

    boolean isOpaqueBinary() throws SQLException {
        return (this.collationIndex == 63 && isBinary() && (getMysqlType() == 254 || getMysqlType() == 253)) ? (this.originalTableNameLength != 0 || this.connection == null || this.connection.versionMeetsMinimum(5, 0, 25)) && !isImplicitTemporaryTable() : this.connection.versionMeetsMinimum(4, 1, 0) && "binary".equalsIgnoreCase(getEncoding());
    }

    public boolean isPrimaryKey() {
        return (this.colFlag & 2) > 0;
    }

    boolean isReadOnly() throws SQLException {
        if (this.connection.versionMeetsMinimum(4, 1, 0)) {
            String orgColumnName = getOriginalName();
            String orgTableName = getOriginalTableName();
            return orgColumnName == null || orgColumnName.length() <= 0 || orgTableName == null || orgTableName.length() <= 0;
        }
        return false;
    }

    public boolean isUniqueKey() {
        return (this.colFlag & 4) > 0;
    }

    public boolean isUnsigned() {
        return (this.colFlag & 32) > 0;
    }

    public void setUnsigned() {
        this.colFlag = (short) (this.colFlag | 32);
    }

    public boolean isZeroFill() {
        return (this.colFlag & 64) > 0;
    }

    private void setBlobTypeBasedOnLength() {
        if (this.length == 255) {
            this.mysqlType = EscherProperties.GEOTEXT__NOMEASUREALONGPATH;
            return;
        }
        if (this.length == 65535) {
            this.mysqlType = 252;
        } else if (this.length == 16777215) {
            this.mysqlType = EscherProperties.GEOTEXT__BOLDFONT;
        } else if (this.length == 4294967295L) {
            this.mysqlType = 251;
        }
    }

    private boolean isNativeNumericType() {
        return (this.mysqlType >= 1 && this.mysqlType <= 5) || this.mysqlType == 8 || this.mysqlType == 13;
    }

    private boolean isNativeDateTimeType() {
        return this.mysqlType == 10 || this.mysqlType == 14 || this.mysqlType == 12 || this.mysqlType == 11 || this.mysqlType == 7;
    }

    public boolean isCharsetApplicableType() {
        return this.mysqlType == 247 || this.mysqlType == 245 || this.mysqlType == 248 || this.mysqlType == 254 || this.mysqlType == 253 || this.mysqlType == 15;
    }

    public void setConnection(MySQLConnection conn) {
        this.connection = conn;
        if (this.encoding == null || this.collationIndex == 0) {
            this.encoding = this.connection.getEncoding();
        }
    }

    void setMysqlType(int type) {
        this.mysqlType = type;
        this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
    }

    protected void setUseOldNameMetadata(boolean useOldNameMetadata) {
        this.useOldNameMetadata = useOldNameMetadata;
    }

    public String toString() {
        try {
            StringBuilder asString = new StringBuilder();
            asString.append(super.toString());
            asString.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
            asString.append("catalog=");
            asString.append(getDatabaseName());
            asString.append(",tableName=");
            asString.append(getTableName());
            asString.append(",originalTableName=");
            asString.append(getOriginalTableName());
            asString.append(",columnName=");
            asString.append(getName());
            asString.append(",originalColumnName=");
            asString.append(getOriginalName());
            asString.append(",mysqlType=");
            asString.append(getMysqlType());
            asString.append("(");
            asString.append(MysqlDefs.typeToName(getMysqlType()));
            asString.append(")");
            asString.append(",flags=");
            if (isAutoIncrement()) {
                asString.append(" AUTO_INCREMENT");
            }
            if (isPrimaryKey()) {
                asString.append(" PRIMARY_KEY");
            }
            if (isUniqueKey()) {
                asString.append(" UNIQUE_KEY");
            }
            if (isBinary()) {
                asString.append(" BINARY");
            }
            if (isBlob()) {
                asString.append(" BLOB");
            }
            if (isMultipleKey()) {
                asString.append(" MULTI_KEY");
            }
            if (isUnsigned()) {
                asString.append(" UNSIGNED");
            }
            if (isZeroFill()) {
                asString.append(" ZEROFILL");
            }
            asString.append(", charsetIndex=");
            asString.append(this.collationIndex);
            asString.append(", charsetName=");
            asString.append(this.encoding);
            asString.append("]");
            return asString.toString();
        } catch (Throwable th) {
            return super.toString();
        }
    }

    protected boolean isSingleBit() {
        return this.isSingleBit;
    }

    protected boolean getvalueNeedsQuoting() {
        return this.valueNeedsQuoting;
    }

    private boolean determineNeedsQuoting() {
        boolean retVal;
        switch (this.sqlType) {
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
                retVal = false;
                break;
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            default:
                retVal = true;
                break;
        }
        return retVal;
    }
}
