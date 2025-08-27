package com.mysql.jdbc;

import ch.qos.logback.core.joran.action.ActionConst;
import ch.qos.logback.core.net.ssl.SSL;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.commons.httpclient.ConnectMethod;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.tomcat.util.buf.AbstractChunk;
import org.apache.xmlbeans.XmlErrorCodes;
import org.bouncycastle.i18n.TextBundle;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData.class */
public class DatabaseMetaData implements java.sql.DatabaseMetaData {
    protected static final int MAX_IDENTIFIER_LENGTH = 64;
    private static final int DEFERRABILITY = 13;
    private static final int DELETE_RULE = 10;
    private static final int FK_NAME = 11;
    private static final int FKCOLUMN_NAME = 7;
    private static final int FKTABLE_CAT = 4;
    private static final int FKTABLE_NAME = 6;
    private static final int FKTABLE_SCHEM = 5;
    private static final int KEY_SEQ = 8;
    private static final int PK_NAME = 12;
    private static final int PKCOLUMN_NAME = 3;
    private static final int PKTABLE_CAT = 0;
    private static final int PKTABLE_NAME = 2;
    private static final int PKTABLE_SCHEM = 1;
    private static final String SUPPORTS_FK = "SUPPORTS_FK";
    private static final int UPDATE_RULE = 9;
    private static final Constructor<?> JDBC_4_DBMD_SHOW_CTOR;
    private static final Constructor<?> JDBC_4_DBMD_IS_CTOR;
    private static final String[] MYSQL_KEYWORDS;
    private static final String[] SQL92_KEYWORDS;
    private static final String[] SQL2003_KEYWORDS;
    private static volatile String mysqlKeywords;
    protected MySQLConnection conn;
    protected String database;
    protected final String quotedId;
    private ExceptionInterceptor exceptionInterceptor;
    protected static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
    protected static final byte[] SYSTEM_TABLE_AS_BYTES = "SYSTEM TABLE".getBytes();
    protected static final byte[] VIEW_AS_BYTES = "VIEW".getBytes();

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$ProcedureType.class */
    protected enum ProcedureType {
        PROCEDURE,
        FUNCTION
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$IteratorWithCleanup.class */
    protected abstract class IteratorWithCleanup<T> {
        abstract void close() throws SQLException;

        abstract boolean hasNext() throws SQLException;

        abstract T next() throws SQLException;

        protected IteratorWithCleanup() {
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$LocalAndReferencedColumns.class */
    class LocalAndReferencedColumns {
        String constraintName;
        List<String> localColumnsList;
        String referencedCatalog;
        List<String> referencedColumnsList;
        String referencedTable;

        LocalAndReferencedColumns(List<String> localColumns, List<String> refColumns, String constName, String refCatalog, String refTable) {
            this.localColumnsList = localColumns;
            this.referencedColumnsList = refColumns;
            this.constraintName = constName;
            this.referencedTable = refTable;
            this.referencedCatalog = refCatalog;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$ResultSetIterator.class */
    protected class ResultSetIterator extends IteratorWithCleanup<String> {
        int colIndex;
        ResultSet resultSet;

        ResultSetIterator(ResultSet rs, int index) {
            super();
            this.resultSet = rs;
            this.colIndex = index;
        }

        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        void close() throws SQLException {
            this.resultSet.close();
        }

        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        boolean hasNext() throws SQLException {
            return this.resultSet.next();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        public String next() throws SQLException {
            return this.resultSet.getObject(this.colIndex).toString();
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$SingleStringIterator.class */
    protected class SingleStringIterator extends IteratorWithCleanup<String> {
        boolean onFirst;
        String value;

        SingleStringIterator(String s) {
            super();
            this.onFirst = true;
            this.value = s;
        }

        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        void close() throws SQLException {
        }

        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        boolean hasNext() throws SQLException {
            return this.onFirst;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.mysql.jdbc.DatabaseMetaData.IteratorWithCleanup
        public String next() throws SQLException {
            this.onFirst = false;
            return this.value;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$TypeDescriptor.class */
    class TypeDescriptor {
        int bufferLength;
        int charOctetLength;
        Integer columnSize;
        short dataType;
        Integer decimalDigits;
        String isNullable;
        int nullability;
        int numPrecRadix;
        String typeName;

        TypeDescriptor(String typeInfo, String nullabilityInfo) throws SQLException {
            String mysqlType;
            String fullMysqlType;
            int maxLength;
            this.numPrecRadix = 10;
            if (typeInfo == null) {
                throw SQLError.createSQLException("NULL typeinfo not supported.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, DatabaseMetaData.this.getExceptionInterceptor());
            }
            if (typeInfo.indexOf("(") != -1) {
                mysqlType = typeInfo.substring(0, typeInfo.indexOf("(")).trim();
            } else {
                mysqlType = typeInfo;
            }
            int indexOfUnsignedInMysqlType = StringUtils.indexOfIgnoreCase(mysqlType, "unsigned");
            mysqlType = indexOfUnsignedInMysqlType != -1 ? mysqlType.substring(0, indexOfUnsignedInMysqlType - 1) : mysqlType;
            boolean isUnsigned = false;
            if (StringUtils.indexOfIgnoreCase(typeInfo, "unsigned") != -1 && StringUtils.indexOfIgnoreCase(typeInfo, "set") != 0 && StringUtils.indexOfIgnoreCase(typeInfo, "enum") != 0) {
                fullMysqlType = mysqlType + " unsigned";
                isUnsigned = true;
            } else {
                fullMysqlType = mysqlType;
            }
            fullMysqlType = DatabaseMetaData.this.conn.getCapitalizeTypeNames() ? fullMysqlType.toUpperCase(Locale.ENGLISH) : fullMysqlType;
            this.dataType = (short) MysqlDefs.mysqlToJavaType(mysqlType);
            this.typeName = fullMysqlType;
            if (StringUtils.startsWithIgnoreCase(typeInfo, "enum")) {
                String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
                StringTokenizer tokenizer = new StringTokenizer(temp, ",");
                int iMax = 0;
                while (true) {
                    maxLength = iMax;
                    if (!tokenizer.hasMoreTokens()) {
                        break;
                    } else {
                        iMax = Math.max(maxLength, tokenizer.nextToken().length() - 2);
                    }
                }
                this.columnSize = Integer.valueOf(maxLength);
                this.decimalDigits = null;
            } else if (StringUtils.startsWithIgnoreCase(typeInfo, "set")) {
                String temp2 = typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
                StringTokenizer tokenizer2 = new StringTokenizer(temp2, ",");
                int maxLength2 = 0;
                int numElements = tokenizer2.countTokens();
                maxLength2 = numElements > 0 ? 0 + (numElements - 1) : maxLength2;
                while (tokenizer2.hasMoreTokens()) {
                    String setMember = tokenizer2.nextToken().trim();
                    if (setMember.startsWith("'") && setMember.endsWith("'")) {
                        maxLength2 += setMember.length() - 2;
                    } else {
                        maxLength2 += setMember.length();
                    }
                }
                this.columnSize = Integer.valueOf(maxLength2);
                this.decimalDigits = null;
            } else if (typeInfo.indexOf(",") != -1) {
                this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim());
                this.decimalDigits = Integer.valueOf(typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim());
            } else {
                this.columnSize = null;
                this.decimalDigits = null;
                if ((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, TextBundle.TEXT_ENTRY) != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "bit") != -1) && typeInfo.indexOf("(") != -1) {
                    int endParenIndex = typeInfo.indexOf(")");
                    this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, endParenIndex == -1 ? typeInfo.length() : endParenIndex).trim());
                    if (DatabaseMetaData.this.conn.getTinyInt1isBit() && this.columnSize.intValue() == 1 && StringUtils.startsWithIgnoreCase(typeInfo, 0, "tinyint")) {
                        if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
                            this.dataType = (short) 16;
                            this.typeName = "BOOLEAN";
                        } else {
                            this.dataType = (short) -7;
                            this.typeName = "BIT";
                        }
                    }
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint")) {
                    if (DatabaseMetaData.this.conn.getTinyInt1isBit() && typeInfo.indexOf("(1)") != -1) {
                        if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
                            this.dataType = (short) 16;
                            this.typeName = "BOOLEAN";
                        } else {
                            this.dataType = (short) -7;
                            this.typeName = "BIT";
                        }
                    } else {
                        this.columnSize = 3;
                        this.decimalDigits = 0;
                    }
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint")) {
                    this.columnSize = 5;
                    this.decimalDigits = 0;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint")) {
                    this.columnSize = Integer.valueOf(isUnsigned ? 8 : 7);
                    this.decimalDigits = 0;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, XmlErrorCodes.INT) || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer")) {
                    this.columnSize = 10;
                    this.decimalDigits = 0;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint")) {
                    this.columnSize = Integer.valueOf(isUnsigned ? 20 : 19);
                    this.decimalDigits = 0;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24")) {
                    this.columnSize = 19;
                    this.decimalDigits = 0;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real") || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, XmlErrorCodes.FLOAT) || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, XmlErrorCodes.DECIMAL) || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric")) {
                    this.columnSize = 12;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, XmlErrorCodes.DOUBLE)) {
                    this.columnSize = 22;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char")) {
                    this.columnSize = 1;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar")) {
                    this.columnSize = 255;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp") || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime")) {
                    this.columnSize = 19;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date")) {
                    this.columnSize = 10;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, RtspHeaders.Values.TIME)) {
                    this.columnSize = 8;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob")) {
                    this.columnSize = 255;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob")) {
                    this.columnSize = 65535;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob")) {
                    this.columnSize = 16777215;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob")) {
                    this.columnSize = Integer.MAX_VALUE;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext")) {
                    this.columnSize = 255;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, TextBundle.TEXT_ENTRY)) {
                    this.columnSize = 65535;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext")) {
                    this.columnSize = 16777215;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext")) {
                    this.columnSize = Integer.MAX_VALUE;
                } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum") || StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set")) {
                    this.columnSize = 255;
                }
            }
            this.bufferLength = MysqlIO.getMaxBuf();
            this.numPrecRadix = 10;
            if (nullabilityInfo != null) {
                if (nullabilityInfo.equals("YES")) {
                    this.nullability = 1;
                    this.isNullable = "YES";
                    return;
                } else if (nullabilityInfo.equals("UNKNOWN")) {
                    this.nullability = 2;
                    this.isNullable = "";
                    return;
                } else {
                    this.nullability = 0;
                    this.isNullable = "NO";
                    return;
                }
            }
            this.nullability = 0;
            this.isNullable = "NO";
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$IndexMetaDataKey.class */
    protected class IndexMetaDataKey implements Comparable<IndexMetaDataKey> {
        Boolean columnNonUnique;
        Short columnType;
        String columnIndexName;
        Short columnOrdinalPosition;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DatabaseMetaData.class.desiredAssertionStatus();
        }

        IndexMetaDataKey(boolean columnNonUnique, short columnType, String columnIndexName, short columnOrdinalPosition) {
            this.columnNonUnique = Boolean.valueOf(columnNonUnique);
            this.columnType = Short.valueOf(columnType);
            this.columnIndexName = columnIndexName;
            this.columnOrdinalPosition = Short.valueOf(columnOrdinalPosition);
        }

        @Override // java.lang.Comparable
        public int compareTo(IndexMetaDataKey indexInfoKey) {
            int compareResult = this.columnNonUnique.compareTo(indexInfoKey.columnNonUnique);
            if (compareResult != 0) {
                return compareResult;
            }
            int compareResult2 = this.columnType.compareTo(indexInfoKey.columnType);
            if (compareResult2 != 0) {
                return compareResult2;
            }
            int compareResult3 = this.columnIndexName.compareTo(indexInfoKey.columnIndexName);
            if (compareResult3 != 0) {
                return compareResult3;
            }
            return this.columnOrdinalPosition.compareTo(indexInfoKey.columnOrdinalPosition);
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return (obj instanceof IndexMetaDataKey) && compareTo((IndexMetaDataKey) obj) == 0;
        }

        public int hashCode() {
            if ($assertionsDisabled) {
                return 0;
            }
            throw new AssertionError("hashCode not designed");
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$TableMetaDataKey.class */
    protected class TableMetaDataKey implements Comparable<TableMetaDataKey> {
        String tableType;
        String tableCat;
        String tableSchem;
        String tableName;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DatabaseMetaData.class.desiredAssertionStatus();
        }

        TableMetaDataKey(String tableType, String tableCat, String tableSchem, String tableName) {
            this.tableType = tableType == null ? "" : tableType;
            this.tableCat = tableCat == null ? "" : tableCat;
            this.tableSchem = tableSchem == null ? "" : tableSchem;
            this.tableName = tableName == null ? "" : tableName;
        }

        @Override // java.lang.Comparable
        public int compareTo(TableMetaDataKey tablesKey) {
            int compareResult = this.tableType.compareTo(tablesKey.tableType);
            if (compareResult != 0) {
                return compareResult;
            }
            int compareResult2 = this.tableCat.compareTo(tablesKey.tableCat);
            if (compareResult2 != 0) {
                return compareResult2;
            }
            int compareResult3 = this.tableSchem.compareTo(tablesKey.tableSchem);
            if (compareResult3 != 0) {
                return compareResult3;
            }
            return this.tableName.compareTo(tablesKey.tableName);
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return (obj instanceof TableMetaDataKey) && compareTo((TableMetaDataKey) obj) == 0;
        }

        public int hashCode() {
            if ($assertionsDisabled) {
                return 0;
            }
            throw new AssertionError("hashCode not designed");
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$ComparableWrapper.class */
    protected class ComparableWrapper<K extends Comparable<? super K>, V> implements Comparable<ComparableWrapper<K, V>> {
        K key;
        V value;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DatabaseMetaData.class.desiredAssertionStatus();
        }

        public ComparableWrapper(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        @Override // java.lang.Comparable
        public int compareTo(ComparableWrapper<K, V> other) {
            return ((Comparable) getKey()).compareTo(other.getKey());
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ComparableWrapper)) {
                return false;
            }
            Object otherKey = ((ComparableWrapper) obj).getKey();
            return this.key.equals(otherKey);
        }

        public int hashCode() {
            if ($assertionsDisabled) {
                return 0;
            }
            throw new AssertionError("hashCode not designed");
        }

        public String toString() {
            return "{KEY:" + this.key + "; VALUE:" + this.value + "}";
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$TableType.class */
    protected enum TableType {
        LOCAL_TEMPORARY("LOCAL TEMPORARY"),
        SYSTEM_TABLE("SYSTEM TABLE"),
        SYSTEM_VIEW("SYSTEM VIEW"),
        TABLE("TABLE", new String[]{"BASE TABLE"}),
        VIEW("VIEW"),
        UNKNOWN("UNKNOWN");

        private String name;
        private byte[] nameAsBytes;
        private String[] synonyms;

        TableType(String tableTypeName) {
            this(tableTypeName, null);
        }

        TableType(String tableTypeName, String[] tableTypeSynonyms) {
            this.name = tableTypeName;
            this.nameAsBytes = tableTypeName.getBytes();
            this.synonyms = tableTypeSynonyms;
        }

        String getName() {
            return this.name;
        }

        byte[] asBytes() {
            return this.nameAsBytes;
        }

        boolean equalsTo(String tableTypeName) {
            return this.name.equalsIgnoreCase(tableTypeName);
        }

        static TableType getTableTypeEqualTo(String tableTypeName) {
            TableType[] arr$ = values();
            for (TableType tableType : arr$) {
                if (tableType.equalsTo(tableTypeName)) {
                    return tableType;
                }
            }
            return UNKNOWN;
        }

        boolean compliesWith(String tableTypeName) {
            if (equalsTo(tableTypeName)) {
                return true;
            }
            if (this.synonyms != null) {
                String[] arr$ = this.synonyms;
                for (String synonym : arr$) {
                    if (synonym.equalsIgnoreCase(tableTypeName)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        static TableType getTableTypeCompliantWith(String tableTypeName) {
            TableType[] arr$ = values();
            for (TableType tableType : arr$) {
                if (tableType.compliesWith(tableTypeName)) {
                    return tableType;
                }
            }
            return UNKNOWN;
        }
    }

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_DBMD_SHOW_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaData").getConstructor(MySQLConnection.class, String.class);
                JDBC_4_DBMD_IS_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema").getConstructor(MySQLConnection.class, String.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        } else {
            JDBC_4_DBMD_IS_CTOR = null;
            JDBC_4_DBMD_SHOW_CTOR = null;
        }
        MYSQL_KEYWORDS = new String[]{"ACCESSIBLE", "ADD", Rule.ALL, "ALTER", "ANALYZE", "AND", "ARRAY", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DENSE_RANK", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "EMPTY", "ENCLOSED", "ESCAPED", "EXCEPT", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FIRST_VALUE", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "FUNCTION", "GENERATED", "GET", "GRANT", "GROUP", "GROUPING", "GROUPS", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE", "JOIN", "JSON_TABLE", "KEY", "KEYS", "KILL", "LAG", "LAST_VALUE", "LATERAL", "LEAD", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MEMBER", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NTH_VALUE", "NTILE", ActionConst.NULL, "NUMERIC", "OF", "ON", "OPTIMIZE", "OPTIMIZER_COSTS", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "OVER", "PARTITION", "PERCENT_RANK", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "RANK", "READ", "READS", "READ_WRITE", "REAL", "RECURSIVE", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "ROW", "ROWS", "ROW_NUMBER", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", SSL.DEFAULT_PROTOCOL, "STARTING", "STORED", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", "WINDOW", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"};
        SQL92_KEYWORDS = new String[]{"ABSOLUTE", "ACTION", "ADD", Rule.ALL, "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIT", "BIT_LENGTH", "BOTH", "BY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOSE", "COALESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", ConnectMethod.NAME, "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONTINUE", "CONVERT", "CORRESPONDING", "COUNT", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DESCRIBE", "DESCRIPTOR", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DOMAIN", "DOUBLE", "DROP", "ELSE", "END", "END-EXEC", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FULL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HAVING", "HOUR", "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INPUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LAST", "LEADING", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOWER", "MATCH", "MAX", "MIN", "MINUTE", "MODULE", "MONTH", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NOT", ActionConst.NULL, "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARTIAL", "POSITION", "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES", "RELATIVE", "RESTRICT", "REVOKE", "RIGHT", "ROLLBACK", "ROWS", "SCHEMA", "SCROLL", "SECOND", "SECTION", "SELECT", "SESSION", "SESSION_USER", "SET", "SIZE", "SMALLINT", "SOME", "SPACE", "SQL", "SQLCODE", "SQLERROR", "SQLSTATE", "SUBSTRING", "SUM", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIM", "TRUE", "UNION", "UNIQUE", "UNKNOWN", "UPDATE", "UPPER", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN", "WHENEVER", "WHERE", "WITH", "WORK", "WRITE", "YEAR", "ZONE"};
        SQL2003_KEYWORDS = new String[]{"ABS", Rule.ALL, "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL", "CALLED", "CARDINALITY", "CASCADED", "CASE", "CAST", "CEIL", "CEILING", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOB", "CLOSE", "COALESCE", "COLLATE", "COLLECT", "COLUMN", "COMMIT", "CONDITION", ConnectMethod.NAME, "CONSTRAINT", "CONVERT", "CORR", "CORRESPONDING", "COUNT", "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DENSE_RANK", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", "DISTINCT", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC", "ESCAPE", "EVERY", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXP", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FILTER", "FLOAT", "FLOOR", "FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "FUSION", "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD", "HOUR", "IDENTITY", "IN", "INDICATOR", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERSECTION", "INTERVAL", "INTO", "IS", "JOIN", "LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEFT", "LIKE", "LN", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOWER", "MATCH", "MAX", "MEMBER", "MERGE", "METHOD", "MIN", "MINUTE", "MOD", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NO", org.apache.catalina.realm.Constants.NONE_TRANSPORT, "NORMALIZE", "NOT", ActionConst.NULL, "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OVER", "OVERLAPS", "OVERLAY", "PARAMETER", "PARTITION", "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "POSITION", "POWER", "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE", "RANK", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "RELEASE", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "ROW_NUMBER", "SAVEPOINT", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "START", "STATIC", "STDDEV_POP", "STDDEV_SAMP", "SUBMULTISET", "SUBSTRING", "SUM", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATE", "TRANSLATION", "TREAT", "TRIGGER", "TRIM", "TRUE", "UESCAPE", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "UPPER", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "YEAR"};
        mysqlKeywords = null;
    }

    protected static DatabaseMetaData getInstance(MySQLConnection connToSet, String databaseToSet, boolean checkForInfoSchema) throws SQLException {
        if (Util.isJdbc4()) {
            return (checkForInfoSchema && connToSet.getUseInformationSchema() && connToSet.versionMeetsMinimum(5, 0, 7)) ? (DatabaseMetaData) Util.handleNewInstance(JDBC_4_DBMD_IS_CTOR, new Object[]{connToSet, databaseToSet}, connToSet.getExceptionInterceptor()) : (DatabaseMetaData) Util.handleNewInstance(JDBC_4_DBMD_SHOW_CTOR, new Object[]{connToSet, databaseToSet}, connToSet.getExceptionInterceptor());
        }
        if (checkForInfoSchema && connToSet.getUseInformationSchema() && connToSet.versionMeetsMinimum(5, 0, 7)) {
            return new DatabaseMetaDataUsingInfoSchema(connToSet, databaseToSet);
        }
        return new DatabaseMetaData(connToSet, databaseToSet);
    }

    protected DatabaseMetaData(MySQLConnection connToSet, String databaseToSet) {
        this.database = null;
        this.conn = connToSet;
        this.database = databaseToSet;
        this.exceptionInterceptor = this.conn.getExceptionInterceptor();
        String identifierQuote = null;
        try {
            try {
                identifierQuote = getIdentifierQuoteString();
                this.quotedId = identifierQuote;
            } catch (SQLException sqlEx) {
                AssertionFailedException.shouldNotHappen(sqlEx);
                this.quotedId = identifierQuote;
            }
        } catch (Throwable th) {
            this.quotedId = identifierQuote;
            throw th;
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allProceduresAreCallable() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean allTablesAreSelectable() throws SQLException {
        return false;
    }

    private ResultSet buildResultSet(Field[] fields, ArrayList<ResultSetRow> rows) throws SQLException {
        return buildResultSet(fields, rows, this.conn);
    }

    static ResultSet buildResultSet(Field[] fields, ArrayList<ResultSetRow> rows, MySQLConnection c) throws SQLException {
        int fieldsLength = fields.length;
        for (int i = 0; i < fieldsLength; i++) {
            int jdbcType = fields[i].getSQLType();
            switch (jdbcType) {
                case -1:
                case 1:
                case 12:
                    fields[i].setEncoding(c.getCharacterSetMetadata(), c);
                    break;
            }
            fields[i].setConnection(c);
            fields[i].setUseOldNameMetadata(true);
        }
        return ResultSetImpl.getInstance(c.getCatalog(), fields, new RowDataStatic(rows), c, null, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r20v1 */
    protected void convertToJdbcFunctionList(String str, ResultSet resultSet, boolean z, String str2, List<ComparableWrapper<String, ResultSetRow>> list, int i, Field[] fieldArr) throws SQLException {
        byte[][] bArr;
        while (resultSet.next()) {
            boolean z2 = true;
            if (z) {
                z2 = false;
                String string = resultSet.getString(1);
                if (str2 == null && string == null) {
                    z2 = true;
                } else if (str2 != null && str2.equals(string)) {
                    z2 = true;
                }
            }
            if (z2) {
                String string2 = resultSet.getString(i);
                if (fieldArr != null && fieldArr.length == 9) {
                    byte[][] bArr2 = new byte[9];
                    bArr2[0] = str == null ? null : s2b(str);
                    bArr2[1] = null;
                    bArr2[2] = s2b(string2);
                    bArr2[3] = null;
                    bArr2[4] = null;
                    bArr2[5] = null;
                    bArr2[6] = s2b(resultSet.getString("comment"));
                    bArr2[7] = s2b(Integer.toString(2));
                    bArr2[8] = s2b(string2);
                    bArr = bArr2;
                } else {
                    ?? r20 = new byte[6];
                    r20[0] = str == null ? null : s2b(str);
                    r20[1] = 0;
                    r20[2] = s2b(string2);
                    r20[3] = s2b(resultSet.getString("comment"));
                    r20[4] = s2b(Integer.toString(getJDBC4FunctionNoTableConstant()));
                    r20[5] = s2b(string2);
                    bArr = r20;
                }
                list.add(new ComparableWrapper<>(getFullyQualifiedName(str, string2), new ByteArrayRow(bArr, getExceptionInterceptor())));
            }
        }
    }

    protected String getFullyQualifiedName(String catalog, String entity) {
        return StringUtils.quoteIdentifier(catalog == null ? "" : catalog, this.quotedId, this.conn.getPedantic()) + '.' + StringUtils.quoteIdentifier(entity, this.quotedId, this.conn.getPedantic());
    }

    protected int getJDBC4FunctionNoTableConstant() {
        return 0;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [byte[], byte[][]] */
    protected void convertToJdbcProcedureList(boolean fromSelect, String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, List<ComparableWrapper<String, ResultSetRow>> procedureRows, int nameIndex) throws SQLException {
        while (proceduresRs.next()) {
            boolean shouldAdd = true;
            if (needsClientFiltering) {
                shouldAdd = false;
                String procDb = proceduresRs.getString(1);
                if (db == null && procDb == null) {
                    shouldAdd = true;
                } else if (db != null && db.equals(procDb)) {
                    shouldAdd = true;
                }
            }
            if (shouldAdd) {
                String procedureName = proceduresRs.getString(nameIndex);
                ?? r0 = new byte[9];
                r0[0] = catalog == null ? null : s2b(catalog);
                r0[1] = 0;
                r0[2] = s2b(procedureName);
                r0[3] = 0;
                r0[4] = 0;
                r0[5] = 0;
                r0[6] = s2b(proceduresRs.getString("comment"));
                boolean isFunction = fromSelect ? "FUNCTION".equalsIgnoreCase(proceduresRs.getString("type")) : false;
                r0[7] = s2b(isFunction ? Integer.toString(2) : Integer.toString(1));
                r0[8] = s2b(procedureName);
                procedureRows.add(new ComparableWrapper<>(getFullyQualifiedName(catalog, procedureName), new ByteArrayRow(r0, getExceptionInterceptor())));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ResultSetRow convertTypeDescriptorToProcedureRow(byte[] procNameAsBytes, byte[] procCatAsBytes, String paramName, boolean isOutParam, boolean isInParam, boolean isReturnParam, TypeDescriptor typeDesc, boolean forGetFunctionColumns, int ordinal) throws SQLException {
        byte[][] bArr = forGetFunctionColumns ? new byte[17] : new byte[20];
        bArr[0] = procCatAsBytes;
        bArr[1] = 0;
        bArr[2] = procNameAsBytes;
        bArr[3] = s2b(paramName);
        bArr[4] = s2b(String.valueOf(getColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns)));
        bArr[5] = s2b(Short.toString(typeDesc.dataType));
        bArr[6] = s2b(typeDesc.typeName);
        bArr[7] = typeDesc.columnSize == null ? null : s2b(typeDesc.columnSize.toString());
        bArr[8] = bArr[7];
        bArr[9] = typeDesc.decimalDigits == null ? null : s2b(typeDesc.decimalDigits.toString());
        bArr[10] = s2b(Integer.toString(typeDesc.numPrecRadix));
        switch (typeDesc.nullability) {
            case 0:
                bArr[11] = s2b(String.valueOf(0));
                break;
            case 1:
                bArr[11] = s2b(String.valueOf(1));
                break;
            case 2:
                bArr[11] = s2b(String.valueOf(2));
                break;
            default:
                throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        bArr[12] = 0;
        if (forGetFunctionColumns) {
            bArr[13] = 0;
            bArr[14] = s2b(String.valueOf(ordinal));
            bArr[15] = s2b(typeDesc.isNullable);
            bArr[16] = procNameAsBytes;
        } else {
            bArr[13] = 0;
            bArr[14] = 0;
            bArr[15] = 0;
            bArr[16] = 0;
            bArr[17] = s2b(String.valueOf(ordinal));
            bArr[18] = s2b(typeDesc.isNullable);
            bArr[19] = procNameAsBytes;
        }
        return new ByteArrayRow(bArr, getExceptionInterceptor());
    }

    protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns) {
        if (isInParam && isOutParam) {
            return 2;
        }
        if (isInParam) {
            return 1;
        }
        if (isOutParam) {
            return 4;
        }
        if (isReturnParam) {
            return 5;
        }
        return 0;
    }

    protected ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean deletesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    public List<ResultSetRow> extractForeignKeyForTable(ArrayList<ResultSetRow> rows, ResultSet rs, String catalog) throws SQLException {
        int afterFk;
        int indexOfRef;
        int endPos;
        ?? r0 = new byte[3];
        r0[0] = rs.getBytes(1);
        r0[1] = s2b(SUPPORTS_FK);
        String createTableString = rs.getString(2);
        StringTokenizer lineTokenizer = new StringTokenizer(createTableString, ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        StringBuilder commentBuf = new StringBuilder("comment; ");
        boolean firstTime = true;
        while (lineTokenizer.hasMoreTokens()) {
            String line = lineTokenizer.nextToken().trim();
            String constraintName = null;
            if (StringUtils.startsWithIgnoreCase(line, "CONSTRAINT")) {
                boolean usingBackTicks = true;
                int beginPos = StringUtils.indexOfQuoteDoubleAware(line, this.quotedId, 0);
                if (beginPos == -1) {
                    beginPos = line.indexOf(SymbolConstants.QUOTES_SYMBOL);
                    usingBackTicks = false;
                }
                if (beginPos != -1) {
                    if (usingBackTicks) {
                        endPos = StringUtils.indexOfQuoteDoubleAware(line, this.quotedId, beginPos + 1);
                    } else {
                        endPos = StringUtils.indexOfQuoteDoubleAware(line, SymbolConstants.QUOTES_SYMBOL, beginPos + 1);
                    }
                    if (endPos != -1) {
                        constraintName = line.substring(beginPos + 1, endPos);
                        line = line.substring(endPos + 1, line.length()).trim();
                    }
                }
            }
            if (line.startsWith("FOREIGN KEY")) {
                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }
                int indexOfFK = line.indexOf("FOREIGN KEY");
                String localColumnName = null;
                String referencedCatalogName = StringUtils.quoteIdentifier(catalog, this.quotedId, this.conn.getPedantic());
                String referencedTableName = null;
                String referencedColumnName = null;
                if (indexOfFK != -1 && (indexOfRef = StringUtils.indexOfIgnoreCase((afterFk = indexOfFK + "FOREIGN KEY".length()), line, "REFERENCES", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL)) != -1) {
                    int indexOfParenOpen = line.indexOf(40, afterFk);
                    int indexOfParenClose = StringUtils.indexOfIgnoreCase(indexOfParenOpen, line, ")", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
                    if (indexOfParenOpen == -1 || indexOfParenClose == -1) {
                    }
                    localColumnName = line.substring(indexOfParenOpen + 1, indexOfParenClose);
                    int afterRef = indexOfRef + "REFERENCES".length();
                    int referencedColumnBegin = StringUtils.indexOfIgnoreCase(afterRef, line, "(", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
                    if (referencedColumnBegin != -1) {
                        referencedTableName = line.substring(afterRef, referencedColumnBegin);
                        int referencedColumnEnd = StringUtils.indexOfIgnoreCase(referencedColumnBegin + 1, line, ")", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
                        if (referencedColumnEnd != -1) {
                            referencedColumnName = line.substring(referencedColumnBegin + 1, referencedColumnEnd);
                        }
                        int indexOfCatalogSep = StringUtils.indexOfIgnoreCase(0, referencedTableName, ".", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
                        if (indexOfCatalogSep != -1) {
                            referencedCatalogName = referencedTableName.substring(0, indexOfCatalogSep);
                            referencedTableName = referencedTableName.substring(indexOfCatalogSep + 1);
                        }
                    }
                }
                if (!firstTime) {
                    commentBuf.append("; ");
                } else {
                    firstTime = false;
                }
                if (constraintName != null) {
                    commentBuf.append(constraintName);
                } else {
                    commentBuf.append("not_available");
                }
                commentBuf.append("(");
                commentBuf.append(localColumnName);
                commentBuf.append(") REFER ");
                commentBuf.append(referencedCatalogName);
                commentBuf.append("/");
                commentBuf.append(referencedTableName);
                commentBuf.append("(");
                commentBuf.append(referencedColumnName);
                commentBuf.append(")");
                int lastParenIndex = line.lastIndexOf(")");
                if (lastParenIndex != line.length() - 1) {
                    String cascadeOptions = line.substring(lastParenIndex + 1);
                    commentBuf.append(SymbolConstants.SPACE_SYMBOL);
                    commentBuf.append(cascadeOptions);
                }
            }
        }
        r0[2] = s2b(commentBuf.toString());
        rows.add(new ByteArrayRow(r0, getExceptionInterceptor()));
        return rows;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0057, code lost:
    
        throw r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x014b, code lost:
    
        throw r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x005f, code lost:
    
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x015f, code lost:
    
        if (r0 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0162, code lost:
    
        r0.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0153 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0162 A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.sql.ResultSet extractForeignKeyFromCreateTable(java.lang.String r10, java.lang.String r11) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 375
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.extractForeignKeyFromCreateTable(java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        Field[] fields = {new Field("", "TYPE_CAT", 1, 32), new Field("", "TYPE_SCHEM", 1, 32), new Field("", "TYPE_NAME", 1, 32), new Field("", "ATTR_NAME", 1, 32), new Field("", "DATA_TYPE", 5, 32), new Field("", "ATTR_TYPE_NAME", 1, 32), new Field("", "ATTR_SIZE", 4, 32), new Field("", "DECIMAL_DIGITS", 4, 32), new Field("", "NUM_PREC_RADIX", 4, 32), new Field("", "NULLABLE ", 4, 32), new Field("", "REMARKS", 1, 32), new Field("", "ATTR_DEF", 1, 32), new Field("", "SQL_DATA_TYPE", 4, 32), new Field("", "SQL_DATETIME_SUB", 4, 32), new Field("", "CHAR_OCTET_LENGTH", 4, 32), new Field("", "ORDINAL_POSITION", 4, 32), new Field("", "IS_NULLABLE", 1, 32), new Field("", "SCOPE_CATALOG", 1, 32), new Field("", "SCOPE_SCHEMA", 1, 32), new Field("", "SCOPE_TABLE", 1, 32), new Field("", "SOURCE_DATA_TYPE", 5, 32)};
        return buildResultSet(fields, new ArrayList<>());
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getBestRowIdentifier(java.lang.String r10, java.lang.String r11, final java.lang.String r12, int r13, boolean r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 251
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void getCallStmtParameterTypes(java.lang.String r13, java.lang.String r14, com.mysql.jdbc.DatabaseMetaData.ProcedureType r15, java.lang.String r16, java.util.List<com.mysql.jdbc.ResultSetRow> r17, boolean r18) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 1527
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getCallStmtParameterTypes(java.lang.String, java.lang.String, com.mysql.jdbc.DatabaseMetaData$ProcedureType, java.lang.String, java.util.List, boolean):void");
    }

    private int endPositionOfParameterDeclaration(int beginIndex, String procedureDef, String quoteChar) throws SQLException {
        int currentPos = beginIndex + 1;
        int parenDepth = 1;
        while (parenDepth > 0 && currentPos < procedureDef.length()) {
            int closedParenIndex = StringUtils.indexOfIgnoreCase(currentPos, procedureDef, ")", quoteChar, quoteChar, this.conn.isNoBackslashEscapesSet() ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
            if (closedParenIndex != -1) {
                int nextOpenParenIndex = StringUtils.indexOfIgnoreCase(currentPos, procedureDef, "(", quoteChar, quoteChar, this.conn.isNoBackslashEscapesSet() ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
                if (nextOpenParenIndex != -1 && nextOpenParenIndex < closedParenIndex) {
                    parenDepth++;
                    currentPos = closedParenIndex + 1;
                } else {
                    parenDepth--;
                    currentPos = closedParenIndex;
                }
            } else {
                throw SQLError.createSQLException("Internal error when parsing callable statement metadata", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        }
        return currentPos;
    }

    private int findEndOfReturnsClause(String procedureDefn, int positionOfReturnKeyword) throws SQLException {
        String openingMarkers = this.quotedId + "(";
        String closingMarkers = this.quotedId + ")";
        String[] tokens = {"LANGUAGE", "NOT", "DETERMINISTIC", "CONTAINS", "NO", "READ", "MODIFIES", "SQL", "COMMENT", "BEGIN", "RETURN"};
        int startLookingAt = positionOfReturnKeyword + "RETURNS".length() + 1;
        int endOfReturn = -1;
        for (String str : tokens) {
            int nextEndOfReturn = StringUtils.indexOfIgnoreCase(startLookingAt, procedureDefn, str, openingMarkers, closingMarkers, this.conn.isNoBackslashEscapesSet() ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
            if (nextEndOfReturn != -1 && (endOfReturn == -1 || nextEndOfReturn < endOfReturn)) {
                endOfReturn = nextEndOfReturn;
            }
        }
        if (endOfReturn != -1) {
            return endOfReturn;
        }
        int endOfReturn2 = StringUtils.indexOfIgnoreCase(startLookingAt, procedureDefn, ":", openingMarkers, closingMarkers, this.conn.isNoBackslashEscapesSet() ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
        if (endOfReturn2 != -1) {
            for (int i = endOfReturn2; i > 0; i--) {
                if (Character.isWhitespace(procedureDefn.charAt(i))) {
                    return i;
                }
            }
        }
        throw SQLError.createSQLException("Internal error when parsing callable statement metadata", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
    }

    private int getCascadeDeleteOption(String cascadeOptions) {
        int onDeletePos = cascadeOptions.indexOf("ON DELETE");
        if (onDeletePos != -1) {
            String deleteOptions = cascadeOptions.substring(onDeletePos, cascadeOptions.length());
            if (deleteOptions.startsWith("ON DELETE CASCADE")) {
                return 0;
            }
            if (deleteOptions.startsWith("ON DELETE SET NULL")) {
                return 2;
            }
            if (deleteOptions.startsWith("ON DELETE RESTRICT")) {
                return 1;
            }
            if (deleteOptions.startsWith("ON DELETE NO ACTION")) {
                return 3;
            }
            return 3;
        }
        return 3;
    }

    private int getCascadeUpdateOption(String cascadeOptions) {
        int onUpdatePos = cascadeOptions.indexOf("ON UPDATE");
        if (onUpdatePos != -1) {
            String updateOptions = cascadeOptions.substring(onUpdatePos, cascadeOptions.length());
            if (updateOptions.startsWith("ON UPDATE CASCADE")) {
                return 0;
            }
            if (updateOptions.startsWith("ON UPDATE SET NULL")) {
                return 2;
            }
            if (updateOptions.startsWith("ON UPDATE RESTRICT")) {
                return 1;
            }
            if (updateOptions.startsWith("ON UPDATE NO ACTION")) {
                return 3;
            }
            return 3;
        }
        return 3;
    }

    protected IteratorWithCleanup<String> getCatalogIterator(String catalogSpec) throws SQLException {
        IteratorWithCleanup<String> allCatalogsIter;
        if (catalogSpec != null) {
            if (!catalogSpec.equals("")) {
                if (this.conn.getPedantic()) {
                    allCatalogsIter = new SingleStringIterator(catalogSpec);
                } else {
                    allCatalogsIter = new SingleStringIterator(StringUtils.unQuoteIdentifier(catalogSpec, this.quotedId));
                }
            } else {
                allCatalogsIter = new SingleStringIterator(this.database);
            }
        } else if (this.conn.getNullCatalogMeansCurrent()) {
            allCatalogsIter = new SingleStringIterator(this.database);
        } else {
            allCatalogsIter = new ResultSetIterator(getCatalogs(), 1);
        }
        return allCatalogsIter;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x00e2, code lost:
    
        throw r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00e6, code lost:
    
        if (r11 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00e9, code lost:
    
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00fc, code lost:
    
        if (r12 == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00ff, code lost:
    
        r12.close();
     */
    /* JADX WARN: Type inference failed for: r0v42, types: [byte[], byte[][]] */
    @Override // java.sql.DatabaseMetaData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.sql.ResultSet getCatalogs() throws com.mysql.jdbc.AssertionFailedException, java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 275
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getCatalogs():java.sql.ResultSet");
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogSeparator() throws SQLException {
        return ".";
    }

    @Override // java.sql.DatabaseMetaData
    public String getCatalogTerm() throws SQLException {
        return "database";
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0225, code lost:
    
        throw r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x022a, code lost:
    
        if (r17 == null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x022d, code lost:
    
        r17.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x023e, code lost:
    
        if (r16 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0241, code lost:
    
        r16.close();
     */
    /* JADX WARN: Type inference failed for: r0v61, types: [byte[], byte[][]] */
    @Override // java.sql.DatabaseMetaData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.sql.ResultSet getColumnPrivileges(java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.String r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 603
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getColumns(java.lang.String r11, final java.lang.String r12, final java.lang.String r13, java.lang.String r14) throws java.sql.SQLException {
        /*
            r10 = this;
            r0 = r14
            if (r0 != 0) goto L25
            r0 = r10
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            boolean r0 = r0.getNullNamePatternMatchesAll()
            if (r0 == 0) goto L18
            java.lang.String r0 = "%"
            r14 = r0
            goto L25
        L18:
            java.lang.String r0 = "Column name pattern can not be NULL or empty."
            java.lang.String r1 = "S1009"
            r2 = r10
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)
            throw r0
        L25:
            r0 = r14
            r15 = r0
            r0 = r10
            com.mysql.jdbc.Field[] r0 = r0.createColumnsFields()
            r16 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r17 = r0
            r0 = r10
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.sql.Statement r0 = r0.getMetadataSafeStatement()
            r18 = r0
            com.mysql.jdbc.DatabaseMetaData$2 r0 = new com.mysql.jdbc.DatabaseMetaData$2     // Catch: java.lang.Throwable -> L61
            r1 = r0
            r2 = r10
            r3 = r10
            r4 = r11
            com.mysql.jdbc.DatabaseMetaData$IteratorWithCleanup r3 = r3.getCatalogIterator(r4)     // Catch: java.lang.Throwable -> L61
            r4 = r13
            r5 = r12
            r6 = r15
            r7 = r18
            r8 = r17
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L61
            r0.doForAll()     // Catch: java.lang.Throwable -> L61
            r0 = jsr -> L69
        L5e:
            goto L79
        L61:
            r19 = move-exception
            r0 = jsr -> L69
        L66:
            r1 = r19
            throw r1
        L69:
            r20 = r0
            r0 = r18
            if (r0 == 0) goto L77
            r0 = r18
            r0.close()
        L77:
            ret r20
        L79:
            r1 = r10
            r2 = r16
            r3 = r17
            java.sql.ResultSet r1 = r1.buildResultSet(r2, r3)
            r19 = r1
            r1 = r19
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected Field[] createColumnsFields() {
        Field[] fields = {new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "DATA_TYPE", 4, 5), new Field("", "TYPE_NAME", 1, 16), new Field("", "COLUMN_SIZE", 4, Integer.toString(Integer.MAX_VALUE).length()), new Field("", "BUFFER_LENGTH", 4, 10), new Field("", "DECIMAL_DIGITS", 4, 10), new Field("", "NUM_PREC_RADIX", 4, 10), new Field("", "NULLABLE", 4, 10), new Field("", "REMARKS", 1, 0), new Field("", "COLUMN_DEF", 1, 0), new Field("", "SQL_DATA_TYPE", 4, 10), new Field("", "SQL_DATETIME_SUB", 4, 10), new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(Integer.MAX_VALUE).length()), new Field("", "ORDINAL_POSITION", 4, 10), new Field("", "IS_NULLABLE", 1, 3), new Field("", "SCOPE_CATALOG", 1, 255), new Field("", "SCOPE_SCHEMA", 1, 255), new Field("", "SCOPE_TABLE", 1, 255), new Field("", "SOURCE_DATA_TYPE", 5, 10), new Field("", "IS_AUTOINCREMENT", 1, 3), new Field("", "IS_GENERATEDCOLUMN", 1, 3)};
        return fields;
    }

    @Override // java.sql.DatabaseMetaData
    public java.sql.Connection getConnection() throws SQLException {
        return this.conn;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getCrossReference(final java.lang.String r14, final java.lang.String r15, final java.lang.String r16, final java.lang.String r17, final java.lang.String r18, final java.lang.String r19) throws java.sql.SQLException {
        /*
            r13 = this;
            r0 = r16
            if (r0 != 0) goto L10
            java.lang.String r0 = "Table not specified."
            java.lang.String r1 = "S1009"
            r2 = r13
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)
            throw r0
        L10:
            r0 = r13
            com.mysql.jdbc.Field[] r0 = r0.createFkMetadataFields()
            r20 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r21 = r0
            r0 = r13
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            r1 = 3
            r2 = 23
            r3 = 0
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto L76
            r0 = r13
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.sql.Statement r0 = r0.getMetadataSafeStatement()
            r22 = r0
            com.mysql.jdbc.DatabaseMetaData$3 r0 = new com.mysql.jdbc.DatabaseMetaData$3     // Catch: java.lang.Throwable -> L5e
            r1 = r0
            r2 = r13
            r3 = r13
            r4 = r17
            com.mysql.jdbc.DatabaseMetaData$IteratorWithCleanup r3 = r3.getCatalogIterator(r4)     // Catch: java.lang.Throwable -> L5e
            r4 = r22
            r5 = r19
            r6 = r16
            r7 = r17
            r8 = r18
            r9 = r14
            r10 = r15
            r11 = r21
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L5e
            r0.doForAll()     // Catch: java.lang.Throwable -> L5e
            r0 = jsr -> L66
        L5b:
            goto L76
        L5e:
            r23 = move-exception
            r0 = jsr -> L66
        L63:
            r1 = r23
            throw r1
        L66:
            r24 = r0
            r0 = r22
            if (r0 == 0) goto L74
            r0 = r22
            r0.close()
        L74:
            ret r24
        L76:
            r0 = r13
            r1 = r20
            r2 = r21
            java.sql.ResultSet r0 = r0.buildResultSet(r1, r2)
            r22 = r0
            r0 = r22
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected Field[] createFkMetadataFields() {
        Field[] fields = {new Field("", "PKTABLE_CAT", 1, 255), new Field("", "PKTABLE_SCHEM", 1, 0), new Field("", "PKTABLE_NAME", 1, 255), new Field("", "PKCOLUMN_NAME", 1, 32), new Field("", "FKTABLE_CAT", 1, 255), new Field("", "FKTABLE_SCHEM", 1, 0), new Field("", "FKTABLE_NAME", 1, 255), new Field("", "FKCOLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 2), new Field("", "UPDATE_RULE", 5, 2), new Field("", "DELETE_RULE", 5, 2), new Field("", "FK_NAME", 1, 0), new Field("", "PK_NAME", 1, 0), new Field("", "DEFERRABILITY", 5, 2)};
        return fields;
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMajorVersion() throws SQLException {
        return this.conn.getServerMajorVersion();
    }

    @Override // java.sql.DatabaseMetaData
    public int getDatabaseMinorVersion() throws SQLException {
        return this.conn.getServerMinorVersion();
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductName() throws SQLException {
        return "MySQL";
    }

    @Override // java.sql.DatabaseMetaData
    public String getDatabaseProductVersion() throws SQLException {
        return this.conn.getServerVersion();
    }

    @Override // java.sql.DatabaseMetaData
    public int getDefaultTransactionIsolation() throws SQLException {
        if (this.conn.supportsIsolationLevel()) {
            return 2;
        }
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getDriverMajorVersion() {
        return NonRegisteringDriver.getMajorVersionInternal();
    }

    @Override // java.sql.DatabaseMetaData
    public int getDriverMinorVersion() {
        return NonRegisteringDriver.getMinorVersionInternal();
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverName() throws SQLException {
        return NonRegisteringDriver.NAME;
    }

    @Override // java.sql.DatabaseMetaData
    public String getDriverVersion() throws SQLException {
        return "mysql-connector-java-5.1.48 ( Revision: 29734982609c32d3ab7e5cac2e6acee69ff6b4aa )";
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getExportedKeys(java.lang.String r9, java.lang.String r10, final java.lang.String r11) throws java.sql.SQLException {
        /*
            r8 = this;
            r0 = r11
            if (r0 != 0) goto L10
            java.lang.String r0 = "Table not specified."
            java.lang.String r1 = "S1009"
            r2 = r8
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)
            throw r0
        L10:
            r0 = r8
            com.mysql.jdbc.Field[] r0 = r0.createFkMetadataFields()
            r12 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r13 = r0
            r0 = r8
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            r1 = 3
            r2 = 23
            r3 = 0
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto L6d
            r0 = r8
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.sql.Statement r0 = r0.getMetadataSafeStatement()
            r14 = r0
            com.mysql.jdbc.DatabaseMetaData$4 r0 = new com.mysql.jdbc.DatabaseMetaData$4     // Catch: java.lang.Throwable -> L55
            r1 = r0
            r2 = r8
            r3 = r8
            r4 = r9
            com.mysql.jdbc.DatabaseMetaData$IteratorWithCleanup r3 = r3.getCatalogIterator(r4)     // Catch: java.lang.Throwable -> L55
            r4 = r14
            r5 = r11
            r6 = r13
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L55
            r0.doForAll()     // Catch: java.lang.Throwable -> L55
            r0 = jsr -> L5d
        L52:
            goto L6d
        L55:
            r15 = move-exception
            r0 = jsr -> L5d
        L5a:
            r1 = r15
            throw r1
        L5d:
            r16 = r0
            r0 = r14
            if (r0 == 0) goto L6b
            r0 = r14
            r0.close()
        L6b:
            ret r16
        L6d:
            r0 = r8
            r1 = r12
            r2 = r13
            java.sql.ResultSet r0 = r0.buildResultSet(r1, r2)
            r14 = r0
            r0 = r14
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getExportedKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected void getExportKeyResults(String catalog, String exportingTable, String keysComment, List<ResultSetRow> tuples, String fkTableName) throws SQLException {
        getResultsImpl(catalog, exportingTable, keysComment, tuples, fkTableName, true);
    }

    @Override // java.sql.DatabaseMetaData
    public String getExtraNameCharacters() throws SQLException {
        return "#@";
    }

    protected int[] getForeignKeyActions(String commentString) {
        int[] actions = {3, 3};
        int lastParenIndex = commentString.lastIndexOf(")");
        if (lastParenIndex != commentString.length() - 1) {
            String cascadeOptions = commentString.substring(lastParenIndex + 1).trim().toUpperCase(Locale.ENGLISH);
            actions[0] = getCascadeDeleteOption(cascadeOptions);
            actions[1] = getCascadeUpdateOption(cascadeOptions);
        }
        return actions;
    }

    @Override // java.sql.DatabaseMetaData
    public String getIdentifierQuoteString() throws SQLException {
        if (this.conn.supportsQuotedIdentifiers()) {
            return this.conn.useAnsiQuotedIdentifiers() ? SymbolConstants.QUOTES_SYMBOL : "`";
        }
        return SymbolConstants.SPACE_SYMBOL;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getImportedKeys(java.lang.String r9, java.lang.String r10, final java.lang.String r11) throws java.sql.SQLException {
        /*
            r8 = this;
            r0 = r11
            if (r0 != 0) goto L10
            java.lang.String r0 = "Table not specified."
            java.lang.String r1 = "S1009"
            r2 = r8
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)
            throw r0
        L10:
            r0 = r8
            com.mysql.jdbc.Field[] r0 = r0.createFkMetadataFields()
            r12 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r13 = r0
            r0 = r8
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            r1 = 3
            r2 = 23
            r3 = 0
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto L6d
            r0 = r8
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.sql.Statement r0 = r0.getMetadataSafeStatement()
            r14 = r0
            com.mysql.jdbc.DatabaseMetaData$5 r0 = new com.mysql.jdbc.DatabaseMetaData$5     // Catch: java.lang.Throwable -> L55
            r1 = r0
            r2 = r8
            r3 = r8
            r4 = r9
            com.mysql.jdbc.DatabaseMetaData$IteratorWithCleanup r3 = r3.getCatalogIterator(r4)     // Catch: java.lang.Throwable -> L55
            r4 = r11
            r5 = r14
            r6 = r13
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L55
            r0.doForAll()     // Catch: java.lang.Throwable -> L55
            r0 = jsr -> L5d
        L52:
            goto L6d
        L55:
            r15 = move-exception
            r0 = jsr -> L5d
        L5a:
            r1 = r15
            throw r1
        L5d:
            r16 = r0
            r0 = r14
            if (r0 == 0) goto L6b
            r0 = r14
            r0.close()
        L6b:
            ret r16
        L6d:
            r0 = r8
            r1 = r12
            r2 = r13
            java.sql.ResultSet r0 = r0.buildResultSet(r1, r2)
            r14 = r0
            r0 = r14
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getImportedKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    protected void getImportKeyResults(String catalog, String importingTable, String keysComment, List<ResultSetRow> tuples) throws SQLException {
        getResultsImpl(catalog, importingTable, keysComment, tuples, null, false);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getIndexInfo(java.lang.String r10, java.lang.String r11, final java.lang.String r12, final boolean r13, boolean r14) throws java.sql.SQLException {
        /*
            r9 = this;
            r0 = r9
            com.mysql.jdbc.Field[] r0 = r0.createIndexInfoFields()
            r15 = r0
            java.util.TreeMap r0 = new java.util.TreeMap
            r1 = r0
            r1.<init>()
            r16 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r17 = r0
            r0 = r9
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.sql.Statement r0 = r0.getMetadataSafeStatement()
            r18 = r0
            com.mysql.jdbc.DatabaseMetaData$6 r0 = new com.mysql.jdbc.DatabaseMetaData$6     // Catch: java.lang.Throwable -> L76
            r1 = r0
            r2 = r9
            r3 = r9
            r4 = r10
            com.mysql.jdbc.DatabaseMetaData$IteratorWithCleanup r3 = r3.getCatalogIterator(r4)     // Catch: java.lang.Throwable -> L76
            r4 = r12
            r5 = r18
            r6 = r13
            r7 = r16
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L76
            r0.doForAll()     // Catch: java.lang.Throwable -> L76
            r0 = r16
            java.util.Collection r0 = r0.values()     // Catch: java.lang.Throwable -> L76
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L76
            r19 = r0
        L48:
            r0 = r19
            boolean r0 = r0.hasNext()     // Catch: java.lang.Throwable -> L76
            if (r0 == 0) goto L62
            r0 = r17
            r1 = r19
            java.lang.Object r1 = r1.next()     // Catch: java.lang.Throwable -> L76
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L76
            goto L48
        L62:
            r0 = r9
            r1 = r15
            r2 = r17
            java.sql.ResultSet r0 = r0.buildResultSet(r1, r2)     // Catch: java.lang.Throwable -> L76
            r20 = r0
            r0 = r20
            r21 = r0
            r0 = jsr -> L7e
        L73:
            r1 = r21
            return r1
        L76:
            r22 = move-exception
            r0 = jsr -> L7e
        L7b:
            r1 = r22
            throw r1
        L7e:
            r23 = r0
            r0 = r18
            if (r0 == 0) goto L8c
            r0 = r18
            r0.close()
        L8c:
            ret r23
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean):java.sql.ResultSet");
    }

    protected Field[] createIndexInfoFields() {
        Field[] fields = new Field[13];
        fields[0] = new Field("", "TABLE_CAT", 1, 255);
        fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
        fields[2] = new Field("", "TABLE_NAME", 1, 255);
        fields[3] = new Field("", "NON_UNIQUE", 16, 4);
        fields[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
        fields[5] = new Field("", "INDEX_NAME", 1, 32);
        fields[6] = new Field("", "TYPE", 5, 32);
        fields[7] = new Field("", "ORDINAL_POSITION", 5, 5);
        fields[8] = new Field("", "COLUMN_NAME", 1, 32);
        fields[9] = new Field("", "ASC_OR_DESC", 1, 1);
        if (Util.isJdbc42()) {
            fields[10] = new Field("", "CARDINALITY", -5, 20);
            fields[11] = new Field("", "PAGES", -5, 20);
        } else {
            fields[10] = new Field("", "CARDINALITY", 4, 20);
            fields[11] = new Field("", "PAGES", 4, 10);
        }
        fields[12] = new Field("", "FILTER_CONDITION", 1, 32);
        return fields;
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMajorVersion() throws SQLException {
        return 4;
    }

    @Override // java.sql.DatabaseMetaData
    public int getJDBCMinorVersion() throws SQLException {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 16777208;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCatalogNameLength() throws SQLException {
        return 32;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCharLiteralLength() throws SQLException {
        return 16777208;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnNameLength() throws SQLException {
        return 64;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 64;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInIndex() throws SQLException {
        return 16;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 64;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInSelect() throws SQLException {
        return 256;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxColumnsInTable() throws SQLException {
        return 512;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxConnections() throws SQLException {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxCursorNameLength() throws SQLException {
        return 64;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxIndexLength() throws SQLException {
        return 256;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxProcedureNameLength() throws SQLException {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxRowSize() throws SQLException {
        return AbstractChunk.ARRAY_MAX_SIZE;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxSchemaNameLength() throws SQLException {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatementLength() throws SQLException {
        return MysqlIO.getMaxBuf() - 4;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxStatements() throws SQLException {
        return 0;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTableNameLength() throws SQLException {
        return 64;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxTablesInSelect() throws SQLException {
        return 256;
    }

    @Override // java.sql.DatabaseMetaData
    public int getMaxUserNameLength() throws SQLException {
        return 16;
    }

    @Override // java.sql.DatabaseMetaData
    public String getNumericFunctions() throws SQLException {
        return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getPrimaryKeys(java.lang.String r10, java.lang.String r11, final java.lang.String r12) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 218
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        Field[] fields = createProcedureColumnsFields();
        return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, true);
    }

    protected Field[] createProcedureColumnsFields() {
        Field[] fields = {new Field("", "PROCEDURE_CAT", 1, 512), new Field("", "PROCEDURE_SCHEM", 1, 512), new Field("", "PROCEDURE_NAME", 1, 512), new Field("", "COLUMN_NAME", 1, 512), new Field("", "COLUMN_TYPE", 1, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 1, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 1, 512), new Field("", "COLUMN_DEF", 1, 512), new Field("", "SQL_DATA_TYPE", 4, 12), new Field("", "SQL_DATETIME_SUB", 4, 12), new Field("", "CHAR_OCTET_LENGTH", 4, 12), new Field("", "ORDINAL_POSITION", 4, 12), new Field("", "IS_NULLABLE", 1, 512), new Field("", "SPECIFIC_NAME", 1, 512)};
        return fields;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00ef, code lost:
    
        throw r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00fa, code lost:
    
        r18.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010c, code lost:
    
        if (0 == 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0111, code lost:
    
        throw null;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00fa A[Catch: SQLException -> 0x0104, TRY_ENTER, TRY_LEAVE, TryCatch #0 {SQLException -> 0x0104, blocks: (B:35:0x00fa, B:67:0x00fa, B:6:0x001b, B:8:0x0025, B:15:0x0076, B:16:0x008b, B:18:0x0095, B:20:0x00bd, B:22:0x00c6, B:21:0x00c3, B:26:0x00dd, B:12:0x0038, B:14:0x005c), top: B:73:0x0010, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.sql.ResultSet getProcedureOrFunctionColumns(com.mysql.jdbc.Field[] r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, boolean r15, boolean r16) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 474
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getProcedureOrFunctionColumns(com.mysql.jdbc.Field[], java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean):java.sql.ResultSet");
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        Field[] fields = createFieldMetadataForGetProcedures();
        return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, true);
    }

    protected Field[] createFieldMetadataForGetProcedures() {
        Field[] fields = {new Field("", "PROCEDURE_CAT", 1, 255), new Field("", "PROCEDURE_SCHEM", 1, 255), new Field("", "PROCEDURE_NAME", 1, 255), new Field("", "reserved1", 1, 0), new Field("", "reserved2", 1, 0), new Field("", "reserved3", 1, 0), new Field("", "REMARKS", 1, 255), new Field("", "PROCEDURE_TYPE", 5, 6), new Field("", "SPECIFIC_NAME", 1, 255)};
        return fields;
    }

    protected ResultSet getProceduresAndOrFunctions(final Field[] fields, String catalog, String schemaPattern, String procedureNamePattern, final boolean returnProcedures, final boolean returnFunctions) throws SQLException {
        if (procedureNamePattern == null || procedureNamePattern.length() == 0) {
            if (this.conn.getNullNamePatternMatchesAll()) {
                procedureNamePattern = QuickTargetSourceCreator.PREFIX_THREAD_LOCAL;
            } else {
                throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
        }
        ArrayList<ResultSetRow> arrayList = new ArrayList<>();
        if (supportsStoredProcedures()) {
            final String procNamePattern = procedureNamePattern;
            final List<ComparableWrapper<String, ResultSetRow>> procedureRowsToSort = new ArrayList<>();
            new IterateBlock<String>(getCatalogIterator(catalog)) { // from class: com.mysql.jdbc.DatabaseMetaData.8
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: Code restructure failed: missing block: B:44:0x018a, code lost:
                
                    throw r18;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:63:0x0194, code lost:
                
                    r12.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:65:0x01a5, code lost:
                
                    if (r15 == null) goto L55;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:66:0x01a8, code lost:
                
                    r15.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:68:0x01ba, code lost:
                
                    if (0 == 0) goto L59;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:70:0x01bf, code lost:
                
                    throw null;
                 */
                /* JADX WARN: Removed duplicated region for block: B:40:0x0180  */
                /* JADX WARN: Removed duplicated region for block: B:47:0x0194 A[Catch: SQLException -> 0x019d, TRY_ENTER, TRY_LEAVE, TryCatch #0 {SQLException -> 0x019d, blocks: (B:47:0x0194, B:63:0x0194, B:15:0x0061, B:17:0x0070, B:18:0x0075, B:20:0x008b, B:21:0x009a, B:23:0x00ac, B:24:0x00bf, B:26:0x00c6, B:29:0x00e1, B:33:0x00f8, B:35:0x0101, B:36:0x013d, B:38:0x0144, B:19:0x0081), top: B:72:0x005d, inners: #1 }] */
                /* JADX WARN: Removed duplicated region for block: B:52:0x01a8 A[Catch: SQLException -> 0x01b2, TRY_ENTER, TRY_LEAVE, TryCatch #3 {SQLException -> 0x01b2, blocks: (B:52:0x01a8, B:66:0x01a8), top: B:76:0x005d }] */
                /* JADX WARN: Removed duplicated region for block: B:57:0x01bd  */
                @Override // com.mysql.jdbc.IterateBlock
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void forEach(java.lang.String r10) throws java.sql.SQLException {
                    /*
                        Method dump skipped, instructions count: 451
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.AnonymousClass8.forEach(java.lang.String):void");
                }
            }.doForAll();
            Collections.sort(procedureRowsToSort);
            for (ComparableWrapper<String, ResultSetRow> procRow : procedureRowsToSort) {
                arrayList.add(procRow.getValue());
            }
        }
        return buildResultSet(fields, arrayList);
    }

    @Override // java.sql.DatabaseMetaData
    public String getProcedureTerm() throws SQLException {
        return "PROCEDURE";
    }

    @Override // java.sql.DatabaseMetaData
    public int getResultSetHoldability() throws SQLException {
        return 1;
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [byte[], byte[][]] */
    private void getResultsImpl(String catalog, String table, String keysComment, List<ResultSetRow> tuples, String fkTableName, boolean isExport) throws SQLException {
        LocalAndReferencedColumns parsedInfo = parseTableStatusIntoLocalAndReferencedColumns(keysComment);
        if (isExport && !parsedInfo.referencedTable.equals(table)) {
            return;
        }
        if (parsedInfo.localColumnsList.size() != parsedInfo.referencedColumnsList.size()) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, number of local and referenced columns is not the same.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        Iterator<String> localColumnNames = parsedInfo.localColumnsList.iterator();
        Iterator<String> referColumnNames = parsedInfo.referencedColumnsList.iterator();
        int keySeqIndex = 1;
        while (localColumnNames.hasNext()) {
            ?? r0 = new byte[14];
            String lColumnName = StringUtils.unQuoteIdentifier(localColumnNames.next(), this.quotedId);
            String rColumnName = StringUtils.unQuoteIdentifier(referColumnNames.next(), this.quotedId);
            r0[4] = catalog == null ? new byte[0] : s2b(catalog);
            r0[5] = 0;
            r0[6] = s2b(isExport ? fkTableName : table);
            r0[7] = s2b(lColumnName);
            r0[0] = s2b(parsedInfo.referencedCatalog);
            r0[1] = 0;
            r0[2] = s2b(isExport ? table : parsedInfo.referencedTable);
            r0[3] = s2b(rColumnName);
            int i = keySeqIndex;
            keySeqIndex++;
            r0[8] = s2b(Integer.toString(i));
            int[] actions = getForeignKeyActions(keysComment);
            r0[9] = s2b(Integer.toString(actions[1]));
            r0[10] = s2b(Integer.toString(actions[0]));
            r0[11] = s2b(parsedInfo.constraintName);
            r0[12] = 0;
            r0[13] = s2b(Integer.toString(7));
            tuples.add(new ByteArrayRow(r0, getExceptionInterceptor()));
        }
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSchemas() throws SQLException {
        Field[] fields = {new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_CATALOG", 1, 0)};
        ArrayList<ResultSetRow> tuples = new ArrayList<>();
        ResultSet results = buildResultSet(fields, tuples);
        return results;
    }

    @Override // java.sql.DatabaseMetaData
    public String getSchemaTerm() throws SQLException {
        return "";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSearchStringEscape() throws SQLException {
        return "\\";
    }

    @Override // java.sql.DatabaseMetaData
    public String getSQLKeywords() throws SQLException {
        if (mysqlKeywords != null) {
            return mysqlKeywords;
        }
        synchronized (DatabaseMetaData.class) {
            if (mysqlKeywords != null) {
                return mysqlKeywords;
            }
            Set<String> mysqlKeywordSet = new TreeSet<>();
            StringBuilder mysqlKeywordsBuffer = new StringBuilder();
            Collections.addAll(mysqlKeywordSet, MYSQL_KEYWORDS);
            mysqlKeywordSet.removeAll(Arrays.asList(Util.isJdbc4() ? SQL2003_KEYWORDS : SQL92_KEYWORDS));
            for (String keyword : mysqlKeywordSet) {
                mysqlKeywordsBuffer.append(",").append(keyword);
            }
            mysqlKeywords = mysqlKeywordsBuffer.substring(1);
            return mysqlKeywords;
        }
    }

    @Override // java.sql.DatabaseMetaData
    public int getSQLStateType() throws SQLException {
        if (this.conn.versionMeetsMinimum(4, 1, 0) || this.conn.getUseSqlStateCodes()) {
            return 2;
        }
        return 1;
    }

    @Override // java.sql.DatabaseMetaData
    public String getStringFunctions() throws SQLException {
        return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSuperTables(String arg0, String arg1, String arg2) throws SQLException {
        Field[] fields = {new Field("", "TABLE_CAT", 1, 32), new Field("", "TABLE_SCHEM", 1, 32), new Field("", "TABLE_NAME", 1, 32), new Field("", "SUPERTABLE_NAME", 1, 32)};
        return buildResultSet(fields, new ArrayList<>());
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSuperTypes(String arg0, String arg1, String arg2) throws SQLException {
        Field[] fields = {new Field("", "TYPE_CAT", 1, 32), new Field("", "TYPE_SCHEM", 1, 32), new Field("", "TYPE_NAME", 1, 32), new Field("", "SUPERTYPE_CAT", 1, 32), new Field("", "SUPERTYPE_SCHEM", 1, 32), new Field("", "SUPERTYPE_NAME", 1, 32)};
        return buildResultSet(fields, new ArrayList<>());
    }

    @Override // java.sql.DatabaseMetaData
    public String getSystemFunctions() throws SQLException {
        return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
    }

    protected String getTableNameWithCase(String table) {
        String tableNameWithCase = this.conn.lowerCaseTableNames() ? table.toLowerCase() : table;
        return tableNameWithCase;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Type inference failed for: r0v69, types: [byte[], byte[][]] */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getTablePrivileges(java.lang.String r10, java.lang.String r11, java.lang.String r12) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 661
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getTablePrivileges(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getTables(java.lang.String r10, java.lang.String r11, java.lang.String r12, final java.lang.String[] r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[]):java.sql.ResultSet");
    }

    /* renamed from: com.mysql.jdbc.DatabaseMetaData$11, reason: invalid class name */
    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/DatabaseMetaData$11.class */
    static /* synthetic */ class AnonymousClass11 {
        static final /* synthetic */ int[] $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType = new int[TableType.values().length];

        static {
            try {
                $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[TableType.TABLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[TableType.VIEW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[TableType.SYSTEM_TABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[TableType.SYSTEM_VIEW.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[TableType.LOCAL_TEMPORARY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    protected Field[] createTablesFields() {
        Field[] fields = {new Field("", "TABLE_CAT", 12, 255), new Field("", "TABLE_SCHEM", 12, 0), new Field("", "TABLE_NAME", 12, 255), new Field("", "TABLE_TYPE", 12, 5), new Field("", "REMARKS", 12, 0), new Field("", "TYPE_CAT", 12, 0), new Field("", "TYPE_SCHEM", 12, 0), new Field("", "TYPE_NAME", 12, 0), new Field("", "SELF_REFERENCING_COL_NAME", 12, 0), new Field("", "REF_GENERATION", 12, 0)};
        return fields;
    }

    /* JADX WARN: Type inference failed for: r3v11, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r3v3, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r3v5, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r3v7, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r3v9, types: [byte[], byte[][]] */
    @Override // java.sql.DatabaseMetaData
    public ResultSet getTableTypes() throws SQLException {
        ArrayList<ResultSetRow> tuples = new ArrayList<>();
        Field[] fields = {new Field("", "TABLE_TYPE", 12, 256)};
        boolean minVersion5_0_1 = this.conn.versionMeetsMinimum(5, 0, 1);
        tuples.add(new ByteArrayRow(new byte[]{TableType.LOCAL_TEMPORARY.asBytes()}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{TableType.SYSTEM_TABLE.asBytes()}, getExceptionInterceptor()));
        if (minVersion5_0_1) {
            tuples.add(new ByteArrayRow(new byte[]{TableType.SYSTEM_VIEW.asBytes()}, getExceptionInterceptor()));
        }
        tuples.add(new ByteArrayRow(new byte[]{TableType.TABLE.asBytes()}, getExceptionInterceptor()));
        if (minVersion5_0_1) {
            tuples.add(new ByteArrayRow(new byte[]{TableType.VIEW.asBytes()}, getExceptionInterceptor()));
        }
        return buildResultSet(fields, tuples);
    }

    @Override // java.sql.DatabaseMetaData
    public String getTimeDateFunctions() throws SQLException {
        return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
    }

    /* JADX WARN: Type inference failed for: r0v112, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v134, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v156, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v178, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v200, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v222, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v24, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v244, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v266, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v288, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v310, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v332, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v354, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v376, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v398, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v420, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v446, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v46, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v468, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v490, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v512, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v534, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v556, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v578, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v600, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v622, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v644, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v666, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v68, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v688, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v710, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v732, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v754, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v776, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v798, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v820, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v842, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v864, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v886, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v90, types: [byte[], byte[][]] */
    @Override // java.sql.DatabaseMetaData
    public ResultSet getTypeInfo() throws SQLException {
        Field[] fields = {new Field("", "TYPE_NAME", 1, 32), new Field("", "DATA_TYPE", 4, 5), new Field("", "PRECISION", 4, 10), new Field("", "LITERAL_PREFIX", 1, 4), new Field("", "LITERAL_SUFFIX", 1, 4), new Field("", "CREATE_PARAMS", 1, 32), new Field("", "NULLABLE", 5, 5), new Field("", "CASE_SENSITIVE", 16, 3), new Field("", "SEARCHABLE", 5, 3), new Field("", "UNSIGNED_ATTRIBUTE", 16, 3), new Field("", "FIXED_PREC_SCALE", 16, 3), new Field("", "AUTO_INCREMENT", 16, 3), new Field("", "LOCAL_TYPE_NAME", 1, 32), new Field("", "MINIMUM_SCALE", 5, 5), new Field("", "MAXIMUM_SCALE", 5, 5), new Field("", "SQL_DATA_TYPE", 4, 10), new Field("", "SQL_DATETIME_SUB", 4, 10), new Field("", "NUM_PREC_RADIX", 4, 10)};
        ArrayList<ResultSetRow> tuples = new ArrayList<>();
        tuples.add(new ByteArrayRow(new byte[]{s2b("BIT"), Integer.toString(-7).getBytes(), s2b("1"), s2b(""), s2b(""), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("BIT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("BOOL"), Integer.toString(-7).getBytes(), s2b("1"), s2b(""), s2b(""), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("BOOL"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TINYINT"), Integer.toString(-6).getBytes(), s2b("3"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("TINYINT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TINYINT UNSIGNED"), Integer.toString(-6).getBytes(), s2b("3"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("TINYINT UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("BIGINT"), Integer.toString(-5).getBytes(), s2b("19"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("BIGINT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("BIGINT UNSIGNED"), Integer.toString(-5).getBytes(), s2b("20"), s2b(""), s2b(""), s2b("[(M)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("BIGINT UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("LONG VARBINARY"), Integer.toString(-4).getBytes(), s2b("16777215"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("LONG VARBINARY"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("MEDIUMBLOB"), Integer.toString(-4).getBytes(), s2b("16777215"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("MEDIUMBLOB"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("LONGBLOB"), Integer.toString(-4).getBytes(), Integer.toString(Integer.MAX_VALUE).getBytes(), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("LONGBLOB"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("BLOB"), Integer.toString(-4).getBytes(), s2b("65535"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("BLOB"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TINYBLOB"), Integer.toString(-4).getBytes(), s2b("255"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("TINYBLOB"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        ?? r0 = new byte[18];
        r0[0] = s2b("VARBINARY");
        r0[1] = Integer.toString(-3).getBytes();
        r0[2] = s2b(this.conn.versionMeetsMinimum(5, 0, 3) ? "65535" : "255");
        r0[3] = s2b("'");
        r0[4] = s2b("'");
        r0[5] = s2b("(M)");
        r0[6] = Integer.toString(1).getBytes();
        r0[7] = s2b("true");
        r0[8] = Integer.toString(3).getBytes();
        r0[9] = s2b("false");
        r0[10] = s2b("false");
        r0[11] = s2b("false");
        r0[12] = s2b("VARBINARY");
        r0[13] = s2b("0");
        r0[14] = s2b("0");
        r0[15] = s2b("0");
        r0[16] = s2b("0");
        r0[17] = s2b("10");
        tuples.add(new ByteArrayRow(r0, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("BINARY"), Integer.toString(-2).getBytes(), s2b("255"), s2b("'"), s2b("'"), s2b("(M)"), Integer.toString(1).getBytes(), s2b("true"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("BINARY"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("LONG VARCHAR"), Integer.toString(-1).getBytes(), s2b("16777215"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("LONG VARCHAR"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("MEDIUMTEXT"), Integer.toString(-1).getBytes(), s2b("16777215"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("MEDIUMTEXT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("LONGTEXT"), Integer.toString(-1).getBytes(), Integer.toString(Integer.MAX_VALUE).getBytes(), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("LONGTEXT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TEXT"), Integer.toString(-1).getBytes(), s2b("65535"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("TEXT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TINYTEXT"), Integer.toString(-1).getBytes(), s2b("255"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("TINYTEXT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("CHAR"), Integer.toString(1).getBytes(), s2b("255"), s2b("'"), s2b("'"), s2b("(M)"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("CHAR"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        int decimalPrecision = 254;
        if (this.conn.versionMeetsMinimum(5, 0, 3)) {
            if (this.conn.versionMeetsMinimum(5, 0, 6)) {
                decimalPrecision = 65;
            } else {
                decimalPrecision = 64;
            }
        }
        tuples.add(new ByteArrayRow(new byte[]{s2b("NUMERIC"), Integer.toString(2).getBytes(), s2b(String.valueOf(decimalPrecision)), s2b(""), s2b(""), s2b("[(M[,D])] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("NUMERIC"), s2b("-308"), s2b("308"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("DECIMAL"), Integer.toString(3).getBytes(), s2b(String.valueOf(decimalPrecision)), s2b(""), s2b(""), s2b("[(M[,D])] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("DECIMAL"), s2b("-308"), s2b("308"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("INTEGER"), Integer.toString(4).getBytes(), s2b("10"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("INTEGER"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("INTEGER UNSIGNED"), Integer.toString(4).getBytes(), s2b("10"), s2b(""), s2b(""), s2b("[(M)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("INTEGER UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("INT"), Integer.toString(4).getBytes(), s2b("10"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("INT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("INT UNSIGNED"), Integer.toString(4).getBytes(), s2b("10"), s2b(""), s2b(""), s2b("[(M)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("INT UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("MEDIUMINT"), Integer.toString(4).getBytes(), s2b("7"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("MEDIUMINT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("MEDIUMINT UNSIGNED"), Integer.toString(4).getBytes(), s2b("8"), s2b(""), s2b(""), s2b("[(M)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("MEDIUMINT UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("SMALLINT"), Integer.toString(5).getBytes(), s2b("5"), s2b(""), s2b(""), s2b("[(M)] [UNSIGNED] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("SMALLINT"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("SMALLINT UNSIGNED"), Integer.toString(5).getBytes(), s2b("5"), s2b(""), s2b(""), s2b("[(M)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("true"), s2b("false"), s2b("true"), s2b("SMALLINT UNSIGNED"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("FLOAT"), Integer.toString(7).getBytes(), s2b("10"), s2b(""), s2b(""), s2b("[(M,D)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("FLOAT"), s2b("-38"), s2b("38"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("DOUBLE"), Integer.toString(8).getBytes(), s2b("17"), s2b(""), s2b(""), s2b("[(M,D)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("DOUBLE"), s2b("-308"), s2b("308"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("DOUBLE PRECISION"), Integer.toString(8).getBytes(), s2b("17"), s2b(""), s2b(""), s2b("[(M,D)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("DOUBLE PRECISION"), s2b("-308"), s2b("308"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("REAL"), Integer.toString(8).getBytes(), s2b("17"), s2b(""), s2b(""), s2b("[(M,D)] [ZEROFILL]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("true"), s2b("REAL"), s2b("-308"), s2b("308"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        ?? r02 = new byte[18];
        r02[0] = s2b("VARCHAR");
        r02[1] = Integer.toString(12).getBytes();
        r02[2] = s2b(this.conn.versionMeetsMinimum(5, 0, 3) ? "65535" : "255");
        r02[3] = s2b("'");
        r02[4] = s2b("'");
        r02[5] = s2b("(M)");
        r02[6] = Integer.toString(1).getBytes();
        r02[7] = s2b("false");
        r02[8] = Integer.toString(3).getBytes();
        r02[9] = s2b("false");
        r02[10] = s2b("false");
        r02[11] = s2b("false");
        r02[12] = s2b("VARCHAR");
        r02[13] = s2b("0");
        r02[14] = s2b("0");
        r02[15] = s2b("0");
        r02[16] = s2b("0");
        r02[17] = s2b("10");
        tuples.add(new ByteArrayRow(r02, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("ENUM"), Integer.toString(12).getBytes(), s2b("65535"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("ENUM"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("SET"), Integer.toString(12).getBytes(), s2b("64"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("SET"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("DATE"), Integer.toString(91).getBytes(), s2b("0"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("DATE"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TIME"), Integer.toString(92).getBytes(), s2b("0"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("TIME"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("DATETIME"), Integer.toString(93).getBytes(), s2b("0"), s2b("'"), s2b("'"), s2b(""), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("DATETIME"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        tuples.add(new ByteArrayRow(new byte[]{s2b("TIMESTAMP"), Integer.toString(93).getBytes(), s2b("0"), s2b("'"), s2b("'"), s2b("[(M)]"), Integer.toString(1).getBytes(), s2b("false"), Integer.toString(3).getBytes(), s2b("false"), s2b("false"), s2b("false"), s2b("TIMESTAMP"), s2b("0"), s2b("0"), s2b("0"), s2b("0"), s2b("10")}, getExceptionInterceptor()));
        return buildResultSet(fields, tuples);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        Field[] fields = {new Field("", "TYPE_CAT", 12, 32), new Field("", "TYPE_SCHEM", 12, 32), new Field("", "TYPE_NAME", 12, 32), new Field("", "CLASS_NAME", 12, 32), new Field("", "DATA_TYPE", 4, 10), new Field("", "REMARKS", 12, 32), new Field("", "BASE_TYPE", 5, 10)};
        ArrayList<ResultSetRow> tuples = new ArrayList<>();
        return buildResultSet(fields, tuples);
    }

    @Override // java.sql.DatabaseMetaData
    public String getURL() throws SQLException {
        return this.conn.getURL();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x003f, code lost:
    
        throw r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0043, code lost:
    
        if (r5 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0046, code lost:
    
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0059, code lost:
    
        if (r4 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x005c, code lost:
    
        r4.close();
     */
    @Override // java.sql.DatabaseMetaData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getUserName() throws com.mysql.jdbc.AssertionFailedException, java.sql.SQLException {
        /*
            r3 = this;
            r0 = r3
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            boolean r0 = r0.getUseHostsInPrivileges()
            if (r0 == 0) goto L70
            r0 = 0
            r4 = r0
            r0 = 0
            r5 = r0
            r0 = r3
            com.mysql.jdbc.MySQLConnection r0 = r0.conn     // Catch: java.lang.Throwable -> L38
            java.sql.Statement r0 = r0.getMetadataSafeStatement()     // Catch: java.lang.Throwable -> L38
            r4 = r0
            r0 = r4
            java.lang.String r1 = "SELECT USER()"
            java.sql.ResultSet r0 = r0.executeQuery(r1)     // Catch: java.lang.Throwable -> L38
            r5 = r0
            r0 = r5
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L38
            r0 = r5
            r1 = 1
            java.lang.String r0 = r0.getString(r1)     // Catch: java.lang.Throwable -> L38
            r6 = r0
            r0 = jsr -> L40
        L36:
            r1 = r6
            return r1
        L38:
            r7 = move-exception
            r0 = jsr -> L40
        L3d:
            r1 = r7
            throw r1
        L40:
            r8 = r0
            r0 = r5
            if (r0 == 0) goto L58
            r0 = r5
            r0.close()     // Catch: java.lang.Exception -> L4f
            goto L56
        L4f:
            r9 = move-exception
            r0 = r9
            com.mysql.jdbc.AssertionFailedException.shouldNotHappen(r0)
        L56:
            r0 = 0
            r5 = r0
        L58:
            r0 = r4
            if (r0 == 0) goto L6e
            r0 = r4
            r0.close()     // Catch: java.lang.Exception -> L65
            goto L6c
        L65:
            r9 = move-exception
            r0 = r9
            com.mysql.jdbc.AssertionFailedException.shouldNotHappen(r0)
        L6c:
            r0 = 0
            r4 = r0
        L6e:
            ret r8
        L70:
            r0 = r3
            com.mysql.jdbc.MySQLConnection r0 = r0.conn
            java.lang.String r0 = r0.getUser()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getUserName():java.lang.String");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.DatabaseMetaData
    public java.sql.ResultSet getVersionColumns(java.lang.String r10, java.lang.String r11, final java.lang.String r12) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.DatabaseMetaData.getVersionColumns(java.lang.String, java.lang.String, java.lang.String):java.sql.ResultSet");
    }

    @Override // java.sql.DatabaseMetaData
    public boolean insertsAreDetected(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isCatalogAtStart() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean locatorsUpdateCopy() throws SQLException {
        return !this.conn.getEmulateLocators();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedAtStart() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 0, 2) && !this.conn.versionMeetsMinimum(4, 0, 11);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedHigh() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean nullsAreSortedLow() throws SQLException {
        return !nullsAreSortedHigh();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    protected LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String keysComment) throws SQLException {
        int indexOfOpenParenLocalColumns = StringUtils.indexOfIgnoreCase(0, keysComment, "(", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
        if (indexOfOpenParenLocalColumns == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        String constraintName = StringUtils.unQuoteIdentifier(keysComment.substring(0, indexOfOpenParenLocalColumns).trim(), this.quotedId);
        String keysCommentTrimmed = keysComment.substring(indexOfOpenParenLocalColumns, keysComment.length()).trim();
        int indexOfCloseParenLocalColumns = StringUtils.indexOfIgnoreCase(0, keysCommentTrimmed, ")", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
        if (indexOfCloseParenLocalColumns == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        String localColumnNamesString = keysCommentTrimmed.substring(1, indexOfCloseParenLocalColumns);
        int indexOfRefer = StringUtils.indexOfIgnoreCase(0, keysCommentTrimmed, "REFER ", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
        if (indexOfRefer == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        int indexOfOpenParenReferCol = StringUtils.indexOfIgnoreCase(indexOfRefer, keysCommentTrimmed, "(", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__MRK_COM_WS);
        if (indexOfOpenParenReferCol == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        String referCatalogTableString = keysCommentTrimmed.substring(indexOfRefer + "REFER ".length(), indexOfOpenParenReferCol);
        int indexOfSlash = StringUtils.indexOfIgnoreCase(0, referCatalogTableString, "/", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__MRK_COM_WS);
        if (indexOfSlash == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        String referCatalog = StringUtils.unQuoteIdentifier(referCatalogTableString.substring(0, indexOfSlash), this.quotedId);
        String referTable = StringUtils.unQuoteIdentifier(referCatalogTableString.substring(indexOfSlash + 1).trim(), this.quotedId);
        int indexOfCloseParenRefer = StringUtils.indexOfIgnoreCase(indexOfOpenParenReferCol, keysCommentTrimmed, ")", this.quotedId, this.quotedId, StringUtils.SEARCH_MODE__ALL);
        if (indexOfCloseParenRefer == -1) {
            throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        String referColumnNamesString = keysCommentTrimmed.substring(indexOfOpenParenReferCol + 1, indexOfCloseParenRefer);
        List<String> referColumnsList = StringUtils.split(referColumnNamesString, ",", this.quotedId, this.quotedId, false);
        List<String> localColumnsList = StringUtils.split(localColumnNamesString, ",", this.quotedId, this.quotedId, false);
        return new LocalAndReferencedColumns(localColumnsList, referColumnsList, constraintName, referCatalog, referTable);
    }

    protected byte[] s2b(String s) throws SQLException {
        if (s == null) {
            return null;
        }
        return StringUtils.getBytes(s, this.conn.getCharacterSetMetadata(), this.conn.getServerCharset(), this.conn.parserKnowsUnicode(), this.conn, getExceptionInterceptor());
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return this.conn.storesLowerCaseTableName();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return this.conn.storesLowerCaseTableName();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return !this.conn.storesLowerCaseTableName();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return !this.conn.storesLowerCaseTableName();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92FullSQL() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsBatchUpdates() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return this.conn.versionMeetsMinimum(3, 22, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return this.conn.versionMeetsMinimum(3, 22, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return this.conn.versionMeetsMinimum(3, 22, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return this.conn.versionMeetsMinimum(3, 22, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return this.conn.versionMeetsMinimum(3, 22, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsColumnAliasing() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        switch (fromType) {
            case -7:
                return false;
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                switch (toType) {
                    case -6:
                    case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 12:
                        return true;
                    case 0:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return false;
                }
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 1:
            case 12:
                switch (toType) {
                    case -6:
                    case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 12:
                    case 91:
                    case 92:
                    case 93:
                    case MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE /* 1111 */:
                        return true;
                    default:
                        return false;
                }
            case 0:
                return false;
            case 91:
                switch (toType) {
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 12:
                        return true;
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return false;
                }
            case 92:
                switch (toType) {
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 12:
                        return true;
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return false;
                }
            case 93:
                switch (toType) {
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 12:
                    case 91:
                    case 92:
                        return true;
                    default:
                        return false;
                }
            case MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE /* 1111 */:
                switch (toType) {
                    case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                    case -3:
                    case -2:
                    case -1:
                    case 1:
                    case 12:
                        return true;
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsFullOuterJoins() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupBy() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsGroupByUnrelated() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        if (!this.conn.getOverrideSupportsIntegrityEnhancementFacility()) {
            return false;
        }
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLikeEscapeClause() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return !this.conn.lowerCaseTableNames();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return !this.conn.lowerCaseTableNames();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleOpenResults() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleResultSets() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsMultipleTransactions() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNamedParameters() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsNonNullableColumns() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOrderByUnrelated() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsOuterJoins() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedDelete() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        switch (type) {
            case 1003:
                if (concurrency == 1007 || concurrency == 1008) {
                    return true;
                }
                throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            case MysqlErrorNumbers.ER_CANT_CREATE_FILE /* 1004 */:
                if (concurrency == 1007 || concurrency == 1008) {
                    return true;
                }
                throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            case 1005:
                return false;
            default:
                throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        return holdability == 1;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsResultSetType(int type) throws SQLException {
        return type == 1004;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSavepoints() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 0, 14) || this.conn.versionMeetsMinimum(4, 1, 1);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSelectForUpdate() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 0, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStatementPooling() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredProcedures() throws SQLException {
        return this.conn.versionMeetsMinimum(5, 0, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInExists() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInIns() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 1, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        if (this.conn.supportsIsolationLevel()) {
            switch (level) {
                case 1:
                case 2:
                case 4:
                case 8:
                    return true;
                case 3:
                case 5:
                case 6:
                case 7:
                default:
                    return false;
            }
        }
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsTransactions() throws SQLException {
        return this.conn.supportsTransactions();
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnion() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 0, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsUnionAll() throws SQLException {
        return this.conn.versionMeetsMinimum(4, 0, 0);
    }

    @Override // java.sql.DatabaseMetaData
    public boolean updatesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getClientInfoProperties() throws SQLException {
        Field[] fields = {new Field("", "NAME", 12, 255), new Field("", "MAX_LEN", 4, 10), new Field("", "DEFAULT_VALUE", 12, 255), new Field("", "DESCRIPTION", 12, 255)};
        return buildResultSet(fields, new ArrayList(), this.conn);
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        Field[] fields = createFunctionColumnsFields();
        return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, functionNamePattern, columnNamePattern, false, true);
    }

    protected Field[] createFunctionColumnsFields() {
        Field[] fields = {new Field("", "FUNCTION_CAT", 12, 512), new Field("", "FUNCTION_SCHEM", 12, 512), new Field("", "FUNCTION_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "COLUMN_TYPE", 12, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 12, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 32), new Field("", "ORDINAL_POSITION", 4, 32), new Field("", "IS_NULLABLE", 12, 12), new Field("", "SPECIFIC_NAME", 12, 64)};
        return fields;
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        Field[] fields = {new Field("", "FUNCTION_CAT", 1, 255), new Field("", "FUNCTION_SCHEM", 1, 255), new Field("", "FUNCTION_NAME", 1, 255), new Field("", "REMARKS", 1, 255), new Field("", "FUNCTION_TYPE", 5, 6), new Field("", "SPECIFIC_NAME", 1, 255)};
        return getProceduresAndOrFunctions(fields, catalog, schemaPattern, functionNamePattern, false, true);
    }

    public boolean providesQueryObjectGenerator() throws SQLException {
        return false;
    }

    @Override // java.sql.DatabaseMetaData
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        Field[] fields = {new Field("", "TABLE_SCHEM", 12, 255), new Field("", "TABLE_CATALOG", 12, 255)};
        return buildResultSet(fields, new ArrayList<>());
    }

    @Override // java.sql.DatabaseMetaData
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return true;
    }

    protected java.sql.PreparedStatement prepareMetaDataSafeStatement(String sql) throws SQLException {
        java.sql.PreparedStatement pStmt = this.conn.clientPrepareStatement(sql);
        if (pStmt.getMaxRows() != 0) {
            pStmt.setMaxRows(0);
        }
        ((Statement) pStmt).setHoldResultsOpenOverClose(true);
        return pStmt;
    }

    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        Field[] fields = {new Field("", "TABLE_CAT", 12, 512), new Field("", "TABLE_SCHEM", 12, 512), new Field("", "TABLE_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "DATA_TYPE", 4, 12), new Field("", "COLUMN_SIZE", 4, 12), new Field("", "DECIMAL_DIGITS", 4, 12), new Field("", "NUM_PREC_RADIX", 4, 12), new Field("", "COLUMN_USAGE", 12, 512), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 12), new Field("", "IS_NULLABLE", 12, 512)};
        return buildResultSet(fields, new ArrayList<>());
    }

    public boolean generatedKeyAlwaysReturned() throws SQLException {
        return true;
    }
}
