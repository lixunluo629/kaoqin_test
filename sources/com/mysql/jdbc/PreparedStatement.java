package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.DateFormatConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Array;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import org.apache.poi.poifs.common.POIFSConstants;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement.class */
public class PreparedStatement extends StatementImpl implements java.sql.PreparedStatement {
    private static final Constructor<?> JDBC_4_PSTMT_2_ARG_CTOR;
    private static final Constructor<?> JDBC_4_PSTMT_3_ARG_CTOR;
    private static final Constructor<?> JDBC_4_PSTMT_4_ARG_CTOR;
    private static final byte[] HEX_DIGITS;
    protected boolean batchHasPlainStatements;
    private java.sql.DatabaseMetaData dbmd;
    protected char firstCharOfStmt;
    protected boolean isLoadDataQuery;
    protected boolean[] isNull;
    private boolean[] isStream;
    protected int numberOfExecutions;
    protected String originalSql;
    protected int parameterCount;
    protected MysqlParameterMetadata parameterMetaData;
    private InputStream[] parameterStreams;
    private byte[][] parameterValues;
    protected int[] parameterTypes;
    protected ParseInfo parseInfo;
    private java.sql.ResultSetMetaData pstmtResultMetaData;
    private byte[][] staticSqlStrings;
    private byte[] streamConvertBuf;
    private int[] streamLengths;
    private SimpleDateFormat tsdf;
    private SimpleDateFormat ddf;
    private SimpleDateFormat tdf;
    protected boolean useTrueBoolean;
    protected boolean usingAnsiMode;
    protected String batchedValuesClause;
    private boolean doPingInstead;
    private boolean compensateForOnDuplicateKeyUpdate;
    private CharsetEncoder charsetEncoder;
    protected int batchCommandIndex;
    protected boolean serverSupportsFracSecs;
    protected int rewrittenBatchSize;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$BatchVisitor.class */
    interface BatchVisitor {
        BatchVisitor increment();

        BatchVisitor decrement();

        BatchVisitor append(byte[] bArr);

        BatchVisitor merge(byte[] bArr, byte[] bArr2);

        BatchVisitor mergeWithLast(byte[] bArr);
    }

    static {
        if (Util.isJdbc4()) {
            try {
                String jdbc4ClassName = Util.isJdbc42() ? "com.mysql.jdbc.JDBC42PreparedStatement" : "com.mysql.jdbc.JDBC4PreparedStatement";
                JDBC_4_PSTMT_2_ARG_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, String.class);
                JDBC_4_PSTMT_3_ARG_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, String.class, String.class);
                JDBC_4_PSTMT_4_ARG_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, String.class, String.class, ParseInfo.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        } else {
            JDBC_4_PSTMT_2_ARG_CTOR = null;
            JDBC_4_PSTMT_3_ARG_CTOR = null;
            JDBC_4_PSTMT_4_ARG_CTOR = null;
        }
        HEX_DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$BatchParams.class */
    public class BatchParams {
        public boolean[] isNull;
        public boolean[] isStream;
        public InputStream[] parameterStreams;
        public byte[][] parameterStrings;
        public int[] streamLengths;

        /* JADX WARN: Type inference failed for: r1v9, types: [byte[], byte[][]] */
        BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags) {
            this.isNull = null;
            this.isStream = null;
            this.parameterStreams = null;
            this.parameterStrings = (byte[][]) null;
            this.streamLengths = null;
            this.parameterStrings = new byte[strings.length];
            this.parameterStreams = new InputStream[streams.length];
            this.isStream = new boolean[isStreamFlags.length];
            this.streamLengths = new int[lengths.length];
            this.isNull = new boolean[isNullFlags.length];
            System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
            System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
            System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
            System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
            System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$EndPoint.class */
    class EndPoint {
        int begin;
        int end;

        EndPoint(int b, int e) {
            this.begin = b;
            this.end = e;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$ParseInfo.class */
    public static final class ParseInfo {
        char firstStmtChar;
        boolean foundLoadData;
        long lastUsed;
        int statementLength;
        int statementStartPos;
        boolean canRewriteAsMultiValueInsert;
        byte[][] staticSql;
        boolean hasPlaceholders;
        int numberOfQueries;
        boolean isOnDuplicateKeyUpdate;
        int locationOfOnDuplicateKeyUpdate;
        String valuesClause;
        boolean parametersInDuplicateKeyClause;
        String charEncoding;
        private ParseInfo batchHead;
        private ParseInfo batchValues;
        private ParseInfo batchODKUClause;

        ParseInfo(String sql, MySQLConnection conn, java.sql.DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter) throws SQLException {
            this(sql, conn, dbmd, encoding, converter, true);
        }

        /* JADX WARN: Type inference failed for: r1v30, types: [byte[], byte[][]] */
        public ParseInfo(String sql, MySQLConnection conn, java.sql.DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo) throws SQLException {
            char c;
            this.firstStmtChar = (char) 0;
            this.foundLoadData = false;
            this.lastUsed = 0L;
            this.statementLength = 0;
            this.statementStartPos = 0;
            this.canRewriteAsMultiValueInsert = false;
            this.staticSql = (byte[][]) null;
            this.hasPlaceholders = false;
            this.numberOfQueries = 1;
            this.isOnDuplicateKeyUpdate = false;
            this.locationOfOnDuplicateKeyUpdate = -1;
            this.parametersInDuplicateKeyClause = false;
            try {
                if (sql == null) {
                    throw SQLError.createSQLException(Messages.getString("PreparedStatement.61"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, conn.getExceptionInterceptor());
                }
                this.charEncoding = encoding;
                this.lastUsed = System.currentTimeMillis();
                String quotedIdentifierString = dbmd.getIdentifierQuoteString();
                char quotedIdentifierChar = 0;
                if (quotedIdentifierString != null && !quotedIdentifierString.equals(SymbolConstants.SPACE_SYMBOL) && quotedIdentifierString.length() > 0) {
                    quotedIdentifierChar = quotedIdentifierString.charAt(0);
                }
                this.statementLength = sql.length();
                ArrayList<int[]> endpointList = new ArrayList<>();
                boolean inQuotes = false;
                char quoteChar = 0;
                boolean inQuotedId = false;
                int lastParmEnd = 0;
                boolean noBackslashEscapes = conn.isNoBackslashEscapesSet();
                this.statementStartPos = StatementImpl.findStartOfStatement(sql);
                int i = this.statementStartPos;
                while (i < this.statementLength) {
                    char c2 = sql.charAt(i);
                    if (this.firstStmtChar == 0 && Character.isLetter(c2)) {
                        this.firstStmtChar = Character.toUpperCase(c2);
                        if (this.firstStmtChar == 'I') {
                            this.locationOfOnDuplicateKeyUpdate = StatementImpl.getOnDuplicateKeyLocation(sql, conn.getDontCheckOnDuplicateKeyUpdateInSQL(), conn.getRewriteBatchedStatements(), conn.isNoBackslashEscapesSet());
                            this.isOnDuplicateKeyUpdate = this.locationOfOnDuplicateKeyUpdate != -1;
                        }
                    }
                    if (!noBackslashEscapes && c2 == '\\' && i < this.statementLength - 1) {
                        i++;
                    } else {
                        if (!inQuotes && quotedIdentifierChar != 0 && c2 == quotedIdentifierChar) {
                            inQuotedId = !inQuotedId;
                        } else if (!inQuotedId) {
                            if (inQuotes) {
                                if ((c2 == '\'' || c2 == '\"') && c2 == quoteChar) {
                                    if (i < this.statementLength - 1 && sql.charAt(i + 1) == quoteChar) {
                                        i++;
                                    } else {
                                        inQuotes = !inQuotes;
                                        quoteChar = 0;
                                    }
                                } else if ((c2 == '\'' || c2 == '\"') && c2 == quoteChar) {
                                    inQuotes = !inQuotes;
                                    quoteChar = 0;
                                }
                            } else if (c2 == '#' || (c2 == '-' && i + 1 < this.statementLength && sql.charAt(i + 1) == '-')) {
                                int endOfStmt = this.statementLength - 1;
                                while (i < endOfStmt && (c = sql.charAt(i)) != '\r' && c != '\n') {
                                    i++;
                                }
                            } else if (c2 == '/' && i + 1 < this.statementLength) {
                                char cNext = sql.charAt(i + 1);
                                if (cNext == '*') {
                                    i += 2;
                                    int j = i;
                                    while (true) {
                                        if (j >= this.statementLength) {
                                            break;
                                        }
                                        i++;
                                        char cNext2 = sql.charAt(j);
                                        if (cNext2 != '*' || j + 1 >= this.statementLength || sql.charAt(j + 1) != '/') {
                                            j++;
                                        } else {
                                            i++;
                                            if (i < this.statementLength) {
                                                c2 = sql.charAt(i);
                                            }
                                        }
                                    }
                                }
                            } else if (c2 == '\'' || c2 == '\"') {
                                inQuotes = true;
                                quoteChar = c2;
                            }
                        }
                        if (!inQuotes && !inQuotedId) {
                            if (c2 == '?') {
                                endpointList.add(new int[]{lastParmEnd, i});
                                lastParmEnd = i + 1;
                                if (this.isOnDuplicateKeyUpdate && i > this.locationOfOnDuplicateKeyUpdate) {
                                    this.parametersInDuplicateKeyClause = true;
                                }
                            } else if (c2 == ';') {
                                int j2 = i + 1;
                                if (j2 < this.statementLength) {
                                    while (j2 < this.statementLength && Character.isWhitespace(sql.charAt(j2))) {
                                        j2++;
                                    }
                                    if (j2 < this.statementLength) {
                                        this.numberOfQueries++;
                                    }
                                    i = j2 - 1;
                                }
                            }
                        }
                    }
                    i++;
                }
                if (this.firstStmtChar == 'L' && StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA")) {
                    this.foundLoadData = true;
                } else {
                    this.foundLoadData = false;
                }
                endpointList.add(new int[]{lastParmEnd, this.statementLength});
                this.staticSql = new byte[endpointList.size()];
                this.hasPlaceholders = this.staticSql.length > 1;
                for (int i2 = 0; i2 < this.staticSql.length; i2++) {
                    int[] ep = endpointList.get(i2);
                    int end = ep[1];
                    int begin = ep[0];
                    int len = end - begin;
                    if (this.foundLoadData) {
                        this.staticSql[i2] = StringUtils.getBytes(sql, begin, len);
                    } else if (encoding == null) {
                        byte[] buf = new byte[len];
                        for (int j3 = 0; j3 < len; j3++) {
                            buf[j3] = (byte) sql.charAt(begin + j3);
                        }
                        this.staticSql[i2] = buf;
                    } else if (converter != null) {
                        this.staticSql[i2] = StringUtils.getBytes(sql, converter, encoding, conn.getServerCharset(), begin, len, conn.parserKnowsUnicode(), conn.getExceptionInterceptor());
                    } else {
                        this.staticSql[i2] = StringUtils.getBytes(sql, encoding, conn.getServerCharset(), begin, len, conn.parserKnowsUnicode(), conn, conn.getExceptionInterceptor());
                    }
                }
                if (buildRewriteInfo) {
                    this.canRewriteAsMultiValueInsert = this.numberOfQueries == 1 && !this.parametersInDuplicateKeyClause && PreparedStatement.canRewrite(sql, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementStartPos);
                    if (this.canRewriteAsMultiValueInsert && conn.getRewriteBatchedStatements()) {
                        buildRewriteBatchedParams(sql, conn, dbmd, encoding, converter);
                    }
                }
            } catch (StringIndexOutOfBoundsException oobEx) {
                SQLException sqlEx = new SQLException("Parse error for " + sql);
                sqlEx.initCause(oobEx);
                throw sqlEx;
            }
        }

        private void buildRewriteBatchedParams(String sql, MySQLConnection conn, java.sql.DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter) throws SQLException {
            String headSql;
            this.valuesClause = extractValuesClause(sql, conn.getMetaData().getIdentifierQuoteString());
            String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
            if (this.isOnDuplicateKeyUpdate) {
                headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
            } else {
                headSql = sql;
            }
            this.batchHead = new ParseInfo(headSql, conn, metadata, encoding, converter, false);
            this.batchValues = new ParseInfo("," + this.valuesClause, conn, metadata, encoding, converter, false);
            this.batchODKUClause = null;
            if (odkuClause != null && odkuClause.length() > 0) {
                this.batchODKUClause = new ParseInfo("," + this.valuesClause + SymbolConstants.SPACE_SYMBOL + odkuClause, conn, metadata, encoding, converter, false);
            }
        }

        private String extractValuesClause(String sql, String quoteCharStr) throws SQLException {
            int indexOfFirstParen;
            int indexOfValues = -1;
            int valuesSearchStart = this.statementStartPos;
            while (indexOfValues == -1) {
                if (quoteCharStr.length() > 0) {
                    indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, sql, "VALUES", quoteCharStr, quoteCharStr, StringUtils.SEARCH_MODE__MRK_COM_WS);
                } else {
                    indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, sql, "VALUES");
                }
                if (indexOfValues <= 0) {
                    break;
                }
                char c = sql.charAt(indexOfValues - 1);
                if (!Character.isWhitespace(c) && c != ')' && c != '`') {
                    valuesSearchStart = indexOfValues + 6;
                    indexOfValues = -1;
                } else {
                    char c2 = sql.charAt(indexOfValues + 6);
                    if (!Character.isWhitespace(c2) && c2 != '(') {
                        valuesSearchStart = indexOfValues + 6;
                        indexOfValues = -1;
                    }
                }
            }
            if (indexOfValues == -1 || (indexOfFirstParen = sql.indexOf(40, indexOfValues + 6)) == -1) {
                return null;
            }
            int endOfValuesClause = this.isOnDuplicateKeyUpdate ? this.locationOfOnDuplicateKeyUpdate : sql.length();
            return sql.substring(indexOfFirstParen, endOfValuesClause);
        }

        synchronized ParseInfo getParseInfoForBatch(int numBatch) {
            AppendingBatchVisitor apv = new AppendingBatchVisitor();
            buildInfoForBatch(numBatch, apv);
            ParseInfo batchParseInfo = new ParseInfo(apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
            return batchParseInfo;
        }

        String getSqlForBatch(int numBatch) throws UnsupportedEncodingException {
            ParseInfo batchInfo = getParseInfoForBatch(numBatch);
            return getSqlForBatch(batchInfo);
        }

        String getSqlForBatch(ParseInfo batchInfo) throws UnsupportedEncodingException {
            int size = 0;
            byte[][] sqlStrings = batchInfo.staticSql;
            int sqlStringsLength = sqlStrings.length;
            for (byte[] bArr : sqlStrings) {
                size = size + bArr.length + 1;
            }
            StringBuilder buf = new StringBuilder(size);
            for (int i = 0; i < sqlStringsLength - 1; i++) {
                buf.append(StringUtils.toString(sqlStrings[i], this.charEncoding));
                buf.append("?");
            }
            buf.append(StringUtils.toString(sqlStrings[sqlStringsLength - 1]));
            return buf.toString();
        }

        private void buildInfoForBatch(int numBatch, BatchVisitor visitor) {
            if (!this.hasPlaceholders) {
                if (numBatch == 1) {
                    visitor.append(this.staticSql[0]);
                    return;
                }
                visitor.append(this.batchHead.staticSql[0]).increment();
                int numValueRepeats = numBatch - 1;
                if (this.batchODKUClause != null) {
                    numValueRepeats--;
                }
                byte[] valuesStaticSql = this.batchValues.staticSql[0];
                for (int i = 0; i < numValueRepeats; i++) {
                    visitor.mergeWithLast(valuesStaticSql).increment();
                }
                if (this.batchODKUClause != null) {
                    visitor.mergeWithLast(this.batchODKUClause.staticSql[0]).increment();
                    return;
                }
                return;
            }
            byte[][] headStaticSql = this.batchHead.staticSql;
            int headStaticSqlLength = headStaticSql.length;
            byte[] endOfHead = headStaticSql[headStaticSqlLength - 1];
            for (int i2 = 0; i2 < headStaticSqlLength - 1; i2++) {
                visitor.append(headStaticSql[i2]).increment();
            }
            int numValueRepeats2 = numBatch - 1;
            if (this.batchODKUClause != null) {
                numValueRepeats2--;
            }
            byte[][] valuesStaticSql2 = this.batchValues.staticSql;
            int valuesStaticSqlLength = valuesStaticSql2.length;
            byte[] beginOfValues = valuesStaticSql2[0];
            byte[] endOfValues = valuesStaticSql2[valuesStaticSqlLength - 1];
            for (int i3 = 0; i3 < numValueRepeats2; i3++) {
                visitor.merge(endOfValues, beginOfValues).increment();
                for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
                    visitor.append(valuesStaticSql2[j]).increment();
                }
            }
            if (this.batchODKUClause != null) {
                byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
                int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
                byte[] beginOfOdku = batchOdkuStaticSql[0];
                byte[] endOfOdku = batchOdkuStaticSql[batchOdkuStaticSqlLength - 1];
                if (numBatch > 1) {
                    visitor.merge(numValueRepeats2 > 0 ? endOfValues : endOfHead, beginOfOdku).increment();
                    for (int i4 = 1; i4 < batchOdkuStaticSqlLength; i4++) {
                        visitor.append(batchOdkuStaticSql[i4]).increment();
                    }
                    return;
                }
                visitor.append(endOfOdku).increment();
                return;
            }
            visitor.append(endOfHead);
        }

        private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos) {
            this.firstStmtChar = (char) 0;
            this.foundLoadData = false;
            this.lastUsed = 0L;
            this.statementLength = 0;
            this.statementStartPos = 0;
            this.canRewriteAsMultiValueInsert = false;
            this.staticSql = (byte[][]) null;
            this.hasPlaceholders = false;
            this.numberOfQueries = 1;
            this.isOnDuplicateKeyUpdate = false;
            this.locationOfOnDuplicateKeyUpdate = -1;
            this.parametersInDuplicateKeyClause = false;
            this.firstStmtChar = firstStmtChar;
            this.foundLoadData = foundLoadData;
            this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
            this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
            this.statementLength = statementLength;
            this.statementStartPos = statementStartPos;
            this.staticSql = staticSql;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$AppendingBatchVisitor.class */
    static class AppendingBatchVisitor implements BatchVisitor {
        LinkedList<byte[]> statementComponents = new LinkedList<>();

        AppendingBatchVisitor() {
        }

        @Override // com.mysql.jdbc.PreparedStatement.BatchVisitor
        public BatchVisitor append(byte[] values) {
            this.statementComponents.addLast(values);
            return this;
        }

        @Override // com.mysql.jdbc.PreparedStatement.BatchVisitor
        public BatchVisitor increment() {
            return this;
        }

        @Override // com.mysql.jdbc.PreparedStatement.BatchVisitor
        public BatchVisitor decrement() {
            this.statementComponents.removeLast();
            return this;
        }

        @Override // com.mysql.jdbc.PreparedStatement.BatchVisitor
        public BatchVisitor merge(byte[] front, byte[] back) {
            int mergedLength = front.length + back.length;
            byte[] merged = new byte[mergedLength];
            System.arraycopy(front, 0, merged, 0, front.length);
            System.arraycopy(back, 0, merged, front.length, back.length);
            this.statementComponents.addLast(merged);
            return this;
        }

        @Override // com.mysql.jdbc.PreparedStatement.BatchVisitor
        public BatchVisitor mergeWithLast(byte[] values) {
            if (this.statementComponents.isEmpty()) {
                return append(values);
            }
            return merge(this.statementComponents.removeLast(), values);
        }

        /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][], java.lang.Object[]] */
        public byte[][] getStaticSqlStrings() {
            ?? r0 = new byte[this.statementComponents.size()];
            this.statementComponents.toArray((Object[]) r0);
            return r0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Iterator i$ = this.statementComponents.iterator();
            while (i$.hasNext()) {
                byte[] comp = i$.next();
                sb.append(StringUtils.toString(comp));
            }
            return sb.toString();
        }
    }

    protected static int readFully(Reader reader, char[] buf, int length) throws IOException {
        int numCharsRead;
        int count;
        int i = 0;
        while (true) {
            numCharsRead = i;
            if (numCharsRead >= length || (count = reader.read(buf, numCharsRead, length - numCharsRead)) < 0) {
                break;
            }
            i = numCharsRead + count;
        }
        return numCharsRead;
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String catalog) throws SQLException {
        return !Util.isJdbc4() ? new PreparedStatement(conn, catalog) : (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[]{conn, catalog}, conn.getExceptionInterceptor());
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog) throws SQLException {
        return !Util.isJdbc4() ? new PreparedStatement(conn, sql, catalog) : (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[]{conn, sql, catalog}, conn.getExceptionInterceptor());
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
        return !Util.isJdbc4() ? new PreparedStatement(conn, sql, catalog, cachedParseInfo) : (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[]{conn, sql, catalog, cachedParseInfo}, conn.getExceptionInterceptor());
    }

    public PreparedStatement(MySQLConnection conn, String catalog) throws SQLException {
        super(conn, catalog);
        this.batchHasPlainStatements = false;
        this.dbmd = null;
        this.firstCharOfStmt = (char) 0;
        this.isLoadDataQuery = false;
        this.isNull = null;
        this.isStream = null;
        this.numberOfExecutions = 0;
        this.originalSql = null;
        this.parameterStreams = null;
        this.parameterValues = (byte[][]) null;
        this.parameterTypes = null;
        this.staticSqlStrings = (byte[][]) null;
        this.streamConvertBuf = null;
        this.streamLengths = null;
        this.tsdf = null;
        this.useTrueBoolean = false;
        this.compensateForOnDuplicateKeyUpdate = false;
        this.batchCommandIndex = -1;
        this.rewrittenBatchSize = 0;
        detectFractionalSecondsSupport();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
    }

    protected void detectFractionalSecondsSupport() throws SQLException {
        this.serverSupportsFracSecs = this.connection != null && this.connection.versionMeetsMinimum(5, 6, 4);
    }

    public PreparedStatement(MySQLConnection conn, String sql, String catalog) throws SQLException {
        super(conn, catalog);
        this.batchHasPlainStatements = false;
        this.dbmd = null;
        this.firstCharOfStmt = (char) 0;
        this.isLoadDataQuery = false;
        this.isNull = null;
        this.isStream = null;
        this.numberOfExecutions = 0;
        this.originalSql = null;
        this.parameterStreams = null;
        this.parameterValues = (byte[][]) null;
        this.parameterTypes = null;
        this.staticSqlStrings = (byte[][]) null;
        this.streamConvertBuf = null;
        this.streamLengths = null;
        this.tsdf = null;
        this.useTrueBoolean = false;
        this.compensateForOnDuplicateKeyUpdate = false;
        this.batchCommandIndex = -1;
        this.rewrittenBatchSize = 0;
        if (sql == null) {
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        detectFractionalSecondsSupport();
        this.originalSql = sql;
        this.doPingInstead = this.originalSql.startsWith("/* ping */");
        this.dbmd = this.connection.getMetaData();
        this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
        this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
        initializeFromParseInfo();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
        if (conn.getRequiresEscapingEncoder()) {
            this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
        }
    }

    public PreparedStatement(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
        super(conn, catalog);
        this.batchHasPlainStatements = false;
        this.dbmd = null;
        this.firstCharOfStmt = (char) 0;
        this.isLoadDataQuery = false;
        this.isNull = null;
        this.isStream = null;
        this.numberOfExecutions = 0;
        this.originalSql = null;
        this.parameterStreams = null;
        this.parameterValues = (byte[][]) null;
        this.parameterTypes = null;
        this.staticSqlStrings = (byte[][]) null;
        this.streamConvertBuf = null;
        this.streamLengths = null;
        this.tsdf = null;
        this.useTrueBoolean = false;
        this.compensateForOnDuplicateKeyUpdate = false;
        this.batchCommandIndex = -1;
        this.rewrittenBatchSize = 0;
        if (sql == null) {
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        detectFractionalSecondsSupport();
        this.originalSql = sql;
        this.dbmd = this.connection.getMetaData();
        this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
        this.parseInfo = cachedParseInfo;
        this.usingAnsiMode = !this.connection.useAnsiQuotedIdentifiers();
        initializeFromParseInfo();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
        if (conn.getRequiresEscapingEncoder()) {
            this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
        }
    }

    public void addBatch() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.batchedArgs == null) {
                this.batchedArgs = new ArrayList();
            }
            for (int i = 0; i < this.parameterValues.length; i++) {
                checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
            }
            this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
        }
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement
    public void addBatch(String sql) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.batchHasPlainStatements = true;
            super.addBatch(sql);
        }
    }

    public String asSql() throws SQLException {
        return asSql(false);
    }

    public String asSql(boolean quoteStreamsAndUnknowns) throws SQLException {
        String string;
        byte[] val;
        boolean isStreamParam;
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder buf = new StringBuilder();
            try {
                int realParameterCount = this.parameterCount + getParameterIndexOffset();
                Object batchArg = null;
                if (this.batchCommandIndex != -1) {
                    batchArg = this.batchedArgs.get(this.batchCommandIndex);
                }
                for (int i = 0; i < realParameterCount; i++) {
                    if (this.charEncoding != null) {
                        buf.append(StringUtils.toString(this.staticSqlStrings[i], this.charEncoding));
                    } else {
                        buf.append(StringUtils.toString(this.staticSqlStrings[i]));
                    }
                    if (batchArg != null && (batchArg instanceof String)) {
                        buf.append((String) batchArg);
                    } else {
                        if (this.batchCommandIndex == -1) {
                            val = this.parameterValues[i];
                        } else {
                            val = ((BatchParams) batchArg).parameterStrings[i];
                        }
                        if (this.batchCommandIndex == -1) {
                            isStreamParam = this.isStream[i];
                        } else {
                            isStreamParam = ((BatchParams) batchArg).isStream[i];
                        }
                        if (val == null && !isStreamParam) {
                            if (quoteStreamsAndUnknowns) {
                                buf.append("'");
                            }
                            buf.append("** NOT SPECIFIED **");
                            if (quoteStreamsAndUnknowns) {
                                buf.append("'");
                            }
                        } else if (isStreamParam) {
                            if (quoteStreamsAndUnknowns) {
                                buf.append("'");
                            }
                            buf.append("** STREAM DATA **");
                            if (quoteStreamsAndUnknowns) {
                                buf.append("'");
                            }
                        } else if (this.charConverter != null) {
                            buf.append(this.charConverter.toString(val));
                        } else if (this.charEncoding != null) {
                            buf.append(new String(val, this.charEncoding));
                        } else {
                            buf.append(StringUtils.toAsciiString(val));
                        }
                    }
                }
                if (this.charEncoding != null) {
                    buf.append(StringUtils.toString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()], this.charEncoding));
                } else {
                    buf.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()]));
                }
                string = buf.toString();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
            }
        }
        return string;
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement
    public void clearBatch() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.batchHasPlainStatements = false;
            super.clearBatch();
        }
    }

    public void clearParameters() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            for (int i = 0; i < this.parameterValues.length; i++) {
                this.parameterValues[i] = null;
                this.parameterStreams[i] = null;
                this.isStream[i] = false;
                this.isNull[i] = false;
                this.parameterTypes[i] = 0;
            }
        }
    }

    private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException {
        int lastwritten = 0;
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            if (b == 0) {
                if (i > lastwritten) {
                    packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
                }
                packet.writeByte((byte) 92);
                packet.writeByte((byte) 48);
                lastwritten = i + 1;
            } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
                if (i > lastwritten) {
                    packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
                }
                packet.writeByte((byte) 92);
                lastwritten = i;
            }
        }
        if (lastwritten < size) {
            packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
        }
    }

    private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size) {
        int lastwritten = 0;
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            if (b == 0) {
                if (i > lastwritten) {
                    bytesOut.write(buf, lastwritten, i - lastwritten);
                }
                bytesOut.write(92);
                bytesOut.write(48);
                lastwritten = i + 1;
            } else if (b == 39) {
                if (i > lastwritten) {
                    bytesOut.write(buf, lastwritten, i - lastwritten);
                }
                bytesOut.write(this.connection.isNoBackslashEscapesSet() ? 39 : 92);
                lastwritten = i;
            } else if (b == 92 || (!this.usingAnsiMode && b == 34)) {
                if (i > lastwritten) {
                    bytesOut.write(buf, lastwritten, i - lastwritten);
                }
                bytesOut.write(92);
                lastwritten = i;
            }
        }
        if (lastwritten < size) {
            bytesOut.write(buf, lastwritten, size - lastwritten);
        }
    }

    protected boolean checkReadOnlySafeStatement() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = this.firstCharOfStmt == 'S' || !this.connection.isReadOnly();
        }
        return z;
    }

    public boolean execute() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            MySQLConnection locallyScopedConn = this.connection;
            if (!this.doPingInstead && !checkReadOnlySafeStatement()) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.lastQueryIsOnDupKeyUpdate = false;
            if (this.retrieveGeneratedKeys) {
                this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL();
            }
            this.batchedGeneratedKeys = null;
            resetCancelledState();
            implicitlyCloseAllOpenResults();
            clearWarnings();
            if (this.doPingInstead) {
                doPingInstead();
                return true;
            }
            setupStreamingTimeout(locallyScopedConn);
            Buffer sendPacket = fillSendPacket();
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            CachedResultSetMetaData cachedMetadata = null;
            if (locallyScopedConn.getCacheResultSetMetadata()) {
                cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
            }
            Field[] metadataFromCache = null;
            if (cachedMetadata != null) {
                metadataFromCache = cachedMetadata.fields;
            }
            boolean oldInfoMsgState = false;
            if (this.retrieveGeneratedKeys) {
                oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
                locallyScopedConn.setReadInfoMsgEnabled(true);
            }
            locallyScopedConn.setSessionMaxRows(this.firstCharOfStmt == 'S' ? this.maxRows : -1);
            ResultSetInternalMethods rs = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), this.firstCharOfStmt == 'S', metadataFromCache, false);
            if (cachedMetadata != null) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, rs);
            } else if (rs.reallyResult() && locallyScopedConn.getCacheResultSetMetadata()) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, rs);
            }
            if (this.retrieveGeneratedKeys) {
                locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
                rs.setFirstCharOfQuery(this.firstCharOfStmt);
            }
            if (oldCatalog != null) {
                locallyScopedConn.setCatalog(oldCatalog);
            }
            if (rs != null) {
                this.lastInsertId = rs.getUpdateID();
                this.results = rs;
            }
            return rs != null && rs.reallyResult();
        }
    }

    @Override // com.mysql.jdbc.StatementImpl
    protected long[] executeBatchInternal() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.connection.isReadOnly()) {
                throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
            if (this.batchedArgs == null || this.batchedArgs.size() == 0) {
                return new long[0];
            }
            int batchTimeout = this.timeoutInMillis;
            this.timeoutInMillis = 0;
            resetCancelledState();
            try {
                statementBegins();
                clearWarnings();
                if (!this.batchHasPlainStatements && this.connection.getRewriteBatchedStatements()) {
                    if (canRewriteAsMultiValueInsertAtSqlLevel()) {
                        return executeBatchedInserts(batchTimeout);
                    }
                    if (this.connection.versionMeetsMinimum(4, 1, 0) && !this.batchHasPlainStatements && this.batchedArgs != null && this.batchedArgs.size() > 3) {
                        return executePreparedBatchAsMultiStatement(batchTimeout);
                    }
                }
                return executeBatchSerially(batchTimeout);
            } finally {
                this.statementExecuting.set(false);
                clearBatch();
            }
        }
    }

    public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
        return this.parseInfo.canRewriteAsMultiValueInsert;
    }

    protected int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
        return this.parseInfo.locationOfOnDuplicateKeyUpdate;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v34, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v42, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v56, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v62, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v64, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v75, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v77, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r0v90, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v23, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v27, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v35, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v36, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v42, types: [java.sql.PreparedStatement] */
    /* JADX WARN: Type inference failed for: r1v47, types: [java.sql.PreparedStatement] */
    protected long[] executePreparedBatchAsMultiStatement(int r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 808
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.executePreparedBatchAsMultiStatement(int):long[]");
    }

    private String generateMultiStatementForBatch(int numBatches) throws SQLException {
        String string;
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder newStatementSql = new StringBuilder((this.originalSql.length() + 1) * numBatches);
            newStatementSql.append(this.originalSql);
            for (int i = 0; i < numBatches - 1; i++) {
                newStatementSql.append(';');
                newStatementSql.append(this.originalSql);
            }
            string = newStatementSql.toString();
        }
        return string;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    protected long[] executeBatchedInserts(int r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 630
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.executeBatchedInserts(int):long[]");
    }

    protected String getValuesClause() throws SQLException {
        return this.parseInfo.valuesClause;
    }

    protected int computeBatchSize(int numBatchedArgs) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
            long maxSizeOfParameterSet = combinedValues[0];
            long sizeOfEntireBatch = combinedValues[1];
            int maxAllowedPacket = this.connection.getMaxAllowedPacket();
            if (sizeOfEntireBatch < maxAllowedPacket - this.originalSql.length()) {
                return numBatchedArgs;
            }
            return (int) Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
        }
    }

    protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) throws SQLException {
        long[] jArr;
        long sizeOfParameterSet;
        synchronized (checkClosed().getConnectionMutex()) {
            long sizeOfEntireBatch = 0;
            long maxSizeOfParameterSet = 0;
            for (int i = 0; i < numBatchedArgs; i++) {
                BatchParams paramArg = (BatchParams) this.batchedArgs.get(i);
                boolean[] isNullBatch = paramArg.isNull;
                boolean[] isStreamBatch = paramArg.isStream;
                long sizeOfParameterSet2 = 0;
                for (int j = 0; j < isNullBatch.length; j++) {
                    if (!isNullBatch[j]) {
                        if (isStreamBatch[j]) {
                            int streamLength = paramArg.streamLengths[j];
                            if (streamLength != -1) {
                                sizeOfParameterSet2 += streamLength * 2;
                            } else {
                                int paramLength = paramArg.parameterStrings[j].length;
                                sizeOfParameterSet2 += paramLength;
                            }
                        } else {
                            sizeOfParameterSet2 += paramArg.parameterStrings[j].length;
                        }
                    } else {
                        sizeOfParameterSet2 += 4;
                    }
                }
                if (getValuesClause() != null) {
                    sizeOfParameterSet = sizeOfParameterSet2 + getValuesClause().length() + 1;
                } else {
                    sizeOfParameterSet = sizeOfParameterSet2 + this.originalSql.length() + 1;
                }
                sizeOfEntireBatch += sizeOfParameterSet;
                if (sizeOfParameterSet > maxSizeOfParameterSet) {
                    maxSizeOfParameterSet = sizeOfParameterSet;
                }
            }
            jArr = new long[]{maxSizeOfParameterSet, sizeOfEntireBatch};
        }
        return jArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:72:0x01f0, code lost:
    
        throw r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01fd, code lost:
    
        r17.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x020d, code lost:
    
        resetCancelledState();
     */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01fd A[Catch: all -> 0x0223, TryCatch #0 {, blocks: (B:4:0x000c, B:6:0x0015, B:7:0x001a, B:9:0x0024, B:12:0x003f, B:14:0x0053, B:18:0x0060, B:20:0x006c, B:21:0x0084, B:23:0x008b, B:24:0x0098, B:25:0x009d, B:27:0x00a6, B:28:0x00b5, B:30:0x00bd, B:32:0x00e1, B:36:0x00f2, B:55:0x018b, B:37:0x00f8, B:41:0x0131, B:44:0x0139, B:46:0x014a, B:48:0x0152, B:50:0x015a, B:53:0x016a, B:54:0x018a, B:58:0x019d, B:59:0x01a8, B:73:0x01f1, B:75:0x01fd, B:76:0x020d, B:63:0x01b1, B:68:0x01e8, B:65:0x01bb, B:66:0x01e5, B:89:0x01f1, B:91:0x01fd, B:92:0x020d, B:72:0x01f0, B:82:0x0221, B:80:0x021d), top: B:93:0x000c, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x021d A[Catch: all -> 0x0223, TryCatch #0 {, blocks: (B:4:0x000c, B:6:0x0015, B:7:0x001a, B:9:0x0024, B:12:0x003f, B:14:0x0053, B:18:0x0060, B:20:0x006c, B:21:0x0084, B:23:0x008b, B:24:0x0098, B:25:0x009d, B:27:0x00a6, B:28:0x00b5, B:30:0x00bd, B:32:0x00e1, B:36:0x00f2, B:55:0x018b, B:37:0x00f8, B:41:0x0131, B:44:0x0139, B:46:0x014a, B:48:0x0152, B:50:0x015a, B:53:0x016a, B:54:0x018a, B:58:0x019d, B:59:0x01a8, B:73:0x01f1, B:75:0x01fd, B:76:0x020d, B:63:0x01b1, B:68:0x01e8, B:65:0x01bb, B:66:0x01e5, B:89:0x01f1, B:91:0x01fd, B:92:0x020d, B:72:0x01f0, B:82:0x0221, B:80:0x021d), top: B:93:0x000c, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected long[] executeBatchSerially(int r11) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 554
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.executeBatchSerially(int):long[]");
    }

    public String getDateTime(String pattern) {
        SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat(null, pattern, null, null);
        return sdf.format(new Date());
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00f8, code lost:
    
        throw r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0100, code lost:
    
        r12.statementExecuting.set(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x010a, code lost:
    
        if (0 == 0) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010d, code lost:
    
        r22.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0100 A[Catch: NullPointerException -> 0x0126, all -> 0x0130, TryCatch #3 {NullPointerException -> 0x0126, blocks: (B:4:0x000d, B:5:0x0020, B:7:0x002a, B:9:0x0031, B:11:0x003e, B:14:0x005f, B:15:0x0063, B:17:0x0086, B:19:0x009f, B:20:0x00a4, B:22:0x00a8, B:23:0x00af, B:24:0x00b0, B:26:0x00b7, B:28:0x00c1, B:30:0x00d6, B:31:0x00dc, B:29:0x00cd, B:33:0x00df, B:48:0x0100, B:51:0x010d, B:37:0x00e7, B:39:0x00ea, B:66:0x0100, B:69:0x010d, B:45:0x00f8), top: B:71:0x000d, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x010d A[Catch: NullPointerException -> 0x0126, all -> 0x0130, TryCatch #3 {NullPointerException -> 0x0126, blocks: (B:4:0x000d, B:5:0x0020, B:7:0x002a, B:9:0x0031, B:11:0x003e, B:14:0x005f, B:15:0x0063, B:17:0x0086, B:19:0x009f, B:20:0x00a4, B:22:0x00a8, B:23:0x00af, B:24:0x00b0, B:26:0x00b7, B:28:0x00c1, B:30:0x00d6, B:31:0x00dc, B:29:0x00cd, B:33:0x00df, B:48:0x0100, B:51:0x010d, B:37:0x00e7, B:39:0x00ea, B:66:0x0100, B:69:0x010d, B:45:0x00f8), top: B:71:0x000d, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.mysql.jdbc.ResultSetInternalMethods executeInternal(int r13, com.mysql.jdbc.Buffer r14, boolean r15, boolean r16, com.mysql.jdbc.Field[] r17, boolean r18) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 312
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.executeInternal(int, com.mysql.jdbc.Buffer, boolean, boolean, com.mysql.jdbc.Field[], boolean):com.mysql.jdbc.ResultSetInternalMethods");
    }

    public ResultSet executeQuery() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            MySQLConnection locallyScopedConn = this.connection;
            checkForDml(this.originalSql, this.firstCharOfStmt);
            this.batchedGeneratedKeys = null;
            resetCancelledState();
            implicitlyCloseAllOpenResults();
            clearWarnings();
            if (this.doPingInstead) {
                doPingInstead();
                return this.results;
            }
            setupStreamingTimeout(locallyScopedConn);
            Buffer sendPacket = fillSendPacket();
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            CachedResultSetMetaData cachedMetadata = null;
            if (locallyScopedConn.getCacheResultSetMetadata()) {
                cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
            }
            Field[] metadataFromCache = null;
            if (cachedMetadata != null) {
                metadataFromCache = cachedMetadata.fields;
            }
            locallyScopedConn.setSessionMaxRows(this.maxRows);
            this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false);
            if (oldCatalog != null) {
                locallyScopedConn.setCatalog(oldCatalog);
            }
            if (cachedMetadata != null) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
            } else if (locallyScopedConn.getCacheResultSetMetadata()) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, this.results);
            }
            this.lastInsertId = this.results.getUpdateID();
            return this.results;
        }
    }

    public int executeUpdate() throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate());
    }

    protected long executeUpdateInternal(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch) throws SQLException {
        long jExecuteUpdateInternal;
        synchronized (checkClosed().getConnectionMutex()) {
            if (clearBatchedGeneratedKeysAndWarnings) {
                clearWarnings();
                this.batchedGeneratedKeys = null;
                jExecuteUpdateInternal = executeUpdateInternal(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
            } else {
                jExecuteUpdateInternal = executeUpdateInternal(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
            }
        }
        return jExecuteUpdateInternal;
    }

    protected long executeUpdateInternal(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch) throws SQLException {
        long j;
        synchronized (checkClosed().getConnectionMutex()) {
            MySQLConnection locallyScopedConn = this.connection;
            if (locallyScopedConn.isReadOnly(false)) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.firstCharOfStmt == 'S' && isSelectQuery()) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), SQLError.SQL_STATE_NO_ROWS_UPDATED_OR_DELETED, getExceptionInterceptor());
            }
            resetCancelledState();
            implicitlyCloseAllOpenResults();
            Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            locallyScopedConn.setSessionMaxRows(-1);
            boolean oldInfoMsgState = false;
            if (this.retrieveGeneratedKeys) {
                oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
                locallyScopedConn.setReadInfoMsgEnabled(true);
            }
            ResultSetInternalMethods rs = executeInternal(-1, sendPacket, false, false, null, isReallyBatch);
            if (this.retrieveGeneratedKeys) {
                locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
                rs.setFirstCharOfQuery(this.firstCharOfStmt);
            }
            if (oldCatalog != null) {
                locallyScopedConn.setCatalog(oldCatalog);
            }
            this.results = rs;
            this.updateCount = rs.getUpdateCount();
            if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate && (this.updateCount == 2 || this.updateCount == 0)) {
                this.updateCount = 1L;
            }
            this.lastInsertId = rs.getUpdateID();
            j = this.updateCount;
        }
        return j;
    }

    protected boolean containsOnDuplicateKeyUpdateInSQL() {
        return this.parseInfo.isOnDuplicateKeyUpdate;
    }

    protected Buffer fillSendPacket() throws SQLException {
        Buffer bufferFillSendPacket;
        synchronized (checkClosed().getConnectionMutex()) {
            bufferFillSendPacket = fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
        }
        return bufferFillSendPacket;
    }

    protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths) throws SQLException {
        Buffer sendPacket;
        synchronized (checkClosed().getConnectionMutex()) {
            sendPacket = this.connection.getIO().getSharedSendPacket();
            sendPacket.clear();
            sendPacket.writeByte((byte) 3);
            boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
            int ensurePacketSize = 0;
            String statementComment = this.connection.getStatementComment();
            byte[] commentAsBytes = null;
            if (statementComment != null) {
                if (this.charConverter != null) {
                    commentAsBytes = this.charConverter.toBytes(statementComment);
                } else {
                    commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                }
                int ensurePacketSize2 = 0 + commentAsBytes.length;
                ensurePacketSize = ensurePacketSize2 + 6;
            }
            for (int i = 0; i < batchedParameterStrings.length; i++) {
                if (batchedIsStream[i] && useStreamLengths) {
                    ensurePacketSize += batchedStreamLengths[i];
                }
            }
            if (ensurePacketSize != 0) {
                sendPacket.ensureCapacity(ensurePacketSize);
            }
            if (commentAsBytes != null) {
                sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
                sendPacket.writeBytesNoNull(commentAsBytes);
                sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
            }
            for (int i2 = 0; i2 < batchedParameterStrings.length; i2++) {
                checkAllParametersSet(batchedParameterStrings[i2], batchedParameterStreams[i2], i2);
                sendPacket.writeBytesNoNull(this.staticSqlStrings[i2]);
                if (batchedIsStream[i2]) {
                    streamToBytes(sendPacket, batchedParameterStreams[i2], true, batchedStreamLengths[i2], useStreamLengths);
                } else {
                    sendPacket.writeBytesNoNull(batchedParameterStrings[i2]);
                }
            }
            sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
        }
        return sendPacket;
    }

    private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex) throws SQLException {
        if (parameterString == null && parameterStream == null) {
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), SQLError.SQL_STATE_WRONG_NO_OF_PARAMETERS, getExceptionInterceptor());
        }
    }

    protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches) throws SQLException {
        PreparedStatement pstmt;
        synchronized (checkClosed().getConnectionMutex()) {
            pstmt = new PreparedStatement(localConn, "Rewritten batch of: " + this.originalSql, this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches));
            pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
            pstmt.rewrittenBatchSize = numBatches;
        }
        return pstmt;
    }

    protected void setRetrieveGeneratedKeys(boolean flag) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.retrieveGeneratedKeys = flag;
        }
    }

    public int getRewrittenBatchSize() {
        return this.rewrittenBatchSize;
    }

    public String getNonRewrittenSql() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            int indexOfBatch = this.originalSql.indexOf(" of: ");
            if (indexOfBatch != -1) {
                return this.originalSql.substring(indexOfBatch + 5);
            }
            return this.originalSql;
        }
    }

    public byte[] getBytesRepresentation(int parameterIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.isStream[parameterIndex]) {
                return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
            }
            byte[] parameterVal = this.parameterValues[parameterIndex];
            if (parameterVal == null) {
                return null;
            }
            if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
                byte[] valNoQuotes = new byte[parameterVal.length - 2];
                System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
                return valNoQuotes;
            }
            return parameterVal;
        }
    }

    protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            Object batchedArg = this.batchedArgs.get(commandIndex);
            if (batchedArg instanceof String) {
                try {
                    return StringUtils.getBytes((String) batchedArg, this.charEncoding);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
                }
            }
            BatchParams params = (BatchParams) batchedArg;
            if (params.isStream[parameterIndex]) {
                return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
            }
            byte[] parameterVal = params.parameterStrings[parameterIndex];
            if (parameterVal == null) {
                return null;
            }
            if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
                byte[] valNoQuotes = new byte[parameterVal.length - 2];
                System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
                return valNoQuotes;
            }
            return parameterVal;
        }
    }

    private final String getDateTimePattern(String dt, boolean toTime) throws Exception {
        int dtLength = dt != null ? dt.length() : 0;
        if (dtLength >= 8 && dtLength <= 10) {
            int dashCount = 0;
            boolean isDateOnly = true;
            int i = 0;
            while (true) {
                if (i >= dtLength) {
                    break;
                }
                char c = dt.charAt(i);
                if (!Character.isDigit(c) && c != '-') {
                    isDateOnly = false;
                    break;
                }
                if (c == '-') {
                    dashCount++;
                }
                i++;
            }
            if (isDateOnly && dashCount == 2) {
                return "yyyy-MM-dd";
            }
        }
        boolean colonsOnly = true;
        int i2 = 0;
        while (true) {
            if (i2 >= dtLength) {
                break;
            }
            char c2 = dt.charAt(i2);
            if (Character.isDigit(c2) || c2 == ':') {
                i2++;
            } else {
                colonsOnly = false;
                break;
            }
        }
        if (colonsOnly) {
            return DateFormatConstants.TIME_FOR_COLON;
        }
        StringReader reader = new StringReader(dt + SymbolConstants.SPACE_SYMBOL);
        ArrayList<Object[]> vec = new ArrayList<>();
        ArrayList<Object[]> vecRemovelist = new ArrayList<>();
        Object[] nv = {'y', new StringBuilder(), 0};
        vec.add(nv);
        if (toTime) {
            Object[] nv2 = {'h', new StringBuilder(), 0};
            vec.add(nv2);
        }
        while (true) {
            int z = reader.read();
            if (z == -1) {
                break;
            }
            char separator = (char) z;
            int maxvecs = vec.size();
            for (int count = 0; count < maxvecs; count++) {
                Object[] v = vec.get(count);
                int n = ((Integer) v[2]).intValue();
                char c3 = getSuccessor(((Character) v[0]).charValue(), n);
                if (!Character.isLetterOrDigit(separator)) {
                    if (c3 == ((Character) v[0]).charValue() && c3 != 'S') {
                        vecRemovelist.add(v);
                    } else {
                        ((StringBuilder) v[1]).append(separator);
                        if (c3 == 'X' || c3 == 'Y') {
                            v[2] = 4;
                        }
                    }
                } else {
                    if (c3 == 'X') {
                        c3 = 'y';
                        Object[] nv3 = {'M', new StringBuilder(((StringBuilder) v[1]).toString()).append('M'), 1};
                        vec.add(nv3);
                    } else if (c3 == 'Y') {
                        c3 = 'M';
                        Object[] nv4 = {'d', new StringBuilder(((StringBuilder) v[1]).toString()).append('d'), 1};
                        vec.add(nv4);
                    }
                    ((StringBuilder) v[1]).append(c3);
                    if (c3 == ((Character) v[0]).charValue()) {
                        v[2] = Integer.valueOf(n + 1);
                    } else {
                        v[0] = Character.valueOf(c3);
                        v[2] = 1;
                    }
                }
            }
            int size = vecRemovelist.size();
            for (int i3 = 0; i3 < size; i3++) {
                vec.remove(vecRemovelist.get(i3));
            }
            vecRemovelist.clear();
        }
        int size2 = vec.size();
        for (int i4 = 0; i4 < size2; i4++) {
            Object[] v2 = vec.get(i4);
            char c4 = ((Character) v2[0]).charValue();
            boolean bk = getSuccessor(c4, ((Integer) v2[2]).intValue()) != c4;
            boolean atEnd = (c4 == 's' || c4 == 'm' || (c4 == 'h' && toTime)) && bk;
            boolean finishesAtDate = bk && c4 == 'd' && !toTime;
            boolean containsEnd = ((StringBuilder) v2[1]).toString().indexOf(87) != -1;
            if ((!atEnd && !finishesAtDate) || containsEnd) {
                vecRemovelist.add(v2);
            }
        }
        int size3 = vecRemovelist.size();
        for (int i5 = 0; i5 < size3; i5++) {
            vec.remove(vecRemovelist.get(i5));
        }
        vecRemovelist.clear();
        StringBuilder format = (StringBuilder) vec.get(0)[1];
        format.setLength(format.length() - 1);
        return format.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ae, code lost:
    
        throw r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00b5, code lost:
    
        if (0 == 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b8, code lost:
    
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ca, code lost:
    
        if (0 == 0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00cd, code lost:
    
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00de, code lost:
    
        if (0 == 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e3, code lost:
    
        throw null;
     */
    @Override // java.sql.PreparedStatement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.getMetaData():java.sql.ResultSetMetaData");
    }

    protected boolean isSelectQuery() throws SQLException {
        boolean zStartsWithIgnoreCaseAndWs;
        synchronized (checkClosed().getConnectionMutex()) {
            zStartsWithIgnoreCaseAndWs = StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
        }
        return zStartsWithIgnoreCaseAndWs;
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        MysqlParameterMetadata mysqlParameterMetadata;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.parameterMetaData == null) {
                if (this.connection.getGenerateSimpleParameterMetadata()) {
                    this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
                } else {
                    this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
                }
            }
            mysqlParameterMetadata = this.parameterMetaData;
        }
        return mysqlParameterMetadata;
    }

    ParseInfo getParseInfo() {
        return this.parseInfo;
    }

    private final char getSuccessor(char c, int n) {
        if (c == 'y' && n == 2) {
            return 'X';
        }
        if (c == 'y' && n < 4) {
            return 'y';
        }
        if (c == 'y') {
            return 'M';
        }
        if (c == 'M' && n == 2) {
            return 'Y';
        }
        if (c == 'M' && n < 3) {
            return 'M';
        }
        if (c == 'M') {
            return 'd';
        }
        if (c == 'd' && n < 2) {
            return 'd';
        }
        if (c == 'd') {
            return 'H';
        }
        if (c == 'H' && n < 2) {
            return 'H';
        }
        if (c == 'H') {
            return 'm';
        }
        if (c == 'm' && n < 2) {
            return 'm';
        }
        if (c == 'm') {
            return 's';
        }
        return (c != 's' || n >= 2) ? 'W' : 's';
    }

    private final void hexEscapeBlock(byte[] buf, Buffer packet, int size) throws SQLException {
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            int lowBits = (b & 255) / 16;
            int highBits = (b & 255) % 16;
            packet.writeByte(HEX_DIGITS[lowBits]);
            packet.writeByte(HEX_DIGITS[highBits]);
        }
    }

    /* JADX WARN: Type inference failed for: r1v16, types: [byte[], byte[][]] */
    private void initializeFromParseInfo() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.staticSqlStrings = this.parseInfo.staticSql;
            this.isLoadDataQuery = this.parseInfo.foundLoadData;
            this.firstCharOfStmt = this.parseInfo.firstStmtChar;
            this.parameterCount = this.staticSqlStrings.length - 1;
            this.parameterValues = new byte[this.parameterCount];
            this.parameterStreams = new InputStream[this.parameterCount];
            this.isStream = new boolean[this.parameterCount];
            this.streamLengths = new int[this.parameterCount];
            this.isNull = new boolean[this.parameterCount];
            this.parameterTypes = new int[this.parameterCount];
            clearParameters();
            for (int j = 0; j < this.parameterCount; j++) {
                this.isStream[j] = false;
            }
        }
    }

    boolean isNull(int paramIndex) throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = this.isNull[paramIndex];
        }
        return z;
    }

    private final int readblock(InputStream i, byte[] b) throws SQLException {
        try {
            return i.read(b);
        } catch (Throwable ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    private final int readblock(InputStream i, byte[] b, int length) throws SQLException {
        try {
            int lengthToRead = length;
            if (lengthToRead > b.length) {
                lengthToRead = b.length;
            }
            return i.read(b, 0, lengthToRead);
        } catch (Throwable ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    @Override // com.mysql.jdbc.StatementImpl
    protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return;
        }
        synchronized (locallyScopedConn.getConnectionMutex()) {
            if (this.isClosed) {
                return;
            }
            if (this.useUsageAdvisor && this.numberOfExecutions <= 1) {
                this.connection.getProfilerEventHandlerInstance().processEvent((byte) 0, this.connection, this, null, 0L, new Throwable(), Messages.getString("PreparedStatement.43"));
            }
            super.realClose(calledExplicitly, closeOpenResults);
            this.dbmd = null;
            this.originalSql = null;
            this.staticSqlStrings = (byte[][]) null;
            this.parameterValues = (byte[][]) null;
            this.parameterStreams = null;
            this.isStream = null;
            this.streamLengths = null;
            this.isNull = null;
            this.streamConvertBuf = null;
            this.parameterTypes = null;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setArray(int i, Array x) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 12);
        } else {
            setBinaryStream(parameterIndex, x, length);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 3);
        } else {
            setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 3;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                int parameterIndexOffset = getParameterIndexOffset();
                if (parameterIndex < 1 || parameterIndex > this.staticSqlStrings.length) {
                    throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
                if (parameterIndexOffset == -1 && parameterIndex == 1) {
                    throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
                this.parameterStreams[(parameterIndex - 1) + parameterIndexOffset] = x;
                this.isStream[(parameterIndex - 1) + parameterIndexOffset] = true;
                this.streamLengths[(parameterIndex - 1) + parameterIndexOffset] = length;
                this.isNull[(parameterIndex - 1) + parameterIndexOffset] = false;
                this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 2004;
            }
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        setBinaryStream(parameterIndex, inputStream, (int) length);
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int i, java.sql.Blob x) throws SQLException {
        if (x == null) {
            setNull(i, 2004);
            return;
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        bytesOut.write(39);
        escapeblockFast(x.getBytes(1L, (int) x.length()), bytesOut, (int) x.length());
        bytesOut.write(39);
        setInternal(i, bytesOut.toByteArray());
        this.parameterTypes[(i - 1) + getParameterIndexOffset()] = 2004;
    }

    @Override // java.sql.PreparedStatement
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        if (this.useTrueBoolean) {
            setInternal(parameterIndex, x ? "1" : "0");
        } else {
            setInternal(parameterIndex, x ? "'t'" : "'f'");
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 16;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setByte(int parameterIndex, byte x) throws SQLException {
        setInternal(parameterIndex, String.valueOf((int) x));
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = -6;
    }

    @Override // java.sql.PreparedStatement
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        setBytes(parameterIndex, x, true, true);
        if (x != null) {
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = -2;
        }
    }

    protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                String connectionEncoding = this.connection.getEncoding();
                try {
                    try {
                        if (this.connection.isNoBackslashEscapesSet() || (escapeForMBChars && this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding))) {
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream((x.length * 2) + 3);
                            bOut.write(120);
                            bOut.write(39);
                            for (int i = 0; i < x.length; i++) {
                                int lowBits = (x[i] & 255) / 16;
                                int highBits = (x[i] & 255) % 16;
                                bOut.write(HEX_DIGITS[lowBits]);
                                bOut.write(HEX_DIGITS[highBits]);
                            }
                            bOut.write(39);
                            setInternal(parameterIndex, bOut.toByteArray());
                            return;
                        }
                        int numBytes = x.length;
                        int pad = 2;
                        boolean needsIntroducer = checkForIntroducer && this.connection.versionMeetsMinimum(4, 1, 0);
                        if (needsIntroducer) {
                            pad = 2 + 7;
                        }
                        ByteArrayOutputStream bOut2 = new ByteArrayOutputStream(numBytes + pad);
                        if (needsIntroducer) {
                            bOut2.write(95);
                            bOut2.write(98);
                            bOut2.write(105);
                            bOut2.write(110);
                            bOut2.write(97);
                            bOut2.write(114);
                            bOut2.write(121);
                        }
                        bOut2.write(39);
                        for (byte b : x) {
                            switch (b) {
                                case 0:
                                    bOut2.write(92);
                                    bOut2.write(48);
                                    break;
                                case 10:
                                    bOut2.write(92);
                                    bOut2.write(110);
                                    break;
                                case 13:
                                    bOut2.write(92);
                                    bOut2.write(114);
                                    break;
                                case 26:
                                    bOut2.write(92);
                                    bOut2.write(90);
                                    break;
                                case 34:
                                    bOut2.write(92);
                                    bOut2.write(34);
                                    break;
                                case 39:
                                    bOut2.write(92);
                                    bOut2.write(39);
                                    break;
                                case 92:
                                    bOut2.write(92);
                                    bOut2.write(92);
                                    break;
                                default:
                                    bOut2.write(b);
                                    break;
                            }
                        }
                        bOut2.write(39);
                        setInternal(parameterIndex, bOut2.toByteArray());
                    } catch (RuntimeException ex) {
                        SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                        sqlEx.initCause(ex);
                        throw sqlEx;
                    }
                } catch (SQLException ex2) {
                    throw ex2;
                }
            }
        }
    }

    protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
        byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
        parameterWithQuotes[0] = 39;
        System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
        parameterWithQuotes[parameterAsBytes.length + 1] = 39;
        setInternal(parameterIndex, parameterWithQuotes);
    }

    protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
        setInternal(parameterIndex, parameterAsBytes);
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                if (reader == null) {
                    setNull(parameterIndex, -1);
                } else {
                    boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
                    String forcedEncoding = this.connection.getClobCharacterEncoding();
                    if (useLength && length != -1) {
                        char[] c = new char[length];
                        int numCharsRead = readFully(reader, c, length);
                        if (forcedEncoding == null) {
                            setString(parameterIndex, new String(c, 0, numCharsRead));
                        } else {
                            try {
                                setBytes(parameterIndex, StringUtils.getBytes(new String(c, 0, numCharsRead), forcedEncoding));
                            } catch (UnsupportedEncodingException e) {
                                throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                            }
                        }
                    } else {
                        char[] c2 = new char[4096];
                        StringBuilder buf = new StringBuilder();
                        while (true) {
                            int len = reader.read(c2);
                            if (len == -1) {
                                break;
                            } else {
                                buf.append(c2, 0, len);
                            }
                        }
                        if (forcedEncoding == null) {
                            setString(parameterIndex, buf.toString());
                        } else {
                            try {
                                setBytes(parameterIndex, StringUtils.getBytes(buf.toString(), forcedEncoding));
                            } catch (UnsupportedEncodingException e2) {
                                throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                            }
                        }
                    }
                    this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 2005;
                }
            } catch (IOException ioEx) {
                throw SQLError.createSQLException(ioEx.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        }
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int i, java.sql.Clob x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(i, 2005);
            } else {
                String forcedEncoding = this.connection.getClobCharacterEncoding();
                if (forcedEncoding == null) {
                    setString(i, x.getSubString(1L, (int) x.length()));
                } else {
                    try {
                        setBytes(i, StringUtils.getBytes(x.getSubString(1L, (int) x.length()), forcedEncoding));
                    } catch (UnsupportedEncodingException e) {
                        throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                }
                this.parameterTypes[(i - 1) + getParameterIndexOffset()] = 2005;
            }
        }
    }

    @Override // java.sql.PreparedStatement
    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
        setDate(parameterIndex, x, null);
    }

    @Override // java.sql.PreparedStatement
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 91);
            return;
        }
        if (!this.useLegacyDatetimeCode) {
            newSetDateInternal(parameterIndex, x, cal);
            return;
        }
        synchronized (checkClosed().getConnectionMutex()) {
            this.ddf = TimeUtil.getSimpleDateFormat(this.ddf, "''yyyy-MM-dd''", cal, null);
            setInternal(parameterIndex, this.ddf.format((Date) x));
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 91;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setDouble(int parameterIndex, double x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.connection.getAllowNanAndInf() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || Double.isNaN(x))) {
                throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 8;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setFloat(int parameterIndex, float x) throws SQLException {
        setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 6;
    }

    @Override // java.sql.PreparedStatement
    public void setInt(int parameterIndex, int x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 4;
    }

    protected final void setInternal(int paramIndex, byte[] val) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            int parameterIndexOffset = getParameterIndexOffset();
            checkBounds(paramIndex, parameterIndexOffset);
            this.isStream[(paramIndex - 1) + parameterIndexOffset] = false;
            this.isNull[(paramIndex - 1) + parameterIndexOffset] = false;
            this.parameterStreams[(paramIndex - 1) + parameterIndexOffset] = null;
            this.parameterValues[(paramIndex - 1) + parameterIndexOffset] = val;
        }
    }

    protected void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (paramIndex < 1) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (paramIndex > this.parameterCount) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (parameterIndexOffset == -1 && paramIndex == 1) {
                throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
        }
    }

    protected final void setInternal(int paramIndex, String val) throws SQLException {
        byte[] parameterAsBytes;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.charConverter != null) {
                parameterAsBytes = this.charConverter.toBytes(val);
            } else {
                parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
            }
            setInternal(paramIndex, parameterAsBytes);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setLong(int parameterIndex, long x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = -5;
    }

    @Override // java.sql.PreparedStatement
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setInternal(parameterIndex, "null");
            this.isNull[(parameterIndex - 1) + getParameterIndexOffset()] = true;
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 0;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNull(int parameterIndex, int sqlType, String arg) throws SQLException {
        setNull(parameterIndex, sqlType);
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 0;
    }

    private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException, NumberFormatException {
        Number parameterAsNum;
        BigDecimal scaledBigDecimal;
        if (parameterObj instanceof Boolean) {
            parameterAsNum = ((Boolean) parameterObj).booleanValue() ? 1 : 0;
        } else if (parameterObj instanceof String) {
            switch (targetSqlType) {
                case -7:
                    if ("1".equals(parameterObj) || "0".equals(parameterObj)) {
                        parameterAsNum = Integer.valueOf((String) parameterObj);
                        break;
                    } else {
                        boolean parameterAsBoolean = "true".equalsIgnoreCase((String) parameterObj);
                        parameterAsNum = parameterAsBoolean ? 1 : 0;
                        break;
                    }
                case -6:
                case 4:
                case 5:
                    parameterAsNum = Integer.valueOf((String) parameterObj);
                    break;
                case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                    parameterAsNum = Long.valueOf((String) parameterObj);
                    break;
                case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                case -3:
                case -2:
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                default:
                    parameterAsNum = new BigDecimal((String) parameterObj);
                    break;
                case 6:
                case 8:
                    parameterAsNum = Double.valueOf((String) parameterObj);
                    break;
                case 7:
                    parameterAsNum = Float.valueOf((String) parameterObj);
                    break;
            }
        } else {
            parameterAsNum = (Number) parameterObj;
        }
        switch (targetSqlType) {
            case -7:
            case -6:
            case 4:
            case 5:
                setInt(parameterIndex, parameterAsNum.intValue());
                return;
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                setLong(parameterIndex, parameterAsNum.longValue());
                return;
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            default:
                return;
            case 2:
            case 3:
                if (parameterAsNum instanceof BigDecimal) {
                    try {
                        scaledBigDecimal = ((BigDecimal) parameterAsNum).setScale(scale);
                    } catch (ArithmeticException e) {
                        try {
                            scaledBigDecimal = ((BigDecimal) parameterAsNum).setScale(scale, 4);
                        } catch (ArithmeticException e2) {
                            throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                    setBigDecimal(parameterIndex, scaledBigDecimal);
                    return;
                }
                if (parameterAsNum instanceof BigInteger) {
                    setBigDecimal(parameterIndex, new BigDecimal((BigInteger) parameterAsNum, scale));
                    return;
                } else {
                    setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
                    return;
                }
            case 6:
            case 8:
                setDouble(parameterIndex, parameterAsNum.doubleValue());
                return;
            case 7:
                setFloat(parameterIndex, parameterAsNum.floatValue());
                return;
        }
    }

    public void setObject(int parameterIndex, Object parameterObj) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (parameterObj == null) {
                setNull(parameterIndex, MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE);
            } else if (parameterObj instanceof Byte) {
                setInt(parameterIndex, ((Byte) parameterObj).intValue());
            } else if (parameterObj instanceof String) {
                setString(parameterIndex, (String) parameterObj);
            } else if (parameterObj instanceof BigDecimal) {
                setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
            } else if (parameterObj instanceof Short) {
                setShort(parameterIndex, ((Short) parameterObj).shortValue());
            } else if (parameterObj instanceof Integer) {
                setInt(parameterIndex, ((Integer) parameterObj).intValue());
            } else if (parameterObj instanceof Long) {
                setLong(parameterIndex, ((Long) parameterObj).longValue());
            } else if (parameterObj instanceof Float) {
                setFloat(parameterIndex, ((Float) parameterObj).floatValue());
            } else if (parameterObj instanceof Double) {
                setDouble(parameterIndex, ((Double) parameterObj).doubleValue());
            } else if (parameterObj instanceof byte[]) {
                setBytes(parameterIndex, (byte[]) parameterObj);
            } else if (parameterObj instanceof java.sql.Date) {
                setDate(parameterIndex, (java.sql.Date) parameterObj);
            } else if (parameterObj instanceof Time) {
                setTime(parameterIndex, (Time) parameterObj);
            } else if (parameterObj instanceof Timestamp) {
                setTimestamp(parameterIndex, (Timestamp) parameterObj);
            } else if (parameterObj instanceof Boolean) {
                setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
            } else if (parameterObj instanceof InputStream) {
                setBinaryStream(parameterIndex, (InputStream) parameterObj, -1);
            } else if (parameterObj instanceof java.sql.Blob) {
                setBlob(parameterIndex, (java.sql.Blob) parameterObj);
            } else if (parameterObj instanceof java.sql.Clob) {
                setClob(parameterIndex, (java.sql.Clob) parameterObj);
            } else if (this.connection.getTreatUtilDateAsTimestamp() && (parameterObj instanceof Date)) {
                setTimestamp(parameterIndex, new Timestamp(((Date) parameterObj).getTime()));
            } else if (parameterObj instanceof BigInteger) {
                setString(parameterIndex, parameterObj.toString());
            } else {
                setSerializableObject(parameterIndex, parameterObj);
            }
        }
    }

    public void setObject(int parameterIndex, Object parameterObj, int targetSqlType) throws SQLException {
        if (!(parameterObj instanceof BigDecimal)) {
            setObject(parameterIndex, parameterObj, targetSqlType, 0);
        } else {
            setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal) parameterObj).scale());
        }
    }

    public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
        Date parameterAsDate;
        synchronized (checkClosed().getConnectionMutex()) {
            if (parameterObj == null) {
                setNull(parameterIndex, MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE);
            } else {
                try {
                    switch (targetSqlType) {
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
                            setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
                            break;
                        case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                        case -3:
                        case -2:
                        case 2004:
                            if (parameterObj instanceof byte[]) {
                                setBytes(parameterIndex, (byte[]) parameterObj);
                                break;
                            } else if (parameterObj instanceof java.sql.Blob) {
                                setBlob(parameterIndex, (java.sql.Blob) parameterObj);
                                break;
                            } else {
                                setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
                                break;
                            }
                        case -1:
                        case 1:
                        case 12:
                            if (parameterObj instanceof BigDecimal) {
                                setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal) parameterObj)));
                                break;
                            } else {
                                setString(parameterIndex, parameterObj.toString());
                                break;
                            }
                        case 16:
                            if (parameterObj instanceof Boolean) {
                                setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
                                break;
                            } else if (parameterObj instanceof String) {
                                setBoolean(parameterIndex, "true".equalsIgnoreCase((String) parameterObj) || !"0".equalsIgnoreCase((String) parameterObj));
                                break;
                            } else if (parameterObj instanceof Number) {
                                int intValue = ((Number) parameterObj).intValue();
                                setBoolean(parameterIndex, intValue != 0);
                                break;
                            } else {
                                throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                            }
                            break;
                        case 91:
                        case 93:
                            if (parameterObj instanceof String) {
                                ParsePosition pp = new ParsePosition(0);
                                DateFormat sdf = TimeUtil.getSimpleDateFormat(null, getDateTimePattern((String) parameterObj, false), null, null);
                                parameterAsDate = sdf.parse((String) parameterObj, pp);
                            } else {
                                parameterAsDate = (Date) parameterObj;
                            }
                            switch (targetSqlType) {
                                case 91:
                                    if (parameterAsDate instanceof java.sql.Date) {
                                        setDate(parameterIndex, (java.sql.Date) parameterAsDate);
                                        break;
                                    } else {
                                        setDate(parameterIndex, new java.sql.Date(parameterAsDate.getTime()));
                                        break;
                                    }
                                case 93:
                                    if (parameterAsDate instanceof Timestamp) {
                                        setTimestamp(parameterIndex, (Timestamp) parameterAsDate);
                                        break;
                                    } else {
                                        setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
                                        break;
                                    }
                            }
                            break;
                        case 92:
                            if (!(parameterObj instanceof String)) {
                                if (parameterObj instanceof Timestamp) {
                                    Timestamp xT = (Timestamp) parameterObj;
                                    setTime(parameterIndex, new Time(xT.getTime()));
                                    break;
                                } else {
                                    setTime(parameterIndex, (Time) parameterObj);
                                    break;
                                }
                            } else {
                                DateFormat sdf2 = TimeUtil.getSimpleDateFormat(null, getDateTimePattern((String) parameterObj, true), null, null);
                                setTime(parameterIndex, new Time(sdf2.parse((String) parameterObj).getTime()));
                                break;
                            }
                        case MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE /* 1111 */:
                            setSerializableObject(parameterIndex, parameterObj);
                            break;
                        case 2005:
                            if (parameterObj instanceof java.sql.Clob) {
                                setClob(parameterIndex, (java.sql.Clob) parameterObj);
                                break;
                            } else {
                                setString(parameterIndex, parameterObj.toString());
                                break;
                            }
                        default:
                            throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                    }
                } catch (Exception ex) {
                    if (ex instanceof SQLException) {
                        throw ((SQLException) ex);
                    }
                    SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }
            }
        }
    }

    protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
        BatchParams paramArg = (BatchParams) paramSet;
        boolean[] isNullBatch = paramArg.isNull;
        boolean[] isStreamBatch = paramArg.isStream;
        for (int j = 0; j < isNullBatch.length; j++) {
            if (isNullBatch[j]) {
                int i = batchedParamIndex;
                batchedParamIndex++;
                batchedStatement.setNull(i, 0);
            } else if (isStreamBatch[j]) {
                int i2 = batchedParamIndex;
                batchedParamIndex++;
                batchedStatement.setBinaryStream(i2, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
            } else {
                int i3 = batchedParamIndex;
                batchedParamIndex++;
                ((PreparedStatement) batchedStatement).setBytesNoEscapeNoQuotes(i3, paramArg.parameterStrings[j]);
            }
        }
        return batchedParamIndex;
    }

    @Override // java.sql.PreparedStatement
    public void setRef(int i, Ref x) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    private final void setSerializableObject(int parameterIndex, Object parameterObj) throws SQLException, IOException {
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
            objectOut.writeObject(parameterObj);
            objectOut.flush();
            objectOut.close();
            bytesOut.flush();
            bytesOut.close();
            byte[] buf = bytesOut.toByteArray();
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
            setBinaryStream(parameterIndex, (InputStream) bytesIn, buf.length);
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = -2;
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setShort(int parameterIndex, short x) throws SQLException {
        setInternal(parameterIndex, String.valueOf((int) x));
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 5;
    }

    @Override // java.sql.PreparedStatement
    public void setString(int parameterIndex, String x) throws SQLException {
        byte[] parameterAsBytes;
        byte[] parameterAsBytes2;
        byte[] parameterAsBytes3;
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, 1);
            } else {
                checkClosed();
                int stringLength = x.length();
                if (this.connection.isNoBackslashEscapesSet()) {
                    boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
                    if (!needsHexEscape) {
                        StringBuilder quotedString = new StringBuilder(x.length() + 2);
                        quotedString.append('\'');
                        quotedString.append(x);
                        quotedString.append('\'');
                        if (!this.isLoadDataQuery) {
                            parameterAsBytes3 = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                        } else {
                            parameterAsBytes3 = StringUtils.getBytes(quotedString.toString());
                        }
                        setInternal(parameterIndex, parameterAsBytes3);
                    } else {
                        if (!this.isLoadDataQuery) {
                            parameterAsBytes2 = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                        } else {
                            parameterAsBytes2 = StringUtils.getBytes(x);
                        }
                        setBytes(parameterIndex, parameterAsBytes2);
                    }
                    return;
                }
                String parameterAsString = x;
                boolean needsQuoted = true;
                if (this.isLoadDataQuery || isEscapeNeededForString(x, stringLength)) {
                    needsQuoted = false;
                    StringBuilder buf = new StringBuilder((int) (x.length() * 1.1d));
                    buf.append('\'');
                    for (int i = 0; i < stringLength; i++) {
                        char c = x.charAt(i);
                        switch (c) {
                            case 0:
                                buf.append('\\');
                                buf.append('0');
                                break;
                            case '\n':
                                buf.append('\\');
                                buf.append('n');
                                break;
                            case '\r':
                                buf.append('\\');
                                buf.append('r');
                                break;
                            case 26:
                                buf.append('\\');
                                buf.append('Z');
                                break;
                            case '\"':
                                if (this.usingAnsiMode) {
                                    buf.append('\\');
                                }
                                buf.append('\"');
                                break;
                            case '\'':
                                buf.append('\\');
                                buf.append('\'');
                                break;
                            case '\\':
                                buf.append('\\');
                                buf.append('\\');
                                break;
                            case 165:
                            case 8361:
                                if (this.charsetEncoder != null) {
                                    CharBuffer cbuf = CharBuffer.allocate(1);
                                    ByteBuffer bbuf = ByteBuffer.allocate(1);
                                    cbuf.put(c);
                                    cbuf.position(0);
                                    this.charsetEncoder.encode(cbuf, bbuf, true);
                                    if (bbuf.get(0) == 92) {
                                        buf.append('\\');
                                    }
                                }
                                buf.append(c);
                                break;
                            default:
                                buf.append(c);
                                break;
                        }
                    }
                    buf.append('\'');
                    parameterAsString = buf.toString();
                }
                if (!this.isLoadDataQuery) {
                    if (needsQuoted) {
                        parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                    } else {
                        parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                    }
                } else {
                    parameterAsBytes = StringUtils.getBytes(parameterAsString);
                }
                setInternal(parameterIndex, parameterAsBytes);
                this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 12;
            }
        }
    }

    private boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;
        for (int i = 0; i < stringLength; i++) {
            char c = x.charAt(i);
            switch (c) {
                case 0:
                    needsHexEscape = true;
                    break;
                case '\n':
                    needsHexEscape = true;
                    break;
                case '\r':
                    needsHexEscape = true;
                    break;
                case 26:
                    needsHexEscape = true;
                    break;
                case '\"':
                    needsHexEscape = true;
                    break;
                case '\'':
                    needsHexEscape = true;
                    break;
                case '\\':
                    needsHexEscape = true;
                    break;
            }
            if (needsHexEscape) {
                return needsHexEscape;
            }
        }
        return needsHexEscape;
    }

    @Override // java.sql.PreparedStatement
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTime(int parameterIndex, Time x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimeInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
        }
    }

    private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 92);
            return;
        }
        checkClosed();
        if (!this.useLegacyDatetimeCode) {
            newSetTimeInternal(parameterIndex, x, targetCalendar);
        } else {
            Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
            setInternal(parameterIndex, "'" + TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward).toString() + "'");
        }
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 92;
    }

    @Override // java.sql.PreparedStatement
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            int fractLen = -1;
            if (!this.sendFractionalSeconds || !this.serverSupportsFracSecs) {
                fractLen = 0;
            } else if (this.parameterMetaData != null && this.parameterMetaData.metadata != null && this.parameterMetaData.metadata.fields != null && parameterIndex <= this.parameterMetaData.metadata.fields.length && parameterIndex >= 0 && this.parameterMetaData.metadata.getField(parameterIndex).getDecimals() > 0) {
                fractLen = this.parameterMetaData.metadata.getField(parameterIndex).getDecimals();
            }
            setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true, fractLen, this.connection.getUseSSPSCompatibleTimezoneShift());
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            int fractLen = -1;
            if (!this.sendFractionalSeconds || !this.serverSupportsFracSecs) {
                fractLen = 0;
            } else if (this.parameterMetaData != null && this.parameterMetaData.metadata != null && this.parameterMetaData.metadata.fields != null && parameterIndex <= this.parameterMetaData.metadata.fields.length && parameterIndex >= 0) {
                fractLen = this.parameterMetaData.metadata.getField(parameterIndex).getDecimals();
            }
            setTimestampInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false, fractLen, this.connection.getUseSSPSCompatibleTimezoneShift());
        }
    }

    protected void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward, int fractionalLength, boolean useSSPSCompatibleTimezoneShift) throws SQLException {
        int nanos;
        if (x == null) {
            setNull(parameterIndex, 93);
            return;
        }
        checkClosed();
        Timestamp x2 = (Timestamp) x.clone();
        if (!this.serverSupportsFracSecs || (!this.sendFractionalSeconds && fractionalLength == 0)) {
            x2 = TimeUtil.truncateFractionalSeconds(x2);
        }
        if (fractionalLength < 0) {
            fractionalLength = 6;
        }
        Timestamp x3 = TimeUtil.adjustTimestampNanosPrecision(x2, fractionalLength, !this.connection.isServerTruncatesFracSecs());
        if (!this.useLegacyDatetimeCode) {
            newSetTimestampInternal(parameterIndex, x3, targetCalendar);
        } else {
            Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
            Timestamp x4 = TimeUtil.changeTimezone(this.connection, TimeUtil.setProlepticIfNeeded(sessionCalendar, targetCalendar), targetCalendar, x3, tz, this.connection.getServerTimezoneTZ(), rollForward);
            if (useSSPSCompatibleTimezoneShift) {
                doSSPSCompatibleTimezoneShift(parameterIndex, x4, fractionalLength, targetCalendar);
            } else {
                synchronized (this) {
                    this.tsdf = TimeUtil.getSimpleDateFormat(this.tsdf, "''yyyy-MM-dd HH:mm:ss", null, null);
                    Calendar adjCal = TimeUtil.setProlepticIfNeeded(this.tsdf.getCalendar(), targetCalendar);
                    if (this.tsdf.getCalendar() != adjCal) {
                        this.tsdf.setCalendar(adjCal);
                    }
                    StringBuffer buf = new StringBuffer();
                    buf.append(this.tsdf.format((Date) x4));
                    if (fractionalLength > 0 && (nanos = x4.getNanos()) != 0) {
                        buf.append('.');
                        buf.append(TimeUtil.formatNanos(nanos, this.serverSupportsFracSecs, fractionalLength));
                    }
                    buf.append('\'');
                    setInternal(parameterIndex, buf.toString());
                }
            }
        }
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 93;
    }

    private void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.tsdf = TimeUtil.getSimpleDateFormat(this.tsdf, "''yyyy-MM-dd HH:mm:ss", targetCalendar, targetCalendar != null ? null : this.connection.getServerTimezoneTZ());
            StringBuffer buf = new StringBuffer();
            buf.append(this.tsdf.format((Date) x));
            buf.append('.');
            buf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs, 6));
            buf.append('\'');
            setInternal(parameterIndex, buf.toString());
        }
    }

    private void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.tdf = TimeUtil.getSimpleDateFormat(this.tdf, "''HH:mm:ss''", targetCalendar, targetCalendar != null ? null : this.connection.getServerTimezoneTZ());
            setInternal(parameterIndex, this.tdf.format((Date) x));
        }
    }

    private void newSetDateInternal(int parameterIndex, java.sql.Date x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.ddf = TimeUtil.getSimpleDateFormat(this.ddf, "''yyyy-MM-dd''", targetCalendar, targetCalendar != null ? null : this.connection.getNoTimezoneConversionForDateType() ? this.connection.getDefaultTimeZone() : this.connection.getServerTimezoneTZ());
            setInternal(parameterIndex, this.ddf.format((Date) x));
        }
    }

    private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, int fractionalLength, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            Calendar sessionCalendar2 = TimeUtil.setProlepticIfNeeded(this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew(), targetCalendar);
            synchronized (sessionCalendar2) {
                Date oldTime = sessionCalendar2.getTime();
                try {
                    sessionCalendar2.setTime(x);
                    int year = sessionCalendar2.get(1);
                    int month = sessionCalendar2.get(2) + 1;
                    int date = sessionCalendar2.get(5);
                    int hour = sessionCalendar2.get(11);
                    int minute = sessionCalendar2.get(12);
                    int seconds = sessionCalendar2.get(13);
                    StringBuilder tsBuf = new StringBuilder();
                    tsBuf.append('\'');
                    tsBuf.append(year);
                    tsBuf.append("-");
                    if (month < 10) {
                        tsBuf.append('0');
                    }
                    tsBuf.append(month);
                    tsBuf.append('-');
                    if (date < 10) {
                        tsBuf.append('0');
                    }
                    tsBuf.append(date);
                    tsBuf.append(' ');
                    if (hour < 10) {
                        tsBuf.append('0');
                    }
                    tsBuf.append(hour);
                    tsBuf.append(':');
                    if (minute < 10) {
                        tsBuf.append('0');
                    }
                    tsBuf.append(minute);
                    tsBuf.append(':');
                    if (seconds < 10) {
                        tsBuf.append('0');
                    }
                    tsBuf.append(seconds);
                    tsBuf.append('.');
                    tsBuf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs, fractionalLength));
                    tsBuf.append('\'');
                    setInternal(parameterIndex, tsBuf.toString());
                } finally {
                    sessionCalendar2.setTime(oldTime);
                }
            }
        }
    }

    @Override // java.sql.PreparedStatement
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 12);
        } else {
            setBinaryStream(parameterIndex, x, length);
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 2005;
        }
    }

    @Override // java.sql.PreparedStatement
    public void setURL(int parameterIndex, URL arg) throws SQLException {
        if (arg != null) {
            setString(parameterIndex, arg.toString());
            this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 70;
        } else {
            setNull(parameterIndex, 1);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008d A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009d A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b4 A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00be A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00db A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0149 A[Catch: all -> 0x0155, all -> 0x017e, TryCatch #1 {all -> 0x0155, blocks: (B:4:0x000d, B:6:0x0014, B:7:0x001d, B:8:0x002b, B:10:0x0037, B:14:0x0048, B:16:0x0050, B:28:0x008d, B:30:0x00a8, B:32:0x00b4, B:38:0x00db, B:43:0x00eb, B:50:0x011b, B:52:0x012d, B:53:0x0137, B:46:0x00fd, B:47:0x010b, B:56:0x0149, B:33:0x00be, B:35:0x00d0, B:29:0x009d, B:21:0x0064, B:22:0x007b), top: B:85:0x000d, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x016b A[Catch: IOException -> 0x0172, all -> 0x017e, DONT_GENERATE, TRY_ENTER, TRY_LEAVE, TryCatch #2 {IOException -> 0x0172, blocks: (B:65:0x016b, B:80:0x016b), top: B:86:0x000d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void streamToBytes(com.mysql.jdbc.Buffer r6, java.io.InputStream r7, boolean r8, int r9, boolean r10) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 391
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.streamToBytes(com.mysql.jdbc.Buffer, java.io.InputStream, boolean, int, boolean):void");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private final byte[] streamToBytes(java.io.InputStream r6, boolean r7, int r8, boolean r9) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 330
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.PreparedStatement.streamToBytes(java.io.InputStream, boolean, int, boolean):byte[]");
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append(": ");
        try {
            buf.append(asSql());
        } catch (SQLException sqlEx) {
            buf.append("EXCEPTION: " + sqlEx.toString());
        }
        return buf.toString();
    }

    protected int getParameterIndexOffset() {
        return 0;
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        setAsciiStream(parameterIndex, x, -1);
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setAsciiStream(parameterIndex, x, (int) length);
        this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 2005;
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        setBinaryStream(parameterIndex, x, -1);
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setBinaryStream(parameterIndex, x, (int) length);
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        setBinaryStream(parameterIndex, inputStream);
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        setCharacterStream(parameterIndex, reader, -1);
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, (int) length);
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        setCharacterStream(parameterIndex, reader);
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, length);
    }

    @Override // java.sql.PreparedStatement
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        setNCharacterStream(parameterIndex, value, -1L);
    }

    public void setNString(int parameterIndex, String x) throws SQLException {
        byte[] parameterAsBytes;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
                setString(parameterIndex, x);
                return;
            }
            if (x == null) {
                setNull(parameterIndex, 1);
            } else {
                int stringLength = x.length();
                StringBuilder buf = new StringBuilder((int) ((x.length() * 1.1d) + 4.0d));
                buf.append("_utf8");
                buf.append('\'');
                for (int i = 0; i < stringLength; i++) {
                    char c = x.charAt(i);
                    switch (c) {
                        case 0:
                            buf.append('\\');
                            buf.append('0');
                            break;
                        case '\n':
                            buf.append('\\');
                            buf.append('n');
                            break;
                        case '\r':
                            buf.append('\\');
                            buf.append('r');
                            break;
                        case 26:
                            buf.append('\\');
                            buf.append('Z');
                            break;
                        case '\"':
                            if (this.usingAnsiMode) {
                                buf.append('\\');
                            }
                            buf.append('\"');
                            break;
                        case '\'':
                            buf.append('\\');
                            buf.append('\'');
                            break;
                        case '\\':
                            buf.append('\\');
                            buf.append('\\');
                            break;
                        default:
                            buf.append(c);
                            break;
                    }
                }
                buf.append('\'');
                String parameterAsString = buf.toString();
                if (!this.isLoadDataQuery) {
                    parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                } else {
                    parameterAsBytes = StringUtils.getBytes(parameterAsString);
                }
                setInternal(parameterIndex, parameterAsBytes);
                this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = -9;
            }
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                if (reader == null) {
                    setNull(parameterIndex, -1);
                } else {
                    boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
                    if (useLength && length != -1) {
                        char[] c = new char[(int) length];
                        int numCharsRead = readFully(reader, c, (int) length);
                        setNString(parameterIndex, new String(c, 0, numCharsRead));
                    } else {
                        char[] c2 = new char[4096];
                        StringBuilder buf = new StringBuilder();
                        while (true) {
                            int len = reader.read(c2);
                            if (len == -1) {
                                break;
                            } else {
                                buf.append(c2, 0, len);
                            }
                        }
                        setNString(parameterIndex, buf.toString());
                    }
                    this.parameterTypes[(parameterIndex - 1) + getParameterIndexOffset()] = 2011;
                }
            } catch (IOException ioEx) {
                throw SQLError.createSQLException(ioEx.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        setNCharacterStream(parameterIndex, reader);
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        if (reader == null) {
            setNull(parameterIndex, -1);
        } else {
            setNCharacterStream(parameterIndex, reader, length);
        }
    }

    public ParameterBindings getParameterBindings() throws SQLException {
        EmulatedPreparedStatementBindings emulatedPreparedStatementBindings;
        synchronized (checkClosed().getConnectionMutex()) {
            emulatedPreparedStatementBindings = new EmulatedPreparedStatementBindings();
        }
        return emulatedPreparedStatementBindings;
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PreparedStatement$EmulatedPreparedStatementBindings.class */
    class EmulatedPreparedStatementBindings implements ParameterBindings {
        private ResultSetImpl bindingsAsRs;
        private boolean[] parameterIsNull;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v8, types: [byte[], byte[][]] */
        EmulatedPreparedStatementBindings() throws SQLException {
            int charsetIndex;
            List<ResultSetRow> rows = new ArrayList<>();
            this.parameterIsNull = new boolean[PreparedStatement.this.parameterCount];
            System.arraycopy(PreparedStatement.this.isNull, 0, this.parameterIsNull, 0, PreparedStatement.this.parameterCount);
            ?? r0 = new byte[PreparedStatement.this.parameterCount];
            Field[] typeMetadata = new Field[PreparedStatement.this.parameterCount];
            for (int i = 0; i < PreparedStatement.this.parameterCount; i++) {
                if (PreparedStatement.this.batchCommandIndex == -1) {
                    r0[i] = PreparedStatement.this.getBytesRepresentation(i);
                } else {
                    r0[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, PreparedStatement.this.batchCommandIndex);
                }
                if (PreparedStatement.this.parameterTypes[i] == -2 || PreparedStatement.this.parameterTypes[i] == 2004) {
                    charsetIndex = 63;
                } else {
                    try {
                        charsetIndex = CharsetMapping.getCollationIndexForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);
                    } catch (RuntimeException ex) {
                        SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                        sqlEx.initCause(ex);
                        throw sqlEx;
                    } catch (SQLException ex2) {
                        throw ex2;
                    }
                }
                Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, PreparedStatement.this.parameterTypes[i], r0[i].length);
                parameterMetadata.setConnection(PreparedStatement.this.connection);
                typeMetadata[i] = parameterMetadata;
            }
            rows.add(new ByteArrayRow(r0, PreparedStatement.this.getExceptionInterceptor()));
            this.bindingsAsRs = new ResultSetImpl(PreparedStatement.this.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), PreparedStatement.this.connection, null);
            this.bindingsAsRs.next();
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Array getArray(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getArray(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public InputStream getAsciiStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getAsciiStream(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBigDecimal(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public InputStream getBinaryStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBinaryStream(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public java.sql.Blob getBlob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBlob(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public boolean getBoolean(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBoolean(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public byte getByte(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getByte(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public byte[] getBytes(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBytes(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Reader getCharacterStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public java.sql.Clob getClob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getClob(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public java.sql.Date getDate(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getDate(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public double getDouble(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getDouble(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public float getFloat(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getFloat(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public int getInt(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getInt(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public long getLong(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getLong(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Reader getNCharacterStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Reader getNClob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Object getObject(int parameterIndex) throws SQLException {
            PreparedStatement.this.checkBounds(parameterIndex, 0);
            if (this.parameterIsNull[parameterIndex - 1]) {
                return null;
            }
            switch (PreparedStatement.this.parameterTypes[parameterIndex - 1]) {
                case -6:
                    return Byte.valueOf(getByte(parameterIndex));
                case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                    return Long.valueOf(getLong(parameterIndex));
                case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                case -3:
                case -2:
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                case 7:
                default:
                    return this.bindingsAsRs.getObject(parameterIndex);
                case 4:
                    return Integer.valueOf(getInt(parameterIndex));
                case 5:
                    return Short.valueOf(getShort(parameterIndex));
                case 6:
                    return Float.valueOf(getFloat(parameterIndex));
                case 8:
                    return Double.valueOf(getDouble(parameterIndex));
            }
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Ref getRef(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getRef(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public short getShort(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getShort(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public String getString(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getString(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Time getTime(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getTime(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public Timestamp getTimestamp(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getTimestamp(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public URL getURL(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getURL(parameterIndex);
        }

        @Override // com.mysql.jdbc.ParameterBindings
        public boolean isNull(int parameterIndex) throws SQLException {
            PreparedStatement.this.checkBounds(parameterIndex, 0);
            return this.parameterIsNull[parameterIndex - 1];
        }
    }

    public String getPreparedSql() {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (this.rewrittenBatchSize == 0) {
                    return this.originalSql;
                }
                try {
                    return this.parseInfo.getSqlForBatch(this.parseInfo);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement
    public int getUpdateCount() throws SQLException {
        int count = super.getUpdateCount();
        if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate && (count == 2 || count == 0)) {
            count = 1;
        }
        return count;
    }

    protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos) {
        int updateClausePos;
        if (!StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos)) {
            return StringUtils.startsWithIgnoreCaseAndWs(sql, "REPLACE", statementStartPos) && StringUtils.indexOfIgnoreCase(statementStartPos, sql, "SELECT", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) == -1;
        }
        if (StringUtils.indexOfIgnoreCase(statementStartPos, sql, "SELECT", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) != -1) {
            return false;
        }
        return !isOnDuplicateKeyUpdate || (updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ")) == -1 || StringUtils.indexOfIgnoreCase(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) == -1;
    }

    public long executeLargeUpdate() throws SQLException {
        return executeUpdateInternal(true, false);
    }
}
